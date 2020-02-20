package com.ray.dormitory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ray.dormitory.bean.bo.OrganizationOption;
import com.ray.dormitory.bean.po.Organization;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author : Ray
 * @date : 2019.11.28 18:56
 */
@Repository
public interface OrganizationMapper extends BaseMapper<Organization> {
    List<OrganizationOption> getOptions();
}
