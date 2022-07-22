package com.tggame.lottery.dao.mapper;

import com.tggame.lottery.entity.Lottery;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * Lottery mapper
 * 直接与xml映射
 *
 * @author tg
 */
@Repository
public interface LotteryMapper extends BaseMapper<Lottery> {

}
