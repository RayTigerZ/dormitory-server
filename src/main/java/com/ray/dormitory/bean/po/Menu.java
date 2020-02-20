package com.ray.dormitory.bean.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * <p>
 * 前端菜单
 * </p>
 *
 * @author Ray
 * @date 2020-1-8 14:43:15
 */
@Getter
@Setter
@TableName(value = "menu", resultMap = "menuMap")
@JsonIgnoreProperties(value = {"handler"})
public class Menu {

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String title;
    private String name;
    private String path;
    private String component;

    private String icon;
    private Integer parentId;
    private Boolean noCache;
    private Boolean hidden;
    private String redirect;
    @TableField("`order`")
    private Integer order;

    @TableField(exist = false)
    private List<Menu> children;

}
