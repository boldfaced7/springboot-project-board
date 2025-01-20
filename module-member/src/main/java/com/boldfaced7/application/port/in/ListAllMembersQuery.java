package com.boldfaced7.application.port.in;

import com.boldfaced7.domain.Member;

import java.util.List;

public interface ListAllMembersQuery {
    List<Member> listAllMembers(ListAllMembersCommand command);
}
