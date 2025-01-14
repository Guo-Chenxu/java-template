package com.guochenxu.javatemplate;

import com.guochenxu.javatemplate.config.ALiYunConfig;
import com.guochenxu.javatemplate.config.RedissonConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableMongoAuditing
@EnableConfigurationProperties(value = {ALiYunConfig.class, RedissonConfig.class})
@EnableAsync
@EnableScheduling
@Slf4j
@EnableSwagger2
public class JavaTemplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaTemplateApplication.class, args);
    }

}
