/*
* tg学习代码
 */
package com.tggame.core.exceptions;

import java.io.Serializable;

public class BetException extends BaseException implements Serializable {
    public BetException(BaseException.BaseExceptionEnum exceptionMessage) {
        super(exceptionMessage);
    }

    public BetException(BaseException.BaseExceptionEnum exceptionMessage, Object... params) {
        super(exceptionMessage, params);
    }

    public BetException(String message) {
        super(message);
    }

    public BetException(String message, Throwable cause) {
        super(message, cause);
    }
}
