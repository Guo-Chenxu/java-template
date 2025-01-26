package com.guochenxu.javatemplate;

import com.guochenxu.javatemplate.entity.AdminUser;
import com.guochenxu.javatemplate.service.AdminUserService;
import com.guochenxu.javatemplate.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 服务测试
 *
 * @author: guoch
 * @create: 2025-01-15 00:41
 * @version: 1.0
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class ServiceTest {

    @Resource
    private AdminUserService adminUserService;

    @Test
    public void testInsert() {
        AdminUser admin = AdminUser.builder()
                .username("admin")
                .password("admin")
                .build();
        admin.encryptPassword();
        adminUserService.save(admin);
    }
}
