/*
 * tg学习代码
 */
package com.tggame.open.entity;

import com.tggame.bet.entity.BetCode;

/**
 * 开奖结果
 *
 * @author tg
 */
public enum OpenEnum implements java.io.Serializable {
    Instance;

    /**
     * 开奖号码
     * 计算位数 如：123.4 -> 1+2+3+4=10 则位数为 0
     *
     * @param num btcusdt 价格数字
     * @return 开奖结果 0 开奖号码，1 大小结果，2 单双结果
     */
    public String[] drawn(String num) {
        String[] arr = num.replace(".", "").split("");
        int sum = 0;
        for (String s : arr) {
            sum += Integer.parseInt(s);
        }
        String lastNum = String.valueOf(sum);
        lastNum = lastNum.split("")[lastNum.length() - 1];

        String bigSmall = Integer.parseInt(lastNum) >= 5 ? BetCode.Big.val : BetCode.Small.val;
        String oddEven = Integer.parseInt(lastNum) % 2 == 0 ? BetCode.Even.val : BetCode.Odd.val;

        return new String[]{lastNum, bigSmall, oddEven};
    }
}
