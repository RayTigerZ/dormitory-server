package com.ray.dormitory.bean.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ray.dormitory.util.Export;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
public class Cost implements Serializable, Export {


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
     * 收费项目
     */
    private String chargeName;

    /**
     * 单价
     */
    private Double chargePrice;

    /**
     * 单位
     */
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

    @Ignore
    private static List<String> key;
    @Ignore
    private static List<String> header;

    static {
        key = new ArrayList<>();
        key.add("roomNum");
        key.add("chargeName");
        key.add("chargePrice");
        key.add("chargeUnit");
        key.add("count");
        key.add("cycle");
        key.add("createTime");
        key.add("isPayed");
        key.add("payTime");

        header = new ArrayList<>();
        header.add("宿舍号");
        header.add("收费项目");
        header.add("单价");
        header.add("单位");
        header.add("数量");
        header.add("缴费周期");
        header.add("生成时间");
        header.add("缴费状态");
        header.add("缴费时间");

    }

    @Override
    public List<String> getKey() {
        return key;
    }

    @Override
    public List<String> getHeader() {
        return header;
    }

}
