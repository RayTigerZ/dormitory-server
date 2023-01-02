package com.ray.dormitory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ray.dormitory.infrastructure.entity.StayApply;

/**
 * <p>
 * 留校申请 服务类
 * </p>
 *
 * @author Ray
 * @date 2020-2-18 19:24:33
 */
public interface StayApplyService extends IService<StayApply> {
    boolean check(int id, boolean result);

}
