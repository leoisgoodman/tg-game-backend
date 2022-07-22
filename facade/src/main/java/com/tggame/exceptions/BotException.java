/*
 * tg学习代码
 */
package com.tggame.exceptions;

import java.io.Serializable;

public class BotException extends BaseException implements Serializable {
    public BotException(BaseException.BaseExceptionEnum exceptionMessage) {
        super(exceptionMessage);
    }

    public BotException(BaseException.BaseExceptionEnum exceptionMessage, Object... params) {
        super(exceptionMessage, params);
    }

    public BotException(String message) {
        super(message);
    }

    public BotException(String message, Throwable cause) {
        super(message, cause);
    }
}
