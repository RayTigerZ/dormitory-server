package com.ray.dormitory.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.dormitory.web.bo.Count;
import com.ray.dormitory.infrastructure.enums.CycleType;
import com.ray.dormitory.infrastructure.entity.VisitRecord;
import com.ray.dormitory.exception.CustomException;
import com.ray.dormitory.infrastructure.mapper.VisitRecordMapper;
import com.ray.dormitory.service.VisitRecordService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 来访记录表 服务实现类
 * </p>
 *
 * @author Ray
 * @date 2020-2-14 17:23:00
 */
@Service
public class VisitRecordServiceImpl extends ServiceImpl<VisitRecordMapper, VisitRecord> implements VisitRecordService {

    @Override
    public boolean leave(int id) {

        VisitRecord record = baseMapper.selectById(id);
        if (record != null) {
            if (record.getLeaveTime() == null) {
                Wrapper<VisitRecord> wrapper = Wrappers.<VisitRecord>lambdaUpdate().set(VisitRecord::getLeaveTime, LocalDateTime.now()).eq(VisitRecord::getId, id);
                return update(wrapper);
            } else {
                throw new CustomException(204, "该访客记录已填写离开时间");
            }
        } else {
            throw new CustomException(204, "访客记录不存在");
        }

    }

    @Override
    public List<Count> statistic(CycleType type, int last) {
        List<Integer> list = new ArrayList<>();
        for (int i = last; i > 0; i--) {
            list.add(i - 1);
        }
        return baseMapper.statistic(type, list);
    }

    @Override
    public boolean save(VisitRecord entity) {
        entity.setVisitTime(LocalDateTime.now());
        return super.save(entity);
    }
}
