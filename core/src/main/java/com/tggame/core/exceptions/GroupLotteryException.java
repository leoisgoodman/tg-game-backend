/*
* tg学习代码
 */
package com.tggame.core.exceptions;

import java.io.Serializable;

public class GroupLotteryException extends BaseException implements Serializable {
    public GroupLotteryException(BaseException.BaseExceptionEnum exceptionMessage) {
        super(exceptionMessage);
    }

    public GroupLotteryException(BaseException.BaseExceptionEnum exceptionMessage, Object... params) {
        super(exceptionMessage, params);
    }

    public GroupLotteryException(String message) {
        super(message);
    }

    public GroupLotteryException(String message, Throwable cause) {
        super(message, cause);
    }
}
