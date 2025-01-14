package com.guochenxu.javatemplate.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 分页请求
 *
 * @author: guoch
 * @create: 2024-12-30 23:38
 * @version: 1.0
 */
@ApiModel("分页请求")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class BasePageReq implements Serializable {
    private static final long serialVersionUID = 183274093L;

    @ApiModelProperty("页码")
    private Integer pageNum;

    @ApiModelProperty("每页大小")
    private Integer pageSize;

    @ApiModelProperty(value = "关键字 可不填")
    private String keyword;

    public void verify() {
        if (this.pageNum == null || this.pageNum <= 0) {
            this.pageNum = 1;
        }
        if (this.pageSize == null || this.pageSize <= 0) {
            this.pageSize = 20;
        }
    }

    public BasePageReq(Integer pageNum, Integer pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public static BasePageReq of(Integer pageNum, Integer pageSize) {
        BasePageReq basePageReq = new BasePageReq(pageNum, pageSize);
        basePageReq.verify();
        return basePageReq;
    }
}
