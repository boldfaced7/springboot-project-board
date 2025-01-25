package com.boldfaced7.exception.auth;

import com.boldfaced7.exception.ErrorCode;
import com.boldfaced7.exception.BusinessBaseException;

public class UnauthorizedException extends BusinessBaseException {
    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UnauthorizedException() {
        super(ErrorCode.UNAUTHORIZED);
    }
}
