/*
 * tg学习代码
 */
package com.tggame.exceptions;

import java.io.Serializable;

public class OpenRecordException extends BaseException implements Serializable {
    public OpenRecordException(BaseException.BaseExceptionEnum exceptionMessage) {
        super(exceptionMessage);
    }

    public OpenRecordException(BaseException.BaseExceptionEnum exceptionMessage, Object... params) {
        super(exceptionMessage, params);
    }

    public OpenRecordException(String message) {
        super(message);
    }

    public OpenRecordException(String message, Throwable cause) {
        super(message, cause);
    }
}
