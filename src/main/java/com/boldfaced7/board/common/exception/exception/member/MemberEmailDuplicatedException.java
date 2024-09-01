package com.boldfaced7.board.common.exception.exception.member;

import com.boldfaced7.board.common.exception.ErrorCode;
import com.boldfaced7.board.common.exception.exception.BusinessBaseException;

public class MemberEmailDuplicatedException extends BusinessBaseException {
    public MemberEmailDuplicatedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
