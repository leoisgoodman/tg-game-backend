/*
 * tg学习代码
 */
package com.tggame.exceptions;

import com.tggame.core.base.BaseException;

import java.io.Serializable;

public class WorkerNodeException extends BaseException implements Serializable {
    public WorkerNodeException(BaseException.BaseExceptionEnum exceptionMessage) {
        super(exceptionMessage);
    }

    public WorkerNodeException(BaseException.BaseExceptionEnum exceptionMessage, Object... params) {
        super(exceptionMessage, params);
    }

    public WorkerNodeException(String message) {
        super(message);
    }

    public WorkerNodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
