package com.ray.dormitory.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ray.dormitory.bean.po.Answer;
import com.ray.dormitory.bean.po.Survey;
import com.ray.dormitory.service.AnswerService;
import com.ray.dormitory.service.QuestionnaireService;
import com.ray.dormitory.service.SurveyService;
import com.ray.dormitory.util.JwtUtil;
import com.ray.dormitory.util.SysConfig;
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
    private SysConfig sysConfig;

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
        String token = request.getHeader(sysConfig.getTokenName());
        String account = JwtUtil.getAccount(token);
        if (account.length() == 12) {
            Integer userId = JwtUtil.getId(token);
            String grade = account.substring(0, 4);
            List<Survey> surveyList = surveyService.list(Wrappers.<Survey>lambdaQuery().eq(Survey::getGrade, grade));
            for (Survey survey : surveyList) {
                Map<String, Object> map = new HashMap<>(3);
                map.put("survey", survey);
                map.put("questionnaire", questionnaireService.getById(survey.getQuestionnaireId()));
                map.put("answer", answerService.getOne(Wrappers.<Answer>lambdaQuery().eq(Answer::getUserId, userId).eq(Answer::getSurveyId, survey.getId())));
            }
        }

        return list;
    }


}
