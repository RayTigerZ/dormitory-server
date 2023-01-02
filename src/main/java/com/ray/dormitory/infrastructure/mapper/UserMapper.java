package com.ray.dormitory.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ray.dormitory.infrastructure.entity.User;
import org.springframework.stereotype.Repository;

/**
 * @author Ray
 * @date : 2019.11.21 13:21
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

}
