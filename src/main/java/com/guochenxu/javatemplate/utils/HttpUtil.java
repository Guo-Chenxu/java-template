package com.guochenxu.javatemplate.utils;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * http工具类
 *
 * @author: guoch
 * @create: 2024-08-30 22:51
 * @version: 1.0
 */
@Slf4j
public class HttpUtil {

    /**
     * okHttp 单例
     */
    private static final OkHttpClient client = new OkHttpClient().newBuilder()
            .connectTimeout(10, TimeUnit.MINUTES)
            .readTimeout(10, TimeUnit.MINUTES)
            .writeTimeout(10, TimeUnit.MINUTES)
            .build();

    public static String doGet(String url, Map<String, String> params, Map<String, String> headers) {
        boolean first = true;
        if (!CollectionUtils.isEmpty(params)) {
            StringBuilder sb = new StringBuilder("?");
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (first) {
                    sb.append(entry.getKey()).append("=").append(entry.getValue());
                    first = false;
                } else {
                    sb.append("&").append(entry.getKey()).append("=").append(entry.getValue());
                }
            }
            url += sb.toString();
        }
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .addHeader("Accept", "*/*")
                .addHeader("Connection", "keep-alive");
        if (!CollectionUtils.isEmpty(headers)) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        Request request = requestBuilder.build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            log.error("http get error url: {}, exception: ", url, e);
            throw new RuntimeException(e);
        }
    }

    public static String doPost(String url, String body, Map<String, String> headers) {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody bodyContent = RequestBody.create(body, mediaType);
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .method("POST", bodyContent)
                .addHeader("Accept", "*/*")
                .addHeader("Content-Type", "application/json")
                .addHeader("Connection", "keep-alive");
        if (!CollectionUtils.isEmpty(headers)) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        Request request = requestBuilder.build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            log.error("http post error url: {}, body: {}, exception: ", url, body, e);
            throw new RuntimeException(e);
        }
    }
}
