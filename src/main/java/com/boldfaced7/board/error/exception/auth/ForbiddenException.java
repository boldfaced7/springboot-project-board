package com.boldfaced7.board.error.exception.auth;

import com.boldfaced7.board.error.ErrorCode;
import com.boldfaced7.board.error.exception.BusinessBaseException;

public class ForbiddenException extends BusinessBaseException {
    public ForbiddenException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ForbiddenException() {
        super(ErrorCode.FORBIDDEN);
    }
}
