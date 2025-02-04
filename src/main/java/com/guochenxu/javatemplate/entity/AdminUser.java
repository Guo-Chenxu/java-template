package com.guochenxu.javatemplate.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.guochenxu.javatemplate.utils.HashUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;


/**
 * AdminUser 表实体类
 *
 * @author: guochenxu
 * @create: 2024-10-30 21:25
 * @version: 1.0
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class AdminUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String username;

    private String password;

    private Date createTime;

    private Date updateTime;

    private Integer deleted;

    public void encryptPassword() {
        this.password = HashUtil.sha256(this.password);
    }

    public void encryptPassword(String _password) {
        this.password = HashUtil.sha256(_password);
    }

    public boolean checkPassword(String _password) {
        return HashUtil.sha256(_password).equals(this.password);
    }
}
