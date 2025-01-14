package com.guochenxu.javatemplate.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guochenxu.javatemplate.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * User 表数据库访问层
 *
 * @author: guochenxu
 * @create: 2024-09-25 21:41
 * @version: 1.0
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Delete("delete from user where id = #{userId}")
    int absoluteDeleteUserById(@Param("userId") Long userId);
}
