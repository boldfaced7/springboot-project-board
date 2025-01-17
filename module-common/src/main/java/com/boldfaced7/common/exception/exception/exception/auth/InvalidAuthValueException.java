package com.boldfaced7.common.exception.exception.exception.auth;

import com.boldfaced7.common.exception.exception.ErrorCode;
import com.boldfaced7.common.exception.exception.exception.BusinessBaseException;

public class InvalidAuthValueException extends BusinessBaseException {
    public InvalidAuthValueException(ErrorCode errorCode) {
        super(errorCode);
    }

    public InvalidAuthValueException() {
        super(ErrorCode.INVALID_AUTH_VALUE);
    }
}
