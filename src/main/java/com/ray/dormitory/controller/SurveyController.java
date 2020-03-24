package com.ray.dormitory.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ray.dormitory.bean.bo.Student;
import com.ray.dormitory.bean.po.Answer;
import com.ray.dormitory.bean.po.Questionnaire;
import com.ray.dormitory.bean.po.Survey;
import com.ray.dormitory.bean.po.User;
import com.ray.dormitory.exception.CustomException;
import com.ray.dormitory.exception.ErrorEnum;
import com.ray.dormitory.export.ExportData;
import com.ray.dormitory.service.AnswerService;
import com.ray.dormitory.service.QuestionnaireService;
import com.ray.dormitory.service.SurveyService;
import com.ray.dormitory.service.UserService;
import com.ray.dormitory.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
    public boolean answer(@RequestBody Answer entity) {
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
    public Map<String, Integer> getFinishState(@PathVariable int id) {
        Map<String, Integer> map = new HashMap<>(2);
        Survey survey = surveyService.getById(id);
        Wrapper<User> wrapper = Wrappers.<User>query()
                .eq("LENGTH(account)", Constants.STUDENT_ACCOUNT_LEN)
                .eq("LEFT(account," + Constants.GRADE_LEN + ")", survey.getGrade());
        map.put("sum", userService.count(wrapper));
        int finish = answerService.count(Wrappers.<Answer>lambdaQuery().eq(Answer::getSurveyId, survey.getId()));
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
        List<Student> rows = list.stream().map(Student::to).collect(Collectors.toList());
        String fileName = survey.getName() + (state ? " 完成名单-" : " 未完成名单-") + new SimpleDateFormat(Constants.EXPORT_FILE_DATE_FORMAT).format(new Date());
        return new ExportData<>(fileName, rows);
    }
}
