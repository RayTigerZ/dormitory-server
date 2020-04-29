package com.ray.dormitory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ray.dormitory.bean.bo.Count;
import com.ray.dormitory.bean.enums.CycleType;
import com.ray.dormitory.bean.po.VisitRecord;

import java.util.List;

/**
 * <p>
 * 来访记录表 服务类
 * </p>
 *
 * @author Ray
 * @date 2020-2-14 17:23:00
 */

public interface VisitRecordService extends IService<VisitRecord> {
    boolean leave(int id);

    List<Count> statistic(CycleType type, int last);
}
