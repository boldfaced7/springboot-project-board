package com.boldfaced7.application.service;

import com.boldfaced7.MemberTestUtil;
import com.boldfaced7.application.port.in.ListAllMembersCommand;
import com.boldfaced7.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.boldfaced7.MemberTestUtil.*;

class ListAllMembersServiceTest {

    @DisplayName("[listAllMembers] 페이지 번호를 보내면, 회원 리스트를 반환")
    @Test
    void givenListMembersCommand_whenRetrieving_thenReturnsResolvedMembers() {
        // given
        var dummy = List.of(member(ID, EMAIL, PASSWORD, NICKNAME));
        var sut = new ListAllMembersService((pageNumber, pageSize) -> dummy);
        var command = new ListAllMembersCommand(PAGE_NUMBER);

        // when
        var results = sut.listAllMembers(command);

        // then
        results.forEach(this::assertThat);
    }

    private void assertThat(Member result) {
        MemberTestUtil.assertThat(result, ID, EMAIL, PASSWORD, NICKNAME);
    }
}