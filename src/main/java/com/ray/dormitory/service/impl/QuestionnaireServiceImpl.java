package com.ray.dormitory.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.dormitory.bean.po.Questionnaire;
import com.ray.dormitory.exception.CustomException;
import com.ray.dormitory.mapper.QuestionnaireMapper;
import com.ray.dormitory.service.QuestionnaireService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : Ray
 * @date : 2019.12.08 14:58
 */
@Service
public class QuestionnaireServiceImpl extends ServiceImpl<QuestionnaireMapper, Questionnaire> implements QuestionnaireService {

    @Override
    public boolean updateById(Questionnaire entity) {
        Wrapper<Questionnaire> wrapper = Wrappers.<Questionnaire>lambdaQuery().select(Questionnaire::getPublished).eq(Questionnaire::getId, entity.getId());
        List<Object> objs = baseMapper.selectObjs(wrapper);
        boolean draft = (boolean) objs.get(0);
        if (draft) {
            throw new CustomException(204, "问卷为发布状态，无法修改");
        }
        return super.updateById(entity);
    }

}
