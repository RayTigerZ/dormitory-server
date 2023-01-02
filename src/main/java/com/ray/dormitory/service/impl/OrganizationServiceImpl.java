package com.ray.dormitory.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.dormitory.bean.po.Organization;
import com.ray.dormitory.exception.CustomException;
import com.ray.dormitory.exception.ErrorEnum;
import com.ray.dormitory.mapper.OrganizationMapper;
import com.ray.dormitory.service.OrganizationService;
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
            return super.removeById(id);
        } else {
            throw new CustomException(ErrorEnum.CHILDREN_ORGANIZATION_EXIST);
        }


    }

    @Override
    public boolean save(Organization entity) {
        preprocess(entity);
        return super.save(entity);
    }


    @Override
    public List<Organization> list(Wrapper<Organization> queryWrapper) {
        List<Organization> organizations = super.list(queryWrapper);
        //将组织树结构的第三层 children属性设置为null，方便前端渲染
        organizations.forEach(i -> {
            i.getChildren().forEach(j -> {
                j.getChildren().forEach(k -> k.setChildren(null));
            });
        });
        return organizations;
    }

    private static int[] codeLens = {3, 5, 10};

    @Override
    public List<Organization> list(int level) {
        if (level < 1 || level > 3) {
            throw new CustomException(202, "层级参数错误：" + level);
        }
        Wrapper<Organization> wrapper = Wrappers.<Organization>lambdaQuery()
                .isNull(Organization::getParentId)
                .orderByAsc(Organization::getCode);

        List<Organization> list = baseMapper.selectList(wrapper);
        handleChildren(list, level);
        return list;
    }


    private void handleChildren(List<Organization> list, int level) {

        list.forEach(item -> {
            if (level == 1) {
                item.setChildren(null);
            } else {
                handleChildren(item.getChildren(), level - 1);
            }
        });

    }

    @Override
    public boolean updateById(Organization entity) {
        preprocess(entity);
        return super.updateById(entity);

    }


    private void preprocess(Organization entity) {

        int level = entity.getLevel();
        if (level < 1 || level > 3) {
            throw new CustomException(202, "层级参数错误：" + level);
        }

        //增加学院信息时，code和name在表中必须都是不存在的
        //更新学院信息时，code和name除表中自身数据外必须是不存在的
        Integer id = entity.getId();
        Wrapper<Organization> queryWrapper = Wrappers.<Organization>lambdaQuery()
                .ne(id != null, Organization::getId, id)
                .and(i -> i.eq(Organization::getCode, entity.getCode())
                        .or().eq(Organization::getName, entity.getName()));

        long count = baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new CustomException(202, "编码、名称重复");
        }

        if (codeLens[level - 1] != entity.getCode().length()) {
            throw new CustomException(202, "编码长度不符合要求");
        }

        if (level == 1) {
            entity.setParentId(null);
        } else {
            Integer parentId = entity.getParentId();
            String parentName = entity.getParent();
            if (parentId == null && StringUtils.isBlank(parentName)) {
                throw new CustomException(202, "父级参数错误");
            } else if (parentId != null) {
                Organization parent = baseMapper.selectById(parentId);
                if (parent == null) {
                    throw new CustomException(ErrorEnum.PARENT_NOT_EXIST);
                }
            } else if (StringUtils.isNotBlank(parentName)) {
                // 通过其名称找到parentId
                //否则抛出异常
                Wrapper<Organization> wrapper = Wrappers.<Organization>lambdaQuery().
                        eq(Organization::getName, parentName);
                Organization parent = baseMapper.selectOne(wrapper);
                if (parent == null) {
                    throw new CustomException(202, "不存在父级：" + parentName);
                }
                entity.setParentId(parent.getId());
            }

        }

    }


}
