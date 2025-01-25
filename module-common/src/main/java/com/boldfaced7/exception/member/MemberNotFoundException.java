package com.boldfaced7.exception.member;

import com.boldfaced7.exception.ErrorCode;
import com.boldfaced7.exception.entity.NotFoundException;

public class MemberNotFoundException extends NotFoundException {

    public MemberNotFoundException() {
        super(ErrorCode.MEMBER_NOT_FOUND);
    }
}
