package com.ray.dormitory.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ray.dormitory.bean.po.Answer;
import com.ray.dormitory.bean.po.Questionnaire;
import com.ray.dormitory.bean.po.Survey;
import com.ray.dormitory.bean.po.User;
import com.ray.dormitory.service.AnswerService;
import com.ray.dormitory.service.QuestionnaireService;
import com.ray.dormitory.service.SurveyService;
import com.ray.dormitory.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        return surveyService.page(page);
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
        if (account.length() == 12) {
            Integer userId = user.getId();
            String grade = account.substring(0, 4);
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


}
