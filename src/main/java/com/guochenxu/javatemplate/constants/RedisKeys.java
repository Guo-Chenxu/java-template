package com.guochenxu.javatemplate.constants;

/**
 * redis中存储的键
 *
 * @author: 郭晨旭
 * @create: 2023-10-30 00:13
 * @version: 1.0
 */
public interface RedisKeys {

    /**
     * 手机验证码
     */
    String VERIFY_CODE_PHONE = "JAVA_TEMPLATE:VERIFY_CODE:PHONE:";

    /**
     * 邮箱验证码
     */
    String VERIFY_CODE_EMAIL = "JAVA_TEMPLATE:VERIFY_CODE:EMAIL:";
}
