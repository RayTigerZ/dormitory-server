package com.ray.dormitory.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.dormitory.infrastructure.enums.AllocateStatus;
import com.ray.dormitory.infrastructure.entity.Survey;
import com.ray.dormitory.exception.CustomException;
import com.ray.dormitory.infrastructure.mapper.SurveyMapper;
import com.ray.dormitory.service.SurveyService;
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
        if (entity.getBeginTime().isBefore(entity.getEndTime())) {
            return super.saveOrUpdate(entity);
        } else {
            throw new CustomException(204, "开始时间要小于结束时间");
        }
    }

    @Override
    public boolean save(Survey entity) {
        entity.setStatus(AllocateStatus.NOT_ALLOCATE);
        return super.save(entity);
    }
}
