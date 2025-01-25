package com.boldfaced7.exception.entity;

import com.boldfaced7.exception.ErrorCode;
import com.boldfaced7.exception.BusinessBaseException;

public class NotFoundException extends BusinessBaseException {

    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public NotFoundException() {
        super(ErrorCode.NOT_FOUND);
    }
}
