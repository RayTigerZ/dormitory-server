package com.ray.dormitory.bean.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 账单
 * </p>
 *
 * @author Ray
 * @date 2020-2-26 14:56:49
 */
@Getter
@Setter
@TableName("cost")

public class Cost implements Serializable {


    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 宿舍号
     */
    private String roomNum;

    /**
     * 费用项
     */
    private String chargeName;

    private Double chargePrice;

    private String chargeUnit;

    /**
     * 用量计数
     */
    private Double count;

    /**
     * 账单年月
     */
    private String cycle;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 是否缴费
     */
    private Boolean isPayed;

    /**
     * 缴费时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payTime;


}
