package com.ray.dormitory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ray.dormitory.bean.po.AllocateTemp;

import java.util.Collection;

/**
 * <p>
 * 宿舍预分配结果表 服务类
 * </p>
 *
 * @author Ray
 * @date 2020-3-23 21:10:14
 */
public interface AllocateTempService extends IService<AllocateTemp> {
    /**
     * 保存surveyID的预分配结果，更新survey的状态
     *
     * @param surveyId   问卷调查ID
     * @param entityList
     * @return
     */

    boolean saveBatch(int surveyId, Collection<AllocateTemp> entityList);

    /**
     * 根据allocate_temp正式分配宿舍
     *
     * @param surveyId 问卷调查ID
     * @return
     */
    boolean allocate(int surveyId);

}
