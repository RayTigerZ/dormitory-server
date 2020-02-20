package com.ray.dormitory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ray.dormitory.bean.po.Menu;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * @author Ray
 * @date : 2019.11.21 13:21
 */
@Repository
public interface MenuMapper extends BaseMapper<Menu> {


    /**
     * 获取用户对应的菜单
     *
     * @param userId
     * @return
     */
    List<Menu> getMenusByUserId(int userId);


    /**
     * 通过用户ID获取该用户具有的API 路径
     *
     * @param userId 用户ID
     * @return
     */
    Set<String> getApiPermissionsOfUser(int userId);

    /**
     * 获取后端系统API的权限
     *
     * @return
     */
    List<Menu> getApiPermissionList();


}
