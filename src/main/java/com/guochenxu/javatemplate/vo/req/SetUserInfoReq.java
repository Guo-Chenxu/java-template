package com.guochenxu.javatemplate.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 获取用户信息返回值
 *
 * @author: guoch
 * @create: 2024-09-27 00:59
 * @version: 1.0
 */

@ApiModel("设置用户信息请求值")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SetUserInfoReq implements Serializable {

    private static final long serialVersionUID = 2341089239L;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("个人简介")
    private String personalStatement;
}
