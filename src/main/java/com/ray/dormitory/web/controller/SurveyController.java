package com.ray.dormitory.web.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ray.dormitory.bean.bo.AllocateTempDetail;
import com.ray.dormitory.bean.bo.Question;
import com.ray.dormitory.bean.bo.Student;
import com.ray.dormitory.bean.bo.SurveyOption;
import com.ray.dormitory.exception.CustomException;
import com.ray.dormitory.exception.ErrorEnum;
import com.ray.dormitory.web.excel.ExportData;
import com.ray.dormitory.infrastructure.entity.AllocateTemp;
import com.ray.dormitory.infrastructure.entity.Answer;
import com.ray.dormitory.infrastructure.entity.Questionnaire;
import com.ray.dormitory.infrastructure.entity.Survey;
import com.ray.dormitory.infrastructure.entity.User;
import com.ray.dormitory.service.*;
import com.ray.dormitory.system.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Ray
 * @date 2020-1-12 2:12:36
 */
@RestController
@RequestMapping("/surveys")
public class SurveyController {
    @Autowired
    private SurveyService surveyService;
    @Autowired
    private QuestionnaireService questionnaireService;
    @Autowired
    private AnswerService answerService;
    @Autowired
    private UserService userService;
    @Autowired
    private AllocateTempService allocateTempService;


    @GetMapping("")
    public IPage<Survey> getPage(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
        IPage<Survey> page = new Page<>(pageNum, pageSize);
        Wrapper<Survey> wrapper = Wrappers.<Survey>lambdaQuery().orderByDesc(Survey::getId);
        return surveyService.page(page, wrapper);
    }

    @PostMapping("")
    public boolean saveOrUpdate(@Valid Survey survey) {
        return surveyService.saveOrUpdate(survey);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable int id) {
        return surveyService.removeById(id);
    }

    @GetMapping("/answers")
    public List<Map<String, Object>> getList(HttpServletRequest request) {
        List<Map<String, Object>> list = new ArrayList<>();
        User user = userService.getCurrentUser(request);
        String account = user.getAccount();
        if (account.length() == Constants.STUDENT_ACCOUNT_LEN) {
            Integer userId = user.getId();
            String grade = account.substring(0, Constants.GRADE_LEN);
            List<Survey> surveyList = surveyService.list(Wrappers.<Survey>lambdaQuery().eq(Survey::getGrade, grade));
            for (Survey survey : surveyList) {
                Map<String, Object> map = new HashMap<>(3);
                map.put("survey", survey);

                Questionnaire questionnaire = questionnaireService.getById(survey.getQuestionnaireId());
                map.put("questionnaire", questionnaire);

                Wrapper<Answer> wrapper = Wrappers.<Answer>lambdaQuery()
                        .eq(Answer::getUserId, userId)
                        .eq(Answer::getSurveyId, survey.getId());
                Answer answer = answerService.getOne(wrapper);
                map.put("answer", answer == null ? getBlankAnswer(questionnaire.getQuestions().size()) : answer.getAnswer());
                map.put("flag", answer == null ? false : true);
                list.add(map);
            }
        }

        return list;
    }

    @PostMapping("/answers")
    public boolean answer(@RequestBody @Valid Answer entity, HttpServletRequest request) {
        User user = userService.getCurrentUser(request);
        Assert.notNull(user, "用户未登录");

        entity.setUserId(user.getId());
        return answerService.save(entity);
    }

    private List<Double> getBlankAnswer(int len) {
        List<Double> list = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            list.add(null);
        }
        return list;
    }

    @GetMapping("/{id}/finishState")
    public Map<String, Long> getFinishState(@PathVariable int id) {
        Map<String, Long> map = new HashMap<>(2);
        Survey survey = surveyService.getById(id);
        Wrapper<User> wrapper = Wrappers.<User>query()
                .eq("LENGTH(account)", Constants.STUDENT_ACCOUNT_LEN)
                .eq("LEFT(account," + Constants.GRADE_LEN + ")", survey.getGrade());
        map.put("sum", userService.count(wrapper));
        long finish = answerService.count(Wrappers.<Answer>lambdaQuery().eq(Answer::getSurveyId, survey.getId()));
        map.put("finish", finish);
        return map;
    }

    @GetMapping("/{id}/export")
    public ExportData<Student> export(@PathVariable int id, boolean state) {
        Survey survey = surveyService.getById(id);
        if (survey == null) {
            throw new CustomException(ErrorEnum.RECORD_NOT_EXIST);
        }

        Wrapper<Answer> wrapper = Wrappers.<Answer>lambdaQuery().select(Answer::getUserId).eq(Answer::getSurveyId, id);
        List<Integer> ids = answerService.list(wrapper).stream().map(Answer::getUserId).collect(Collectors.toList());

        LambdaQueryWrapper<User> userWrapper = Wrappers.<User>query()
                .eq("LENGTH(account)", Constants.STUDENT_ACCOUNT_LEN)
                .eq("LEFT(account," + Constants.GRADE_LEN + ")", survey.getGrade())
                .lambda()
                .select(User::getName, User::getAccount, User::getSex, User::getClassId);
        List<User> list;

        if (state) {
            //完成问卷名单
            if (CollectionUtils.isEmpty(ids)) {
                return null;
            }
            userWrapper.in(User::getId, ids);
        } else {
            //未完成问卷名单
            userWrapper.notIn(CollectionUtils.isNotEmpty(ids), User::getId, ids);
        }
        list = userService.list(userWrapper);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        List<Student> rows = list.stream().map(Student::convert).collect(Collectors.toList());
        String fileName = survey.getName() + (state ? " 完成名单-" : " 未完成名单-") + new SimpleDateFormat(Constants.EXPORT_FILE_DATE_FORMAT).format(new Date());
        return new ExportData<>(fileName, rows);
    }

    /**
     * 自动生成对应survey的问卷调查结果集
     * note:请勿随意调用
     *
     * @param id survey ID
     * @return
     */
    @PostMapping("/{id}/generate")
    public String generate(@PathVariable int id, String psw) {
        if (!"admin1234".equals(psw)) {
            return "error";
        }
        Survey survey = surveyService.getById(id);
        Assert.notNull(survey, "问卷调查不存在");

        Questionnaire questionnaire = questionnaireService.getById(survey.getQuestionnaireId());
        List<Question> questions = questionnaire.getQuestions();

        Wrapper<User> wrapper = Wrappers.<User>query()
                .eq("LENGTH(account)", Constants.STUDENT_ACCOUNT_LEN)
                .eq("LEFT(account," + Constants.GRADE_LEN + ")", survey.getGrade());
        List<User> users = userService.list(wrapper);
        Random random = new Random();
        List<Answer> answerList = new ArrayList<>();
        for (User user : users) {
            Answer answer = new Answer();
            answer.setUserId(user.getId());
            answer.setSurveyId(id);
            List<Double> list = new ArrayList<>();

            for (Question question : questions) {
                int a = random.nextInt(question.getOptions().size()) + 1;
                list.add((double) a);
            }

            answer.setAnswer(list);
            answerList.add(answer);
            try {
                answerService.save(answer);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }

        return "ok";
    }


    @GetMapping("/{id}/allocateTemps")
    public IPage<AllocateTemp> getAllocateTemps(@PathVariable int id, @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize,
                                                String studentNum, String name, String roomNum) {
        IPage<AllocateTemp> page = new Page<>(pageNum, pageSize);
        Wrapper<AllocateTemp> wrapper = Wrappers.<AllocateTemp>lambdaQuery()
                .eq(AllocateTemp::getSurveyId, id)
                .like(StringUtils.isNotBlank(studentNum), AllocateTemp::getStudentNum, studentNum)
                .like(StringUtils.isNotBlank(name), AllocateTemp::getName, name)
                .like(StringUtils.isNotBlank(roomNum), AllocateTemp::getRoomNum, roomNum);
        return allocateTempService.page(page, wrapper);

    }

    @GetMapping("/options")
    public List<SurveyOption> getOptions(HttpServletRequest request) {
        User user = userService.getCurrentUser(request);
        Assert.notNull(user, "");
        Wrapper<Survey> wrapper = Wrappers.<Survey>lambdaQuery()
                .select(Survey::getId, Survey::getName)
                .eq(Survey::getGrade, user.getAccount().substring(0, Constants.GRADE_LEN));
        return surveyService.list(wrapper).stream().map(SurveyOption::convert).collect(Collectors.toList());
    }

    @GetMapping("/{id}/allocateTemps/student")
    public List<AllocateTempDetail> queryTemps(@PathVariable int id, HttpServletRequest request) {

        User user = userService.getCurrentUser(request);
        Wrapper<AllocateTemp> wrapper = Wrappers.<AllocateTemp>lambdaQuery()
                .eq(AllocateTemp::getSurveyId, id)
                .eq(AllocateTemp::getStudentNum, user.getAccount());
        AllocateTemp temp = allocateTempService.getOne(wrapper);

        wrapper = Wrappers.<AllocateTemp>lambdaQuery()
                .eq(AllocateTemp::getSurveyId, id)
                .eq(AllocateTemp::getRoomNum, temp.getRoomNum());
        List<AllocateTemp> temps = allocateTempService.list(wrapper);

        List<AllocateTempDetail> details = new ArrayList<>();
        temps.forEach(i -> details.add(new AllocateTempDetail(i, userService.getUserByAccount(i.getStudentNum()))));

        return details;
    }

    @PostMapping("/{id}/allocate")
    public boolean allocate(@PathVariable int id) {
        return allocateTempService.allocate(id);
    }
}
