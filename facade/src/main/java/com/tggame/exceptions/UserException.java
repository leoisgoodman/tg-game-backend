/*
 * tg学习代码
 */
package com.tggame.exceptions;

import java.io.Serializable;

public class UserException extends BaseException implements Serializable {
    public UserException(BaseException.BaseExceptionEnum exceptionMessage) {
        super(exceptionMessage);
    }

    public UserException(BaseException.BaseExceptionEnum exceptionMessage, Object... params) {
        super(exceptionMessage, params);
    }

    public UserException(String message) {
        super(message);
    }

    public UserException(String message, Throwable cause) {
        super(message, cause);
    }
}
