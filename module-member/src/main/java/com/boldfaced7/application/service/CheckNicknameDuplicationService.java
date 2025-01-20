package com.boldfaced7.application.service;

import com.boldfaced7.Query;
import com.boldfaced7.application.port.in.CheckNicknameDuplicationCommand;
import com.boldfaced7.application.port.in.CheckNicknameDuplicationQuery;
import com.boldfaced7.application.port.out.CheckNicknameDuplicationPort;
import com.boldfaced7.domain.Member;
import lombok.RequiredArgsConstructor;

@Query
@RequiredArgsConstructor
public class CheckNicknameDuplicationService implements CheckNicknameDuplicationQuery {

    private final CheckNicknameDuplicationPort checkNicknameDuplicationPort;

    @Override
    public boolean isDuplicated(CheckNicknameDuplicationCommand command) {
        Member.Nickname nickname = new Member.Nickname(command.nickname());
        return checkNicknameDuplicationPort.isDuplicated(nickname);
    }
}
