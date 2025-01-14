package com.guochenxu.javatemplate.vo.resp;

import com.guochenxu.javatemplate.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * 获取用户信息返回值
 *
 * @author: guoch
 * @create: 2024-09-27 00:59
 * @version: 1.0
 */

@ApiModel("获取用户信息返回值")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetUserInfoResp implements Serializable {

    private static final long serialVersionUID = 428102184L;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("个人简介")
    private String personalStatement;

    public static GetUserInfoResp from(User u) {
        GetUserInfoResp resp = new GetUserInfoResp();
        BeanUtils.copyProperties(u, resp);
        resp.setUserId(String.valueOf(u.getId()));
        return resp;
    }
}
