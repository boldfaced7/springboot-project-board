package com.boldfaced7.application.port.in;

import com.boldfaced7.domain.Member;

public interface RegisterMemberUseCase {
    Member registerMember(RegisterMemberCommand command);
}
