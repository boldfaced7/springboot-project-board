package com.boldfaced7.board.error.exception.auth;

import com.boldfaced7.board.error.ErrorCode;
import com.boldfaced7.board.error.exception.BusinessBaseException;

public class InvalidRefreshTokenException extends BusinessBaseException {
    public InvalidRefreshTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
    public InvalidRefreshTokenException() {super(ErrorCode.INVALID_REFRESH_TOKEN);}
}
