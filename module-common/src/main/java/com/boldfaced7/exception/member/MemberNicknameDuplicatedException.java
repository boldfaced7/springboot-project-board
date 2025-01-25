package com.boldfaced7.exception.member;

import com.boldfaced7.exception.BusinessBaseException;
import com.boldfaced7.exception.ErrorCode;

public class MemberNicknameDuplicatedException extends BusinessBaseException {
    public MemberNicknameDuplicatedException() {
        super(ErrorCode.MEMBER_EMAIL_DUPLICATED);
    }
}
