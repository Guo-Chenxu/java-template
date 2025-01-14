package com.guochenxu.javatemplate.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 登陆请求返回接口
 *
 * @author: guochenxu
 * @create: 2024-09-25 22:10
 * @version: 1.0
 */

@ApiModel("登陆请求返回接口")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResp implements Serializable {
    private static final long serialVersionUID = 742193L;

    @ApiModelProperty("token")
    private String token;

    @ApiModelProperty("用户状态 true=新用户，false=老用户")
    private Boolean status;

    @ApiModelProperty("用户是否被禁用 true=禁用，false=未禁用")
    private Boolean blocked;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("过期时间 毫秒时间戳")
    private String expireTime;
}
