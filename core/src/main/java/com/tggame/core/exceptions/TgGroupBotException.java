/*
* tg学习代码
 */
package com.tggame.core.exceptions;

import java.io.Serializable;

public class TgGroupBotException extends BaseException implements Serializable {
    public TgGroupBotException(BaseException.BaseExceptionEnum exceptionMessage) {
        super(exceptionMessage);
    }

    public TgGroupBotException(BaseException.BaseExceptionEnum exceptionMessage, Object... params) {
        super(exceptionMessage, params);
    }

    public TgGroupBotException(String message) {
        super(message);
    }

    public TgGroupBotException(String message, Throwable cause) {
        super(message, cause);
    }
}
