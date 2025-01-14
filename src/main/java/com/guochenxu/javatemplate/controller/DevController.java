package com.guochenxu.javatemplate.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guochenxu.javatemplate.entity.User;
import com.guochenxu.javatemplate.service.UserService;
import com.guochenxu.javatemplate.utils.CommonUtil;
import com.guochenxu.javatemplate.utils.IPUtil;
import com.guochenxu.javatemplate.vo.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("dev")
@Api(tags = "开发测试接口")
public class DevController {

    @Resource
    private UserService userService;

    @SaIgnore
    @GetMapping("/ping")
    @ApiOperation("ping")
    public R ping(HttpServletRequest request) {
        Map<String, String> data = new HashMap<>();
        data.put("swagger", "");
        data.put("ip", IPUtil.getIpAddr(request));
        return R.success("pong", data);
    }

    @SaIgnore
    @GetMapping("/token/{phone}")
    @ApiOperation("get token")
    public R<String> getToken(@PathVariable("phone") String phone) {
        phone = CommonUtil.formatPhone(phone);
        User u = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getPhone, phone));
        StpUtil.login(u.getId());
        return R.success("success", StpUtil.getTokenValue());
    }

    @SaIgnore
    @GetMapping("/token")
    @ApiOperation("获取test用户token")
    public R<String> getToken() {
        User u = userService.getById(1L);
        if (u == null) {
            return R.error("用户不存在");
        }
        StpUtil.login(u.getId());
        return R.success("success", StpUtil.getTokenValue());
    }


    @SaCheckLogin
    @DeleteMapping("/del")
    @ApiOperation("用户 测试用,删除用户")
    public R delUser() {
        return userService.absoluteDelUser(StpUtil.getLoginIdAsLong())
                ? R.success()
                : R.error("删除失败，用户不存在");
    }
}
