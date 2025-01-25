package com.boldfaced7.application.service;

import com.boldfaced7.UseCase;
import com.boldfaced7.application.port.in.DeleteMemberCommand;
import com.boldfaced7.application.port.in.DeleteMemberUseCase;
import com.boldfaced7.application.port.out.FindMemberPort;
import com.boldfaced7.application.port.out.UpdateMemberPort;
import com.boldfaced7.domain.Member;
import com.boldfaced7.exception.member.MemberNotFoundException;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class DeleteMemberService implements DeleteMemberUseCase {

    private final FindMemberPort findMemberPort;
    private final UpdateMemberPort updateMemberPort;

    @Override
    public Member deleteMember(DeleteMemberCommand command) {
        Member found = getMember(command.memberId());
        Member deleted = found.delete();
        return updateMemberPort.update(deleted);
    }

    private Member getMember(String memberId) {
        Member.Id id = new Member.Id(memberId);
        return findMemberPort.findById(id)
                .orElseThrow(MemberNotFoundException::new);
    }
}
