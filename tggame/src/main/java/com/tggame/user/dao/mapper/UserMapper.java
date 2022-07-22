package com.tggame.user.dao.mapper;

import com.tggame.user.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * User mapper
 * 直接与xml映射
 *
 * @author tg
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

}
