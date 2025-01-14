package com.guochenxu.javatemplate.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Validator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guochenxu.javatemplate.constants.CacheKeys;
import com.guochenxu.javatemplate.entity.User;
import com.guochenxu.javatemplate.mapper.UserMapper;
import com.guochenxu.javatemplate.service.UserService;
import com.guochenxu.javatemplate.utils.RedisUtil;
import com.guochenxu.javatemplate.vo.req.LoginReq;
import com.guochenxu.javatemplate.vo.req.SetUserInfoReq;
import com.guochenxu.javatemplate.vo.resp.GetUserInfoResp;
import com.guochenxu.javatemplate.vo.resp.LoginResp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * User 表服务实现类
 *
 * @author: guochenxu
 * @create: 2024-09-25 21:41
 * @version: 1.0
 */

@Service("userService")
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private WxMaService wxMaService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisUtil redisUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginResp login(LoginReq req) {
        try {
            WxMaPhoneNumberInfo phoneNoInfo = wxMaService.getUserService().getPhoneNoInfo(req.getPhoneCode());
            String phone = phoneNoInfo.getPurePhoneNumber();
            WxMaJscode2SessionResult sessionInfo = wxMaService.getUserService().getSessionInfo(req.getOpenIdCode());
            String openId = sessionInfo.getOpenid();
            WxMaUserInfo wxMaUserInfo = wxMaService.getUserService().getUserInfo(sessionInfo.getSessionKey(),
                    req.getEncryptedData(), req.getIv());

            User user = this.getOne(new LambdaQueryWrapper<User>().eq(User::getOpenId, openId).select(User::getId));
            boolean status = false;
            if (user == null) {
                status = true;
                register(openId, phone, wxMaUserInfo);
            }

            user = this.getOne(new LambdaQueryWrapper<User>().eq(User::getOpenId, openId)
                    .select(User::getId, User::getBlocked));
            if (user.getBlocked()) {
                return LoginResp.builder().blocked(true).build();
            }
            user.setLastLoginTime(new Date());
            this.updateById(user);

            StpUtil.login(user.getId());
            String token = StpUtil.getTokenValue();
            long expireTime = System.currentTimeMillis() + StpUtil.getTokenTimeout() * 1000L;
            return LoginResp.builder().token(token).expireTime(String.valueOf(expireTime))
                    .status(status).userId(String.valueOf(user.getId()))
                    .blocked(false).build();
        } catch (Exception e) {
            log.error("调用微信接口失败", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public GetUserInfoResp getUserInfo(Long userId) {
        User u = this.getOne(new LambdaQueryWrapper<User>().eq(User::getId, userId));
        if (u == null) {
            throw new RuntimeException("用户不存在");
        }
        return GetUserInfoResp.from(u);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GetUserInfoResp setUserInfo(SetUserInfoReq req, Long userId) {
        User u = this.getOne(new LambdaQueryWrapper<User>().eq(User::getId, userId));
        if (u == null) {
            throw new RuntimeException("用户不存在");
        }

        if (StringUtils.isNotBlank(req.getAvatar())) {
            u.setAvatar(req.getAvatar());
        }
        if (StringUtils.isNotBlank(req.getNickname())) {
            u.setNickname(req.getNickname());
        }
        if (StringUtils.isNotBlank(req.getPhone())) {
            u.setPhone(req.getPhone());
        }
        if (StringUtils.isNotBlank(req.getEmail())) {
            if (!Validator.isEmail(req.getEmail())) {
                throw new RuntimeException("邮箱格式不正确");
            }
            u.setEmail(req.getEmail());
        }
        if (StringUtils.isNotBlank(req.getPersonalStatement())) {
            u.setPersonalStatement(req.getPersonalStatement());
        }
        if (!this.updateById(u)) {
            log.error("修改主页失败, userId:{}, req: {}", userId, req);
            throw new RuntimeException("修改主页失败");
        }
        return GetUserInfoResp.from(u);
    }

    @Override
    public boolean absoluteDelUser(Long userId) {
        int cnt = userMapper.absoluteDeleteUserById(userId);
        return cnt > 0;
    }

    @Override
    public boolean isUser(Long userId) {
        boolean pass = false;
        if (redisUtil.exist(CacheKeys.JAVA_TEMPLATE_USER + userId)) {
            pass = true;
        } else if (this.getById(userId) != null) {
            pass = true;
            redisUtil.addWithExpireTime(CacheKeys.JAVA_TEMPLATE_USER + userId, "", 3, TimeUnit.DAYS);
        }
        return pass;
    }
    private String maskPhone(String phone) {
        String start = phone.substring(0, 6);
        String end = phone.substring(10);
        String middle = "****";
        return start + middle + end;
    }

    private Long register(String openId, String phone, WxMaUserInfo wxMaUserInfo) {
        User user = User.builder().openId(openId)
                .phone(phone).blocked(false)
                .nickname(wxMaUserInfo.getNickName())
                .avatar(wxMaUserInfo.getAvatarUrl())
                .lastLoginTime(new Date())
                .build();
        this.save(user);
        return user.getId();
    }
}
