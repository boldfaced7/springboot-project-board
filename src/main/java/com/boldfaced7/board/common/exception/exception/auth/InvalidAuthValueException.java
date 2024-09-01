package com.boldfaced7.board.common.exception.exception.auth;

import com.boldfaced7.board.common.exception.ErrorCode;
import com.boldfaced7.board.common.exception.exception.BusinessBaseException;

public class InvalidAuthValueException extends BusinessBaseException {
    public InvalidAuthValueException(ErrorCode errorCode) {
        super(errorCode);
    }

    public InvalidAuthValueException() {
        super(ErrorCode.INVALID_AUTH_VALUE);
    }
}
