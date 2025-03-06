package com.boldfaced7.application.port.out;

import com.boldfaced7.domain.Member;

import java.util.List;

public interface ListAllMembersPort {
    List<Member> listAll(int pageNumber, int pageSize);
}
