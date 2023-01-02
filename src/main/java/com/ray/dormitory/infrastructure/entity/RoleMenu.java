package com.ray.dormitory.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 角色--菜单联系表
 * </p>
 *
 * @author Ray
 * @date 2020-1-8 14:43:15
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@TableName("role_menu")
public class RoleMenu implements Serializable {

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 角色ID
     */
    private Integer roleId;

    /**
     * 菜单ID
     */
    private Integer menuId;


}
