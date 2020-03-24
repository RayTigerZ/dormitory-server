package com.ray.dormitory.bean.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 宿舍预分配结果表
 * </p>
 *
 * @author Ray
 * @date 2020-3-23 21:10:14
 */
@Getter
@Setter
@TableName("allocate_temp")
public class AllocateTemp implements Serializable {


    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 学号
     */
    private String studentNum;

    /**
     * 学生姓名
     */
    private String name;

    /**
     * 宿舍号
     */
    private String roomNum;

    /**
     * 问卷调查ID
     */
    private Integer surveyId;


}
