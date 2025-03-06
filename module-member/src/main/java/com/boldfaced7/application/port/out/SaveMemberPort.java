package com.boldfaced7.application.port.out;

import com.boldfaced7.domain.Member;

public interface SaveMemberPort {
    Member save(Member member);
}
