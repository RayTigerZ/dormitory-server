package com.ray.dormitory.infrastructure.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 通知
 * </p>
 *
 * @author Ray
 * @date 2020-2-28 0:19:18
 */
@Getter
@Setter
@NoArgsConstructor
@TableName("notice")
public class Notice implements Serializable {


    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 接收人账户
     */
    private String account;

    private String title;

    /**
     * 通知内容
     */
    private String content;

    /**
     * 是否阅读（0：否，1：是）
     */
    private Boolean isRead;


    @TableField(fill = FieldFill.INSERT)
    private String createUser;

    /**
     * 通知时间
     */
    @TableField(fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    public Notice(String account, String title, String content) {
        this.account = account;
        this.title = title;
        this.content = content;
    }


}
