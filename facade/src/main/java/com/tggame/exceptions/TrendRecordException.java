/*
 * tg学习代码
 */
package com.tggame.exceptions;

import java.io.Serializable;

public class TrendRecordException extends BaseException implements Serializable {
    public TrendRecordException(BaseException.BaseExceptionEnum exceptionMessage) {
        super(exceptionMessage);
    }

    public TrendRecordException(BaseException.BaseExceptionEnum exceptionMessage, Object... params) {
        super(exceptionMessage, params);
    }

    public TrendRecordException(String message) {
        super(message);
    }

    public TrendRecordException(String message, Throwable cause) {
        super(message, cause);
    }
}
