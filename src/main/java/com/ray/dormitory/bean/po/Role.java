package com.ray.dormitory.bean.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * <p>
 * 角色
 * </p>
 *
 * @author Ray Z
 * @date 2019/10/26 18:12:39
 */
@Getter
@Setter
@TableName("role")
public class Role {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @NotBlank(message = "角色英文名不能为空")
    @Size(max = 16, message = "角色英文名长度不超过16")
    private String name;

    @NotBlank(message = "角色中文名不能为空")
    @Size(max = 16, message = "角色中文名长度不超过16")
    private String nameZh;

    @NotBlank(message = "角色描述不能为空")
    @Size(max = 64, message = "角色描述长度不超过64")
    private String description;

}
