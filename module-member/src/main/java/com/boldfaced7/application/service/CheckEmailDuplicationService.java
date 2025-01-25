package com.boldfaced7.application.service;

import com.boldfaced7.Query;
import com.boldfaced7.application.port.in.CheckEmailDuplicationCommand;
import com.boldfaced7.application.port.in.CheckEmailDuplicationQuery;
import com.boldfaced7.application.port.out.CheckEmailDuplicationPort;
import com.boldfaced7.domain.Member;
import lombok.RequiredArgsConstructor;

@Query
@RequiredArgsConstructor
public class CheckEmailDuplicationService implements CheckEmailDuplicationQuery {

    private final CheckEmailDuplicationPort checkEmailDuplicationPort;

    @Override
    public boolean isDuplicated(CheckEmailDuplicationCommand command) {
        Member.Email email = new Member.Email(command.email());
        return checkEmailDuplicationPort.isDuplicated(email);
    }
}
