package com.ray.dormitory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ray.dormitory.bean.po.Role;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Ray
 * @date 2019/11/21 10:18
 */
@Repository
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 获取用户所有的角色信息
     *
     * @param userId 用户ID
     * @return
     */
    List<Role> getRoleOfUser(int userId);
}
