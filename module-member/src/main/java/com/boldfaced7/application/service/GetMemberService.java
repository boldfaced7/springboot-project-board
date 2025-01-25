package com.boldfaced7.application.service;

import com.boldfaced7.Query;
import com.boldfaced7.application.port.in.GetMemberCommand;
import com.boldfaced7.application.port.in.GetMemberQuery;
import com.boldfaced7.application.port.out.FindMemberPort;
import com.boldfaced7.domain.Member;
import com.boldfaced7.exception.member.MemberNotFoundException;
import lombok.RequiredArgsConstructor;

import static com.boldfaced7.domain.Member.*;

@Query
@RequiredArgsConstructor
public class GetMemberService implements GetMemberQuery {

    private final FindMemberPort findMemberPort;

    @Override
    public Member getMember(GetMemberCommand command) {
        Id id = new Id(command.memberId());
        return findMemberPort.findById(id)
                .orElseThrow(MemberNotFoundException::new);
    }
}
