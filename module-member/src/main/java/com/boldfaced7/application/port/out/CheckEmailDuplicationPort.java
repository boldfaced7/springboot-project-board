package com.boldfaced7.application.port.out;

import com.boldfaced7.domain.Member;

public interface CheckEmailDuplicationPort {
    boolean isDuplicated(Member.Email email);
}
