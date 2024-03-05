package com.boldfaced7.board.error.exception.member;

import com.boldfaced7.board.error.ErrorCode;
import com.boldfaced7.board.error.exception.BusinessBaseException;

public class MemberEmailDuplicatedException extends BusinessBaseException {
    public MemberEmailDuplicatedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
