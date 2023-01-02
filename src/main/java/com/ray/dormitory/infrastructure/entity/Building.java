package com.ray.dormitory.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ray.dormitory.infrastructure.enums.Sex;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    private Sex type;
}
