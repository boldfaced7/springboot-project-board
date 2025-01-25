package com.boldfaced7.exception.auth;

import com.boldfaced7.exception.ErrorCode;
import com.boldfaced7.exception.BusinessBaseException;

public class ForbiddenException extends BusinessBaseException {
    public ForbiddenException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ForbiddenException() {
        super(ErrorCode.FORBIDDEN);
    }
}
