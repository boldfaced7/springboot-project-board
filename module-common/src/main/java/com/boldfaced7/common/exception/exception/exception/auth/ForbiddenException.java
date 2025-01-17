package com.boldfaced7.common.exception.exception.exception.auth;

import com.boldfaced7.common.exception.exception.ErrorCode;
import com.boldfaced7.common.exception.exception.exception.BusinessBaseException;

public class ForbiddenException extends BusinessBaseException {
    public ForbiddenException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ForbiddenException() {
        super(ErrorCode.FORBIDDEN);
    }
}
