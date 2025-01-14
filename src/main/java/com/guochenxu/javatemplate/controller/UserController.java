package com.guochenxu.javatemplate.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import com.guochenxu.javatemplate.annotation.CheckPermission;
import com.guochenxu.javatemplate.constants.PermissionConstants;
import com.guochenxu.javatemplate.service.UserService;
import com.guochenxu.javatemplate.utils.VerifyCodeUtil;
import com.guochenxu.javatemplate.vo.R;
import com.guochenxu.javatemplate.vo.req.LoginReq;
import com.guochenxu.javatemplate.vo.req.SetUserInfoReq;
import com.guochenxu.javatemplate.vo.resp.GetUserInfoResp;
import com.guochenxu.javatemplate.vo.resp.LoginResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;


/**
 * User 表控制层
 *
 * @author: guochenxu
 * @create: 2024-09-25 21:41
 * @version: 1.0
 */

@RestController
@RequestMapping("user")
@Slf4j
@Api(tags = "用户接口")
@Validated
public class UserController {
    /**
     * 服务对象
     */
    @Resource
    private UserService userService;

    @Resource
    private VerifyCodeUtil verifyCodeUtil;

    @SaIgnore
    @PostMapping("/verify_code/send")
    @ApiOperation("用户 发送验证码")
    public R sendVerifyCode(@RequestParam("phone") @NotBlank String phone) {
        return verifyCodeUtil.sendVerifyCode2Phone(phone)
                ? R.success() : R.error("发送验证码失败");
    }

    @SaIgnore
    @PostMapping("/verify_code/check")
    @ApiOperation("用户 校验验证码")
    public R checkVerifyCode(@RequestParam("phone") String phone, @RequestParam("code") String code) {
        return verifyCodeUtil.checkPhoneVerifyCode(phone, code)
                ? R.success() : R.error();
    }

    @SaCheckLogin
    @GetMapping("/verify_token")
    @ApiOperation("用户 校验token是否有效")
    public R verifyToken() {
        return R.success("token有效");
    }

    @SaIgnore
    @PostMapping("/login")
    @ApiOperation("用户 登录, 未注册则先注册再登录")
    public R<LoginResp> login(@RequestBody LoginReq req) {
        return R.success(userService.login(req));
    }

    @SaCheckLogin
    @GetMapping("/user_info")
    @ApiOperation("用户 获取用户信息")
    @CheckPermission(PermissionConstants.USER)
    public R<GetUserInfoResp> getUserInfo() {
        return R.success(userService.getUserInfo(StpUtil.getLoginIdAsLong()));
    }

    @SaCheckLogin
    @PostMapping("/user_info")
    @ApiOperation("用户 设置用户信息，对应字段为空则不变")
    @CheckPermission(PermissionConstants.USER)
    public R<GetUserInfoResp> setUserInfo(@RequestBody SetUserInfoReq req) {
        return R.success(userService.setUserInfo(req, StpUtil.getLoginIdAsLong()));
    }
}
