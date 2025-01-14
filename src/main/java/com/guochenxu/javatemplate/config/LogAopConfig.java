package com.guochenxu.javatemplate.config;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Aspect
@Component
@Order(1)
@Slf4j
public class LogAopConfig {

    @Around("@within(org.springframework.web.bind.annotation.RestController)" +
            "||@within(org.springframework.stereotype.Controller)")
    public Object after(ProceedingJoinPoint joinPoint) throws Throwable {

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        if (request.getRequestURI().startsWith("/api/swagger-resources")
                || request.getRequestURI().startsWith("/api/v2/api-docs")) {
            return joinPoint.proceed(joinPoint.getArgs());
        }

        String traceId = IdUtil.getSnowflakeNextIdStr();
        MDC.put("traceId", traceId);
        request.setAttribute("traceId", traceId);
        String userId = "";
        try {
            userId = StpUtil.getLoginIdAsString();
        } catch (Exception e) {
            userId = "no-auth";
        }
        MDC.put("userId", userId);

        log.info("\n============================Request Come In============================\n" +
                        "Trace Id={}\n" +
                        "Time={}\n" +
                        "URL={}\n" +
                        "Request Method={}\n" +
                        "Signature={}\n" +
                        "Parameter={}\n",
                traceId,
                LocalDateTime.now(),
                Optional.of(request.getRequestURI()).orElse(null), request.getMethod(),
                joinPoint.getSignature(),
                JSONObject.toJSONString(filterArgs(joinPoint.getArgs())));

        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed(joinPoint.getArgs());
        long end = System.currentTimeMillis();

        log.info("\n============================Response Return============================\n" +
                        "Trace Id={}\n" +
                        "Time={}\n" +
                        "Consume Time={}ms\n" +
                        "Response={}\n",
                traceId, LocalDateTime.now(), end - start, JSONObject.toJSONString(result));
        HttpServletResponse response = requestAttributes.getResponse();
        response.setHeader("traceId", traceId);

        return result;
    }


    private List<Object> filterArgs(Object[] objects) {
        return Arrays.stream(objects).filter(obj -> !(obj instanceof MultipartFile)
                && !(obj instanceof HttpServletResponse)
                && !(obj instanceof HttpServletRequest)).collect(Collectors.toList());
    }
}
