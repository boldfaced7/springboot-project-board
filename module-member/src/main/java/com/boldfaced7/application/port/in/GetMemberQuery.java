package com.boldfaced7.application.port.in;

import com.boldfaced7.domain.Member;

public interface GetMemberQuery {
    Member getMember(GetMemberCommand command);
}
