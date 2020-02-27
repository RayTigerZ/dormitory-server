package com.ray.dormitory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ray.dormitory.bean.po.Notice;

/**
 * <p>
 * 通知 服务类
 * </p>
 *
 * @author Ray
 * @date 2020-2-28 0:19:18
 */
public interface NoticeService extends IService<Notice> {

    boolean costNotice(int[] costIds);
}
