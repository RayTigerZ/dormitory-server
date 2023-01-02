package com.ray.dormitory.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.dormitory.bean.bo.Count;
import com.ray.dormitory.bean.enums.CycleType;
import com.ray.dormitory.bean.po.User;
import com.ray.dormitory.bean.po.ViolationRecord;
import com.ray.dormitory.exception.CustomException;
import com.ray.dormitory.mapper.UserMapper;
import com.ray.dormitory.mapper.ViolationRecordMapper;
import com.ray.dormitory.service.ViolationRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 违规记录表 服务实现类
 * </p>
 *
 * @author Ray
 * @date 2020-2-15 16:37:24
 */
@Service
public class ViolationRecordServiceImpl extends ServiceImpl<ViolationRecordMapper, ViolationRecord> implements ViolationRecordService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean saveOrUpdate(ViolationRecord entity) {
        Wrapper<User> wrapper = Wrappers.<User>lambdaQuery().eq(User::getAccount, entity.getStudentNum()).eq(User::getName, entity.getStudentName());
        long count = userMapper.selectCount(wrapper);
        if (count == 0) {
            throw new CustomException(204, "学生学号和姓名不对应");
        }
        return super.saveOrUpdate(entity);
    }

    @Override
    public List<Count> statistic(CycleType type, int last) {
        List<Integer> list = new ArrayList<>();
        for (int i = last; i > 0; i--) {
            list.add(i - 1);
        }
        return baseMapper.statistic(type, list);
    }
}
