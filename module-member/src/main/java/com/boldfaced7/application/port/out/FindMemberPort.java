package com.boldfaced7.application.port.out;

import com.boldfaced7.domain.Member;

import java.util.Optional;

public interface FindMemberPort {
    Optional<Member> findById(Member.Id id);
}
