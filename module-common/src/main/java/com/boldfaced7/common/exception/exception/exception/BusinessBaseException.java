package com.boldfaced7.common.exception.exception.exception;

import com.boldfaced7.common.exception.exception.ErrorCode;

public class BusinessBaseException extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessBaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
