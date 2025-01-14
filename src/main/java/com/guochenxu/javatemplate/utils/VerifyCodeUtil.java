package com.guochenxu.javatemplate.utils;

import com.guochenxu.javatemplate.constants.RedisKeys;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 验证码工具
 *
 * @author: 郭晨旭
 * @create: 2023-10-30 00:26
 * @version: 1.0
 */
@Component
@Slf4j
public class VerifyCodeUtil {
    @Resource
    private RedisUtil redisUtil;

    @Resource
    private ALiYunUtil aLiYunUtil;

    /**
     * 验证码 5 分钟过期
     */
    private static final Long EXPIRE_TIME = 5L;


    public boolean sendVerifyCode2Phone(String phone) {
        try {
            String verifyCode = generateVerifyCode(phone);
            aLiYunUtil.sendVerifyCode(phone, verifyCode);
            return true;
        } catch (Exception e) {
            log.error("验证码发送失败, 手机号: {}, 错误信息: {}", phone, e.getMessage());
            throw new RuntimeException(e);
        }
    }


    /**
     * 生成验证码并放入redis中, 5分钟过期
     */
    private String generateVerifyCode(String phone) {
        Random random = new Random();
        long code = phone.hashCode() ^ (random.nextInt(900000) + 100000);
        long nowTime = System.currentTimeMillis();
        code = (code ^ nowTime) % 1000000;
        code = code < 0 ? -code : code;
        String verifyCode = String.format("%06d", code);

        String key = RedisKeys.VERIFY_CODE_PHONE + phone;
        redisUtil.addWithExpireTime(key, verifyCode, EXPIRE_TIME, TimeUnit.MINUTES);
        log.info("{} 的验证码是: {}", phone, verifyCode);
        return verifyCode;
    }

    public boolean checkPhoneVerifyCode(String phone, String verifyCode) {
        String key = RedisKeys.VERIFY_CODE_PHONE + phone;
        String code = redisUtil.get(key);

        if (!StringUtils.equals(code, verifyCode)) {
            return false;
        }
        redisUtil.delete(key);
        return true;
    }
}
