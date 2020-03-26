package com.ray.dormitory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ray.dormitory.bean.po.Organization;

import java.util.List;

/**
 * @author : Ray
 * @date : 2019.11.28 19:43
 */
public interface OrganizationService extends IService<Organization> {


    List<Organization> list(int level);
}
