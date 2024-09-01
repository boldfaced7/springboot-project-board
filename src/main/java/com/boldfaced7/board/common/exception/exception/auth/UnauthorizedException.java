package com.boldfaced7.board.common.exception.exception.auth;

import com.boldfaced7.board.common.exception.ErrorCode;
import com.boldfaced7.board.common.exception.exception.BusinessBaseException;

public class UnauthorizedException extends BusinessBaseException {
    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UnauthorizedException() {
        super(ErrorCode.UNAUTHORIZED);
    }
}
