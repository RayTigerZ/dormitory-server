package com.ray.dormitory.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.dormitory.bean.po.Survey;
import com.ray.dormitory.exception.CustomException;
import com.ray.dormitory.mapper.SurveyMapper;
import com.ray.dormitory.service.SurveyService;
import com.ray.dormitory.util.DateUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Ray
 * @date 2020-1-12 2:12:36
 */
@Service
public class SurveyServiceImpl extends ServiceImpl<SurveyMapper, Survey> implements SurveyService {

    @Override
    public boolean saveOrUpdate(Survey entity) {
        if (DateUtils.compare(entity.getBeginTime(), entity.getEndTime())) {
            return super.saveOrUpdate(entity);
        } else {
            throw new CustomException(204, "开始时间要小于结束时间");
        }
    }
}
