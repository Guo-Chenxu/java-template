package com.guochenxu.javatemplate.annotation;

import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.stp.StpUtil;
import com.guochenxu.javatemplate.constants.PermissionConstants;
import com.guochenxu.javatemplate.service.AdminUserService;
import com.guochenxu.javatemplate.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;

/**
 * 权限控制aop实现类
 *
 * @author: 郭晨旭
 * @create: 2024-01-26 00:44
 * @version: 1.0
 */

@Slf4j
@Component
@Aspect
@Order(2)
public class CheckPermissionAspect {

    @Resource
    private AdminUserService adminUserService;

    @Resource
    private UserService userService;

    @Before("@annotation(com.guochenxu.javatemplate.annotation.CheckPermission)")
    public void checkBefore(JoinPoint jp) throws Throwable {
        Class<?> targetCls = jp.getTarget().getClass();
        MethodSignature ms = (MethodSignature) jp.getSignature();
        Method method = targetCls.getDeclaredMethod(ms.getName(), ms.getParameterTypes());
        CheckPermission checkPermission = method.getAnnotation(CheckPermission.class);
        String[] needPermissions = checkPermission.value();

        long id = StpUtil.getLoginIdAsLong();
        boolean pass = false;
        for (int i = 0; i < needPermissions.length && !pass; i++) {
            switch (needPermissions[i]) {
                case PermissionConstants.ADMIN:
                    pass = adminUserService.isAdmin(id);
                    break;
                case PermissionConstants.USER:
                    pass = userService.isUser(id);
                    break;
            }
        }
        if (!pass) {
            throw new NotPermissionException("权限不足，无法访问");
        }
    }
}
