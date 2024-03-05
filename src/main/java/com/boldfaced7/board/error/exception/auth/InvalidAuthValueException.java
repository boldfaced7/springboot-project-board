package com.boldfaced7.board.error.exception.auth;

import com.boldfaced7.board.error.ErrorCode;
import com.boldfaced7.board.error.exception.BusinessBaseException;

public class InvalidAuthValueException extends BusinessBaseException {
    public InvalidAuthValueException(ErrorCode errorCode) {
        super(errorCode);
    }

    public InvalidAuthValueException() {
        super(ErrorCode.INVALID_AUTH_VALUE);
    }
}
