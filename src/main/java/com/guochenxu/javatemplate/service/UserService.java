package com.guochenxu.javatemplate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guochenxu.javatemplate.entity.User;
import com.guochenxu.javatemplate.vo.req.LoginReq;
import com.guochenxu.javatemplate.vo.req.SetUserInfoReq;
import com.guochenxu.javatemplate.vo.resp.GetUserInfoResp;
import com.guochenxu.javatemplate.vo.resp.LoginResp;

/**
 * (User)表服务接口
 *
 * @author makejava
 * @since 2024-09-25 21:41:59
 */

/**
 * User 表服务实现接口
 *
 * @author: guochenxu
 * @create: 2024-09-25 21:41
 * @version: 1.0
 */

public interface UserService extends IService<User> {

    /**
     * 登录，未注册先注册
     */
    LoginResp login(LoginReq req);

    /**
     * 获取用户信息
     */
    GetUserInfoResp getUserInfo(Long userId);

    /**
     * 设置用户信息
     */
    GetUserInfoResp setUserInfo(SetUserInfoReq req, Long userId);

    /**
     * 绝对删除
     */
    boolean absoluteDelUser(Long userId);

    /**
     * 判断时候是用户
     */
    boolean isUser(Long userId);
}
