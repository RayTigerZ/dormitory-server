package com.ray.dormitory.web.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ray.dormitory.bean.bo.QuestionnaireOption;
import com.ray.dormitory.bean.po.Questionnaire;
import com.ray.dormitory.service.QuestionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : Ray
 * @date : 2019.12.08 14:59
 */
@RestController
@RequestMapping("/questionnaires")
public class QuestionnaireController {
    @Autowired
    private QuestionnaireService questionnaireService;

    @GetMapping("")
    public List<Questionnaire> list() {
        return questionnaireService.list();
    }

    @GetMapping("/{id}")
    public Questionnaire getById(@PathVariable int id) {
        return questionnaireService.getById(id);
    }

    @PostMapping("")
    public boolean saveOrUpdate(@RequestBody @Valid Questionnaire questionnaire) {
        return questionnaireService.saveOrUpdate(questionnaire);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable int id) {
        return questionnaireService.removeById(id);
    }

    @PostMapping("/{id}/publish")
    public boolean publish(@PathVariable int id) {
        return questionnaireService.update(Wrappers.<Questionnaire>lambdaUpdate().eq(Questionnaire::getId, id).set(Questionnaire::getPublished, true));
    }

    @GetMapping("/options")
    public List<QuestionnaireOption> getOptions() {
        return questionnaireService.list(Wrappers.<Questionnaire>lambdaQuery().select(Questionnaire::getId, Questionnaire::getTitle).eq(Questionnaire::getPublished, true))
                .stream().map(QuestionnaireOption::convert).collect(Collectors.toList());
    }
}
