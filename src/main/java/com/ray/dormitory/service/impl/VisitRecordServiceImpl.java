package com.ray.dormitory.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.dormitory.bean.po.VisitRecord;
import com.ray.dormitory.exception.CustomException;
import com.ray.dormitory.mapper.VisitRecordMapper;
import com.ray.dormitory.service.VisitRecordService;
import org.springframework.stereotype.Service;

import java.util.Date;

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
                Wrapper<VisitRecord> wrapper = Wrappers.<VisitRecord>lambdaUpdate().set(VisitRecord::getLeaveTime, new Date()).eq(VisitRecord::getId, id);
                return update(wrapper);
            } else {
                throw new CustomException(204, "该访客记录已填写离开时间");
            }
        } else {
            throw new CustomException(204, "访客记录不存在");
        }

    }
}
