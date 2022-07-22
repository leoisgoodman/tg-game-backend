/*
 * tg学习代码
 */
package com.tggame.exceptions;

import com.tggame.core.base.BaseException;

import java.io.Serializable;

public class LotteryException extends BaseException implements Serializable {
    public LotteryException(BaseException.BaseExceptionEnum exceptionMessage) {
        super(exceptionMessage);
    }

    public LotteryException(BaseException.BaseExceptionEnum exceptionMessage, Object... params) {
        super(exceptionMessage, params);
    }

    public LotteryException(String message) {
        super(message);
    }

    public LotteryException(String message, Throwable cause) {
        super(message, cause);
    }
}
