package com.boldfaced7.application.port.out;

import com.boldfaced7.domain.Member;

public interface CheckNicknameDuplicationPort {
    boolean isDuplicated(Member.Nickname nickname);
}
