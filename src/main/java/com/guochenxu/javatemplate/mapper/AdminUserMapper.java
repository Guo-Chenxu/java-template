package com.guochenxu.javatemplate.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guochenxu.javatemplate.entity.AdminUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * AdminUser 表数据库访问层
 *
 * @author: guochenxu
 * @create: 2024-10-30 21:25
 * @version: 1.0
 */
@Mapper
public interface AdminUserMapper extends BaseMapper<AdminUser> {
}
