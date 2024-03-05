package com.boldfaced7.board.error.exception.auth;

import com.boldfaced7.board.error.ErrorCode;
import com.boldfaced7.board.error.exception.BusinessBaseException;

public class UnauthorizedException extends BusinessBaseException {
    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UnauthorizedException() {
        super(ErrorCode.UNAUTHORIZED);
    }
}
