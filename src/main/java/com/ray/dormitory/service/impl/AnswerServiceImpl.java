package com.ray.dormitory.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.dormitory.infrastructure.entity.Answer;
import com.ray.dormitory.infrastructure.entity.Survey;
import com.ray.dormitory.infrastructure.entity.User;
import com.ray.dormitory.exception.CustomException;
import com.ray.dormitory.exception.ErrorEnum;
import com.ray.dormitory.infrastructure.mapper.AnswerMapper;
import com.ray.dormitory.infrastructure.mapper.SurveyMapper;
import com.ray.dormitory.infrastructure.mapper.UserMapper;
import com.ray.dormitory.service.AnswerService;
import com.ray.dormitory.system.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Ray
 * @date 2020-1-13 16:04:45
 */
@Service
public class AnswerServiceImpl extends ServiceImpl<AnswerMapper, Answer> implements AnswerService {
    @Autowired
    private SurveyMapper surveyMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean save(Answer entity) {
        int surveyId = entity.getSurveyId();
        int userId = entity.getUserId();

        Survey survey = surveyMapper.selectById(surveyId);
        if (survey == null) {
            throw new CustomException(ErrorEnum.RECORD_NOT_EXIST);
        }

        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new CustomException(ErrorEnum.RECORD_NOT_EXIST);
        }

        int grade = survey.getGrade();
        if (!String.valueOf(grade).equals(user.getAccount().substring(0, Constants.GRADE_LEN))) {
            throw new CustomException(202, "不能回答该问卷调查");
        }

        LocalDateTime now = LocalDateTime.now();
        if (!survey.getBeginTime().isBefore(now) || now.isBefore(survey.getEndTime())) {
            throw new CustomException(204, "不在问卷调查填写时间内");
        }

        //判断学生是否已经提交问卷
        Wrapper<Answer> wrapper = Wrappers.<Answer>lambdaQuery()
                .eq(Answer::getSurveyId, surveyId)
                .eq(Answer::getUserId, userId);
        if (count(wrapper) > 0) {
            throw new CustomException(204, "不能重复提交问卷");
        }


        return super.save(entity);
    }
}
