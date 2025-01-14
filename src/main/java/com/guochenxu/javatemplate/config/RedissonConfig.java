package com.guochenxu.javatemplate.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * redisson配置
 *
 * @author: guoch
 * @create: 2025-01-13 18:09
 * @version: 1.0
 */

@Data
@ConfigurationProperties(prefix = "redisson")
public class RedissonConfig {

    private String addr;

    private String password;

    private Integer db;

    private Integer connectPoolSize;

    private Integer connectionMinimumIdleSize;
}
