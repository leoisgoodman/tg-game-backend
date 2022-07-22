package com.tggame.bot.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tggame.bot.entity.Bot;
import org.springframework.stereotype.Repository;

/**
 * Bot mapper
 * 直接与xml映射
 *
 * @author tg
 */
@Repository
public interface BotMapper extends BaseMapper<Bot> {

}
