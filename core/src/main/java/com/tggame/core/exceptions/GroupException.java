/*
* tg学习代码
 */
package com.tggame.core.exceptions;

import java.io.Serializable;

public class GroupException extends BaseException implements Serializable {
    public GroupException(BaseException.BaseExceptionEnum exceptionMessage) {
        super(exceptionMessage);
    }

    public GroupException(BaseException.BaseExceptionEnum exceptionMessage, Object... params) {
        super(exceptionMessage, params);
    }

    public GroupException(String message) {
        super(message);
    }

    public GroupException(String message, Throwable cause) {
        super(message, cause);
    }
}
