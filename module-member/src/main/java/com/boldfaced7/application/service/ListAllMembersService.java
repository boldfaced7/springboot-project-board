package com.boldfaced7.application.service;

import com.boldfaced7.Query;
import com.boldfaced7.application.port.in.ListAllMembersCommand;
import com.boldfaced7.application.port.in.ListAllMembersQuery;
import com.boldfaced7.application.port.out.ListAllMembersPort;
import com.boldfaced7.domain.Member;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Query
@RequiredArgsConstructor
public class ListAllMembersService implements ListAllMembersQuery {

    private static final int PAGE_SIZE = 20;

    private final ListAllMembersPort listAllMembersPort;

    @Override
    public List<Member> listAllMembers(ListAllMembersCommand command) {
        return listAllMembersPort.listAll(command.pageNumber(), PAGE_SIZE);
    }
}
