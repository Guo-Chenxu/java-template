package com.guochenxu.javatemplate.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 登陆请求
 *
 * @author: guochenxu
 * @create: 2024-09-25 22:06
 * @version: 1.0
 */
@ApiModel("登陆请求")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class LoginReq implements Serializable {

    private static final long serialVersionUID = 482931L;

    @ApiModelProperty("换openid的code")
    private String openIdCode;

    @ApiModelProperty("换电话号的code")
    private String phoneCode;

    @ApiModelProperty("加密信息，和加密向量一起获取用户微信名/头像信息")
    private String encryptedData;

    @ApiModelProperty("加密向量")
    private String iv;
}
