package com.boldfaced7.board.common.exception.exception.auth;

import com.boldfaced7.board.common.exception.ErrorCode;
import com.boldfaced7.board.common.exception.exception.BusinessBaseException;

public class InvalidRefreshTokenException extends BusinessBaseException {
    public InvalidRefreshTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
    public InvalidRefreshTokenException() {super(ErrorCode.INVALID_REFRESH_TOKEN);}
}
