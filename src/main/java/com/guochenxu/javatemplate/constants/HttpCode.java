package com.guochenxu.javatemplate.constants;

/**
 * 自定义http状态码
 *
 * @author: 郭晨旭
 * @create: 2023-10-30 09:41
 * @version: 1.0
 */
public interface HttpCode {

    /**
     * 执行成功
     */
    Integer SUCCESS = 200;

    /**
     * 禁止访问
     */
    Integer FORBIDDEN = 403;

    /**
     * 业务异常
     */
    Integer FAIL = 500;

    /**
     * 用户非法操作
     */
    Integer ILLEGAL_OPERATION = 1003; // 用户非法操作
}
