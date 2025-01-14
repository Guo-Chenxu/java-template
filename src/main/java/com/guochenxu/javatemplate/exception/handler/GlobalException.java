package com.guochenxu.javatemplate.exception.handler;

import com.guochenxu.javatemplate.constants.HttpCode;
import com.guochenxu.javatemplate.exception.IllegalException;
import com.guochenxu.javatemplate.exception.NotFoundException;
import com.guochenxu.javatemplate.vo.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * sa-token异常处理
 */
@RestControllerAdvice
@Slf4j
public class GlobalException {


    // 拦截：指定参数不存在异常
    @ExceptionHandler(NotFoundException.class)
    public R handlerException(NotFoundException e) {
        log.error("指定参数不存在：", e);
        return R.error(HttpCode.ILLEGAL_OPERATION, e.getMessage());
    }

    // 拦截：用户非法操作
    @ExceptionHandler(IllegalException.class)
    public R handlerException(IllegalException e) {
        log.error("用户非法操作：", e);
        return R.error(HttpCode.ILLEGAL_OPERATION, e.getMessage());
    }

    // 拦截：空指针异常
    @ExceptionHandler(NullPointerException.class)
    public R handlerException(NullPointerException e) {
        log.error("出现空指针异常: ", e);
        return R.error(e.getMessage());
    }

    // 拦截：其它所有异常
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public R handlerException(MissingServletRequestParameterException e) {
        log.error("缺少参数异常: ", e);
        return new R<>(HttpCode.ILLEGAL_OPERATION, e.getMessage(), null);
    }

    // 拦截：其它所有异常
    @ExceptionHandler(Exception.class)
    public R handlerException(Exception e) {
        log.error("出现异常: ", e);
        if (e instanceof IllegalException) {
            return R.error(HttpCode.ILLEGAL_OPERATION, e.getMessage());
        }
        return R.error(e.getMessage());
    }
}