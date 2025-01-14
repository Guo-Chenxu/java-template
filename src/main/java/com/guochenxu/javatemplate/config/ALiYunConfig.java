package com.guochenxu.javatemplate.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 阿里云短信配置
 *
 * @author: guoch
 * @create: 2024-09-20 00:21
 * @version: 1.0
 */
@Data
@ConfigurationProperties(prefix = "aliyun")
public class ALiYunConfig {

    private SMSConfig sms;

    private OSSConfig oss;

    @Data
    public static class SMSConfig {

        private String accessKeyId;

        private String accessKeySecret;

        private String endpoint;

        /**
         * 短信验证码配置
         */
        private TemplateConfig verifyCodeConfig;

        @Data
        public static class TemplateConfig {

            private String signName;

            private String templateCode;
        }
    }

    @Data
    public static class OSSConfig {

        private String accessKeyId;

        private String accessKeySecret;

        private String endpoint;

        private String region;

        private String bucketName;

        private String baseUrl;
//        private BucketConfig avatar;
//
//        @Data
//        public static class BucketConfig {
//
//            private String bucketName;
//
//            private String baseUrl;
//        }
    }
}

