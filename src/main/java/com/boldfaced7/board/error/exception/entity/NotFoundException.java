package com.boldfaced7.board.error.exception.entity;

import com.boldfaced7.board.error.ErrorCode;
import com.boldfaced7.board.error.exception.BusinessBaseException;

public class NotFoundException extends BusinessBaseException {

    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public NotFoundException() {
        super(ErrorCode.NOT_FOUND);
    }
}
