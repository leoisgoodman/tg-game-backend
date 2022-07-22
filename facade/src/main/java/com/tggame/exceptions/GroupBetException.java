/*
 * tg学习代码
 */
package com.tggame.exceptions;

import java.io.Serializable;

public class GroupBetException extends BaseException implements Serializable {
    public GroupBetException(BaseException.BaseExceptionEnum exceptionMessage) {
        super(exceptionMessage);
    }

    public GroupBetException(BaseException.BaseExceptionEnum exceptionMessage, Object... params) {
        super(exceptionMessage, params);
    }

    public GroupBetException(String message) {
        super(message);
    }

    public GroupBetException(String message, Throwable cause) {
        super(message, cause);
    }
}
