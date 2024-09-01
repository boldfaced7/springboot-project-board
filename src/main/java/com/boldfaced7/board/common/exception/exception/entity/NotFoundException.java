package com.boldfaced7.board.common.exception.exception.entity;

import com.boldfaced7.board.common.exception.ErrorCode;
import com.boldfaced7.board.common.exception.exception.BusinessBaseException;

public class NotFoundException extends BusinessBaseException {

    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public NotFoundException() {
        super(ErrorCode.NOT_FOUND);
    }
}
