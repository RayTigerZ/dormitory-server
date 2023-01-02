package com.ray.dormitory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ray.dormitory.web.bo.Count;
import com.ray.dormitory.infrastructure.enums.CycleType;
import com.ray.dormitory.infrastructure.entity.ViolationRecord;

import java.util.List;

/**
 * <p>
 * 违规记录表 服务类
 * </p>
 *
 * @author Ray
 * @date 2020-2-15 16:37:24
 */
public interface ViolationRecordService extends IService<ViolationRecord> {
    List<Count> statistic(CycleType type, int last);
}
