package com.boldfaced7.common.exception.exception.exception.auth;

import com.boldfaced7.common.exception.exception.ErrorCode;
import com.boldfaced7.common.exception.exception.exception.BusinessBaseException;

public class InvalidRefreshTokenException extends BusinessBaseException {
    public InvalidRefreshTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
    public InvalidRefreshTokenException() {super(ErrorCode.INVALID_REFRESH_TOKEN);}
}
