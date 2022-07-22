/*
 * tg学习代码
 */
package com.tggame.exceptions;

import java.io.Serializable;

public class TgUserFlowException extends BaseException implements Serializable {
    public TgUserFlowException(BaseException.BaseExceptionEnum exceptionMessage) {
        super(exceptionMessage);
    }

    public TgUserFlowException(BaseException.BaseExceptionEnum exceptionMessage, Object... params) {
        super(exceptionMessage, params);
    }

    public TgUserFlowException(String message) {
        super(message);
    }

    public TgUserFlowException(String message, Throwable cause) {
        super(message, cause);
    }
}
