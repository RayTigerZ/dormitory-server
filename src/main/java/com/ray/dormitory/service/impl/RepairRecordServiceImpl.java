package com.ray.dormitory.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.dormitory.infrastructure.entity.RepairRecord;
import com.ray.dormitory.infrastructure.entity.Room;
import com.ray.dormitory.infrastructure.entity.User;
import com.ray.dormitory.infrastructure.entity.UserRole;
import com.ray.dormitory.exception.CustomException;
import com.ray.dormitory.infrastructure.mapper.RepairRecordMapper;
import com.ray.dormitory.infrastructure.mapper.RoomMapper;
import com.ray.dormitory.infrastructure.mapper.UserMapper;
import com.ray.dormitory.service.RepairRecordService;
import com.ray.dormitory.service.UserRoleService;
import com.ray.dormitory.system.SysConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Ray
 * @date 2020-2-24 16:41:23
 */
@Service
public class RepairRecordServiceImpl extends ServiceImpl<RepairRecordMapper, RepairRecord> implements RepairRecordService {

    @Autowired
    private RoomMapper roomMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SysConfig sysConfig;
    @Autowired
    private UserRoleService userRoleService;

    @Override
    public boolean save(RepairRecord entity) {
        Wrapper<Room> wrapper = Wrappers.<Room>lambdaQuery().eq(Room::getNumber, entity.getRoomNum());
        if (roomMapper.selectCount(wrapper) == 0) {
            throw new CustomException(204, "宿舍不存在");
        }
        return super.save(entity);
    }

    @Override
    public boolean setRepairer(int id, String account) {
        RepairRecord record = getById(id);
        if (record.getStatus() == 2 || StringUtils.isNotBlank(record.getRepairer())) {
            throw new CustomException(204, "已安排维修人员，请勿重复安排");
        }
        User user = getRepairer(account);
        String repairer = String.format("%s(%s)", user.getName(), user.getAccount());
        Wrapper<RepairRecord> wrapper = Wrappers.<RepairRecord>lambdaUpdate()
                .set(RepairRecord::getRepairer, repairer)
                .set(RepairRecord::getStatus, 2)
                .eq(RepairRecord::getId, id);

        return update(wrapper);
    }

    @Override
    public boolean setAppointTime(int id, Date time) {
        RepairRecord record = getById(id);
        if (record.getStatus() == 3 || ObjectUtils.isNotNull(record.getAppointTime())) {
            throw new CustomException(204, "已安排上门时间，请勿重复安排");
        }
        Wrapper<RepairRecord> wrapper = Wrappers.<RepairRecord>lambdaUpdate()
                .set(RepairRecord::getAppointTime, time)
                .set(RepairRecord::getStatus, 3)
                .eq(RepairRecord::getId, id);

        return update(wrapper);
    }

    @Override
    public boolean setFeedback(int id, String feedBack) {
        RepairRecord record = getById(id);
        if (record.getStatus() == 4 || StringUtils.isNotBlank(record.getFeedback())) {
            throw new CustomException(204, "已填写反馈，请勿重复填写");
        }
        Wrapper<RepairRecord> wrapper = Wrappers.<RepairRecord>lambdaUpdate()
                .set(RepairRecord::getFeedback, feedBack)
                .set(RepairRecord::getStatus, 4)
                .eq(RepairRecord::getId, id);

        return update(wrapper);
    }


    private User getRepairer(String account) {
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getAccount, account));
        if (user == null) {
            throw new CustomException(204, "用户不存在");
        }
        long count = userRoleService.count(Wrappers.<UserRole>lambdaQuery()
                .eq(UserRole::getUserId, user.getId())
                .eq(UserRole::getRoleId, sysConfig.getRepairerRoleId()));
        if (count == 0) {
            throw new CustomException(204, "维修人员不存在");
        }
        return user;
    }


}
