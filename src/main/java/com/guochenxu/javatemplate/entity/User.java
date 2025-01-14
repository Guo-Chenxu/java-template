package com.guochenxu.javatemplate.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;


/**
 * User 表实体类
 *
 * @author: guochenxu
 * @create: 2025-01-01 15:23
 * @version: 1.0
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("`id`")
    private Long id;

    @TableField("`nickname`")
    private String nickname;

    @TableField("`avatar`")
    private String avatar;

    @TableField("`open_id`")
    private String openId;

    @TableField("`phone`")
    private String phone;

    @TableField("`email`")
    private String email;

    @TableField("`remark`")
    private String remark;

    /**
     * 默认0=无,1=男,2=女
     */
    @TableField("`gender`")
    private Integer gender;

    @TableField("`personal_statement`")
    private String personalStatement;

    /**
     * 真实姓名
     */
    @TableField("`name`")
    private String name;

    /**
     * 黑名单
     */
    @TableField("`blocked`")
    private Boolean blocked;

    /**
     * 上次登录时间
     */
    @TableField("`last_login_time`")
    private Date lastLoginTime;

    @TableField("`create_time`")
    private Date createTime;

    @TableField("`update_time`")
    private Date updateTime;

    @TableField("`deleted`")
    private Integer deleted;


}

