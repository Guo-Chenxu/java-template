package com.guochenxu.javatemplate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guochenxu.javatemplate.constants.CacheKeys;
import com.guochenxu.javatemplate.entity.AdminUser;
import com.guochenxu.javatemplate.mapper.AdminUserMapper;
import com.guochenxu.javatemplate.service.AdminUserService;
import com.guochenxu.javatemplate.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * AdminUser 表服务实现类
 *
 * @author: guochenxu
 * @create: 2024-10-30 21:25
 * @version: 1.0
 */

@Service("adminUserService")
@Slf4j
public class AdminUserServiceImpl extends ServiceImpl<AdminUserMapper, AdminUser> implements AdminUserService {

    @Resource
    private RedisUtil redisUtil;
    @Override
    public boolean isAdmin(Long userId) {
        if (userId == null) {
            return false;
        }
        boolean pass = false;
        if (redisUtil.exist(CacheKeys.JAVA_TEMPLATE_ADMIN + userId)) {
            pass = true;
        } else if (this.getById(userId) != null) {
            pass = true;
            redisUtil.addWithExpireTime(CacheKeys.JAVA_TEMPLATE_ADMIN + userId, "", 3, TimeUnit.DAYS);
        }
        return pass;
    }
}
