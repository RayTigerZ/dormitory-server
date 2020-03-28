package com.ray.dormitory.bean.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ray.dormitory.bean.enums.Sex;
import com.ray.dormitory.valid.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * <p>
 * 建筑（宿舍）
 * <p>
 *
 * @author : Ray
 * @date : 2019.11.22 14:46
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@TableName("building")
public class Building implements Serializable {

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 建筑名称
     */
    @NotBlank(message = "建筑名称不能为空")
    @Size(min = 1, max = 16, message = "建筑名称长度不超过16")
    private String name;

    /**
     * 类型：男/女
     */
    @EnumValue(target = Sex.class, message = "类型参数错误")
    private String type;
}
