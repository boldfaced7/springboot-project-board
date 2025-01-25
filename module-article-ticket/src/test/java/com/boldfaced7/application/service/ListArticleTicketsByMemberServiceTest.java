package com.boldfaced7.application.service;

import com.boldfaced7.application.port.in.ListArticleTicketsByMemberCommand;
import com.boldfaced7.application.port.out.GetMemberInfoResponse;
import com.boldfaced7.domain.ResolvedArticleTicket;
import com.boldfaced7.exception.member.MemberNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.boldfaced7.ArticleTicketTestUtil.*;
import static com.boldfaced7.ArticleTicketTestUtil.assertThat;

class ListArticleTicketsByMemberServiceTest {

    @DisplayName("[listMemberArticleTickets] 회원 id와 페이지 번호를 보내면, 해당 회원의 티켓 리스트를 반환")
    @Test
    void givenListMemberArticleTicketsCommand_whenRetrieving_thenReturnsResolvedArticleTickets() {
        // given
        var dummy = List.of(articleTicket(ID, EVENT_ID, MEMBER_ID, UNUSED));
        var sut = new ListArticleTicketsByMemberService(
                request -> Optional.of(new GetMemberInfoResponse(MEMBER_ID, "", NICKNAME)),
                (memberId, pageSize, pageNumber) -> dummy,
                request -> ResolvedArticleTicket.resolveAll(
                        dummy, NICKNAME, DISPLAY_NAME, EXPIRING_AT, ISSUE_LIMIT)
        );
        var command = new ListArticleTicketsByMemberCommand(MEMBER_ID, PAGE_NUMBER);

        // when
        var results = sut.listMemberArticleTickets(command);

        // then
        results.forEach(result -> assertThat(
                result, ID, EVENT_ID, MEMBER_ID, NICKNAME,
                DISPLAY_NAME, EXPIRING_AT, ISSUE_LIMIT, UNUSED
        ));
    }

    @DisplayName("[listMemberArticleTickets] 존재하지 않는 회원 id를 보내면, 예외를 던짐")
    @Test
    void givenWrongMemberId_whenRetrieving_thenThrowException() {
        // given
        var sut = new ListArticleTicketsByMemberService(
                request -> Optional.empty(),
                null,
                null
        );
        var command = new ListArticleTicketsByMemberCommand(MEMBER_ID, PAGE_NUMBER);

        // when
        var thrown = Assertions.assertThatThrownBy(
                () -> sut.listMemberArticleTickets(command)
        );
        // then
        thrown.isInstanceOf(MemberNotFoundException.class);
    }
}