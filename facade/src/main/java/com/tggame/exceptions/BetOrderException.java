/*
 * tg学习代码
 */
package com.tggame.exceptions;

import java.io.Serializable;

public class BetOrderException extends BaseException implements Serializable {
    public BetOrderException(BaseException.BaseExceptionEnum exceptionMessage) {
        super(exceptionMessage);
    }

    public BetOrderException(BaseException.BaseExceptionEnum exceptionMessage, Object... params) {
        super(exceptionMessage, params);
    }

    public BetOrderException(String message) {
        super(message);
    }

    public BetOrderException(String message, Throwable cause) {
        super(message, cause);
    }
}
