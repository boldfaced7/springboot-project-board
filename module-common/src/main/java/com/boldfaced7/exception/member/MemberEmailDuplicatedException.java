package com.boldfaced7.exception.member;

import com.boldfaced7.exception.ErrorCode;
import com.boldfaced7.exception.BusinessBaseException;

public class MemberEmailDuplicatedException extends BusinessBaseException {
    public MemberEmailDuplicatedException() {
        super(ErrorCode.MEMBER_EMAIL_DUPLICATED);
    }
}
