package com.boldfaced7.common.exception.exception.exception.member;

import com.boldfaced7.common.exception.exception.ErrorCode;
import com.boldfaced7.common.exception.exception.exception.BusinessBaseException;

public class MemberEmailDuplicatedException extends BusinessBaseException {
    public MemberEmailDuplicatedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
