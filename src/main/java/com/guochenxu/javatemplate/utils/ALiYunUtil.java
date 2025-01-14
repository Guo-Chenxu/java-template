// This file is auto-generated, don't edit it. Thanks.
package com.guochenxu.javatemplate.utils;

import com.alibaba.fastjson.JSON;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.comm.SignVersion;
import com.aliyun.teaopenapi.models.Config;
import com.guochenxu.javatemplate.annotation.ExecutionTime;
import com.guochenxu.javatemplate.config.ALiYunConfig;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@ExecutionTime
public class ALiYunUtil {

    //    @Resource
    private final ALiYunConfig aLiYunConfig;

    private static Client smsClient;

    private static OSS ossClient;

    public ALiYunUtil(ALiYunConfig config) {
        this.aLiYunConfig = config;
    }

    /**
     * description
     * 使用AK SK初始化账号Client
     * <a href="https://help.aliyun.com/zh/sms/developer-reference/using-the-openapi-example">文档</a>
     */
    @PostConstruct
    @SneakyThrows
    private void createSMSClient() throws Exception {
        Config config = new Config()
                .setAccessKeyId(aLiYunConfig.getSms().getAccessKeyId())
                .setAccessKeySecret(aLiYunConfig.getSms().getAccessKeySecret());
        config.endpoint = aLiYunConfig.getSms().getEndpoint();
        smsClient = new Client(config);
    }

    /**
     * oss client
     * <a href="https://help.aliyun.com/zh/oss/developer-reference/initialization-3">文档</a>
     */
    @PostConstruct
    @SneakyThrows
    private void createOSSClient() {
//        log.info("初始化oss client: {}", aLiYunConfig);
        DefaultCredentialProvider provider = new DefaultCredentialProvider(aLiYunConfig.getOss().getAccessKeyId(), aLiYunConfig.getOss().getAccessKeySecret());
        String endpoint = aLiYunConfig.getOss().getEndpoint();
        String region = aLiYunConfig.getOss().getRegion();

        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);
        ossClient = OSSClientBuilder.create()
                .endpoint(endpoint)
                .credentialsProvider(provider)
                .clientConfiguration(clientBuilderConfiguration)
                .region(region)
                .build();
    }

    public void sendVerifyCode(String phoneNumber, String verifyCode) {
        Map<String, String> map = new HashMap<>();
        map.put("code", verifyCode);
        sendSms(phoneNumber, aLiYunConfig.getSms().getVerifyCodeConfig(), map);
    }

    private void sendSms(String phoneNumber, ALiYunConfig.SMSConfig.TemplateConfig templateConfig, Map<String, String> map) {
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setPhoneNumbers(phoneNumber)
                .setSignName(templateConfig.getSignName())
                .setTemplateCode(templateConfig.getTemplateCode())
                .setTemplateParam(JSON.toJSONString(map));
        try {
            SendSmsResponse sendSmsResponse = smsClient.sendSms(sendSmsRequest);
            log.info("短信发送结果:{}", SendSmsResponse.toMap(sendSmsResponse));
        } catch (Exception e) {
            log.error("短信发送失败:{}", e.getMessage());
        }
    }

    public String uploadFile(String filename, byte[] file) {
        ossClient.putObject(aLiYunConfig.getOss().getBucketName(), filename, new ByteArrayInputStream(file));
        return aLiYunConfig.getOss().getBaseUrl() + filename;
    }

    public String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1);
        }
        return "";
    }

    private String trim(String s) {
        return StringUtils.trim(s).replaceAll(" ", "-");
    }
}

