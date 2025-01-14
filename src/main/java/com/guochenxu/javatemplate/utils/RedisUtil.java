package com.guochenxu.javatemplate.utils;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 缓存服务实现类
 *
 * @author: 郭晨旭
 * @create: 2023-11-09 13:47
 * @version: 1.0
 */

@Component
@Slf4j
public class RedisUtil {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 用于设置随机过期时间
     */
    private static final Random RANDOM = new Random();

    public void add(String key, String val) {
        stringRedisTemplate.opsForValue().set(key, val);
    }

    public void addWithExpireTime(String key, String val, long expireTime, TimeUnit unit) {
        stringRedisTemplate.opsForValue().set(key, val, expireTime, unit);
    }

    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public Object get(String key, Class<?> clazz) {
        String json = stringRedisTemplate.opsForValue().get(key);
        if (StringUtils.isBlank(json)) {
            return null;
        }
        return JSON.parseObject(json, clazz);
    }

    public Map<String, String> getByPrefix(String prefix) {
        Map<String, String> result = new HashMap<>();
        ScanOptions options = ScanOptions.scanOptions().match(prefix + "*").build();
        try (Cursor<String> cursor = stringRedisTemplate.scan(options)) {
            while (cursor.hasNext()) {
                String key = cursor.next();
                String value = get(key);
                result.put(key, value);
            }
        }
        return result;
    }

    public boolean exist(String key) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key));
    }

    public boolean delete(String key) {
        return Boolean.TRUE.equals(stringRedisTemplate.delete(key));
    }

    public void renew(String key, long expireTime, TimeUnit unit) {
        String v = this.get(key);
        addWithExpireTime(key, v, expireTime, unit);
    }
}
