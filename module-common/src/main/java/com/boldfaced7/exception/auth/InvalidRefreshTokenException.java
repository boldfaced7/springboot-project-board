package com.boldfaced7.exception.auth;

import com.boldfaced7.exception.ErrorCode;
import com.boldfaced7.exception.BusinessBaseException;

public class InvalidRefreshTokenException extends BusinessBaseException {
    public InvalidRefreshTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
    public InvalidRefreshTokenException() {super(ErrorCode.INVALID_REFRESH_TOKEN);}
}
