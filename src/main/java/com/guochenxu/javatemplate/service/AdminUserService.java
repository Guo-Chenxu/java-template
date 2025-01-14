package com.guochenxu.javatemplate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guochenxu.javatemplate.entity.AdminUser;

/**
 * (AdminUser)表服务接口
 *
 * @author makejava
 * @since 2024-10-30 21:25:31
 */

/**
 * AdminUser 表服务实现接口
 *
 * @author: guochenxu
 * @create: 2024-10-30 21:25
 * @version: 1.0
 */

public interface AdminUserService extends IService<AdminUser> {

    /**
     * 判断是否是管理员
     */
    boolean isAdmin(Long userId);
}
