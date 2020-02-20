package com.ray.dormitory.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.dormitory.bean.bo.OrganizationOption;
import com.ray.dormitory.bean.po.Organization;
import com.ray.dormitory.exception.CustomException;
import com.ray.dormitory.mapper.OrganizationMapper;
import com.ray.dormitory.service.OrganizationService;
import com.ray.dormitory.util.bean.ErrorEnum;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * @author : Ray
 * @date : 2019.11.28 19:43
 */

@Service
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, Organization> implements OrganizationService {


    @Override
    public boolean removeById(Serializable id) {

        Wrapper<Organization> wrapper = Wrappers.<Organization>lambdaQuery().eq(Organization::getParentId, id);

        if (baseMapper.selectCount(wrapper) == 0) {
            return baseMapper.deleteById(id) > 0;
        } else {
            throw new CustomException(ErrorEnum.CHILDREN_ORGANIZATION_EXIST);
        }


    }

    @Override
    public boolean save(Organization organization) {

        String parentName = organization.getParent();
        Organization parent = null;

        // parent不为空时，通过其名称找到parentId
        //否则抛出异常
        if (parentName != null) {
            parent = baseMapper.selectOne(Wrappers.<Organization>lambdaQuery().eq(Organization::getName, parentName));
            if (parent == null) {
                throw new NullPointerException("不存在名字为'" + parentName + "'的组织");
            }
        }

        //增加学院信息时，code和name在表中必须都是不存在的
        //更新学院信息时，code和name除表中自身数据外必须是不存在的
        Integer id = organization.getId();
        Wrapper<Organization> queryWrapper = Wrappers.<Organization>lambdaQuery()
                .eq(ObjectUtils.isNotNull(id), Organization::getId, id)
                .and(i -> i.eq(Organization::getCode, organization.getCode())
                        .or().eq(Organization::getName, organization.getName()));

        int count = baseMapper.selectCount(queryWrapper);
        if (count == 0) {
            if (parent != null) {
                organization.setParentId(parent.getId());
            }

            return super.saveOrUpdate(organization);

        } else {
            throw new NullPointerException("组织编码、组织名称存在重复");
        }


    }

    @Override
    public List<OrganizationOption> getOptions() {
        return baseMapper.getOptions();
    }
}
