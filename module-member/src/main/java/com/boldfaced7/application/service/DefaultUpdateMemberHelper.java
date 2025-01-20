package com.boldfaced7.application.service;

import com.boldfaced7.ServiceHelper;
import com.boldfaced7.application.port.in.UpdateMemberHelper;
import com.boldfaced7.application.port.out.FindMemberPort;
import com.boldfaced7.application.port.out.UpdateMemberPort;
import com.boldfaced7.domain.Member;
import com.boldfaced7.exception.member.MemberNotFoundException;
import lombok.RequiredArgsConstructor;

@ServiceHelper
@RequiredArgsConstructor
public class DefaultUpdateMemberHelper implements UpdateMemberHelper {

    private final FindMemberPort findMemberPort;
    private final UpdateMemberPort updateMemberPort;

    @Override
    public Member updateMember(String memberId, MemberUpdater updater) {
        Member found = getMember(memberId);
        return updateMember(found, updater);
    }

    private Member getMember(String memberId) {
        Member.Id id = new Member.Id(memberId);
        return findMemberPort.findById(id)
                .orElseThrow(MemberNotFoundException::new);
    }

    private Member updateMember(Member member, MemberUpdater updater) {
        Member updated = updater.update(member);
        return updateMemberPort.update(updated);
    }
}
