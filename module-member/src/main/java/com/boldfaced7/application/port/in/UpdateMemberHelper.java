package com.boldfaced7.application.port.in;

import com.boldfaced7.domain.Member;

public interface UpdateMemberHelper {
    Member updateMember(String memberId, MemberUpdater updater);

    @FunctionalInterface
    interface MemberUpdater {
        Member update(Member member);
    }
}
