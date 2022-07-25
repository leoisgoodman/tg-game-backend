/*
 * tg学习代码
 */
package com.tggame.bet.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tggame.bet.entity.BetOrder;

/**
 * 投注
 *
 * @author tg
 */
public interface BetOrderService extends IService<BetOrder> {

    /**
     * 投注
     *  @param betOrder
     *
     */
    void bet(BetOrder betOrder);
}


