package com.boldfaced7.common.exception.exception.exception.entity;

import com.boldfaced7.common.exception.exception.ErrorCode;
import com.boldfaced7.common.exception.exception.exception.BusinessBaseException;

public class NotFoundException extends BusinessBaseException {

    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public NotFoundException() {
        super(ErrorCode.NOT_FOUND);
    }
}
