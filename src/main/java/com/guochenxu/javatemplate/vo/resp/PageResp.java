package com.guochenxu.javatemplate.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页查询返回
 *
 * @author: guoch
 * @create: 2024-11-08 19:54
 * @version: 1.0
 */

@ApiModel("分页查询返回")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class PageResp<T> implements Serializable {

    private static final long serialVersionUID = 8312470L;

    @ApiModelProperty("总数量")
    Integer total;

    @ApiModelProperty("数据")
    List<T> data;

    public static <T> PageResp<T> of(Long total, List<T> data) {
        return new PageResp<T>().setTotal(Math.toIntExact(total)).setData(data);
    }

    public static <T> PageResp<T> of(Integer pageNum, Integer pageSize, List<T> records) {
        Long total = (long) records.size();
        int l = (pageNum - 1) * pageSize, r = pageNum * pageSize;
        if (l >= records.size()) {
            return PageResp.of(total, new ArrayList<>());
        }
        r = Math.min(r, records.size());
        records = records.subList(l, r);
        return PageResp.of(total, records);
    }

    public static <T> PageResp<T> empty() {
        return PageResp.of(0L, new ArrayList<>());
    }
}
