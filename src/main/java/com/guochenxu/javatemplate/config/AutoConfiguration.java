package com.guochenxu.javatemplate.config;

import com.guochenxu.javatemplate.utils.ALiYunUtil;
import com.guochenxu.javatemplate.utils.RedissonLockUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class AutoConfiguration {

    @Bean
    @ConditionalOnBean(ALiYunConfig.class)
    public ALiYunUtil aliYunUtil(ALiYunConfig aLiYunConfig) {
        return new ALiYunUtil(aLiYunConfig);
    }

    @Bean
    @ConditionalOnBean(RedissonConfig.class)
    public RedissonLockUtil redissonLockUtil(RedissonConfig redissonConfig) {
        return new RedissonLockUtil(redissonConfig);
    }

}