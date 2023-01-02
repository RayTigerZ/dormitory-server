package com.ray.dormitory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ray.dormitory.infrastructure.entity.RepairRecord;

import java.time.LocalDateTime;


/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Ray
 * @date 2020-2-24 16:41:23
 */
public interface RepairRecordService extends IService<RepairRecord> {
    boolean setRepairer(int id, String account);

    boolean setAppointTime(int id, LocalDateTime time);

    boolean setFeedback(int id, String feedBack);

}
