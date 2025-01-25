package com.boldfaced7.application.service;

import com.boldfaced7.application.port.in.GetArticleTicketCommand;
import com.boldfaced7.application.port.out.GetMemberInfoResponse;
import com.boldfaced7.application.port.out.GetTicketEventInfoResponse;
import com.boldfaced7.exception.articleticket.ArticleTicketNotFoundException;
import com.boldfaced7.exception.articleticketevent.ArticleTicketEventNotFoundException;
import com.boldfaced7.exception.member.MemberNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.boldfaced7.ArticleTicketTestUtil.*;

class GetArticleTicketServiceTest {

    @DisplayName("[getArticleTicket] 티켓 id를 보내면, 티켓 정보를 반환")
    @Test
    void givenGetArticleTicketCommand_whenRetrieving_thenReturnsResolvedArticleTicket() {
        // given
        var sut = new GetArticleTicketService(
                id -> Optional.of(articleTicket(ID, EVENT_ID, MEMBER_ID, UNUSED)),
                request -> Optional.of(new GetMemberInfoResponse(MEMBER_ID, EMAIL, NICKNAME)),
                request -> Optional.of(new GetTicketEventInfoResponse(
                        EVENT_ID, DISPLAY_NAME, EXPIRING_AT, ISSUE_LIMIT, VALID))
        );
        var command = new GetArticleTicketCommand(ID);

        // when
        var result = sut.getArticleTicket(command);

        // then
        assertThat(result, ID, EVENT_ID, MEMBER_ID, NICKNAME, DISPLAY_NAME, EXPIRING_AT, ISSUE_LIMIT, UNUSED);
    }

    @DisplayName("[getArticleTicket] 존재하지 않는 티켓 id를 보내면, 예외를 던짐")
    @Test
    void givenWrongArticleId_whenRetrieving_thenThrowsException() {
        // given
        var sut = new GetArticleTicketService(
                id -> Optional.empty(),
                null,
                null
        );
        var command = new GetArticleTicketCommand(ID);

        // when
        var thrown = Assertions.assertThatThrownBy(
                () -> sut.getArticleTicket(command)
        );
        // then
        thrown.isInstanceOf(ArticleTicketNotFoundException.class);
    }

    @DisplayName("[getArticleTicket] 존재하지 않는 회원 id를 보내면, 예외를 던짐")
    @Test
    void givenWrongMemberId_whenRetrieving_thenThrowsException() {
        // given
        var dummy = Optional.of(articleTicket(ID, EVENT_ID, MEMBER_ID, UNUSED));
        var sut = new GetArticleTicketService(
                id -> dummy,
                request -> Optional.empty(),
                null
        );
        var command = new GetArticleTicketCommand(ID);

        // when
        var thrown = Assertions.assertThatThrownBy(
                () -> sut.getArticleTicket(command)
        );
        // then
        thrown.isInstanceOf(MemberNotFoundException.class);
    }

    @DisplayName("[getArticleTicket] 존재하지 않는 이벤트 id를 보내면, 예외를 던짐")
    @Test
    void givenWrongEventId_whenRetrieving_thenThrowsException() {
        // given
        var dummy = Optional.of(articleTicket(ID, EVENT_ID, MEMBER_ID, UNUSED));
        var sut = new GetArticleTicketService(
                id -> dummy,
                request -> Optional.of(new GetMemberInfoResponse("", "", NICKNAME)),
                request -> Optional.empty()
        );
        var command = new GetArticleTicketCommand(ID);

        // when
        var thrown = Assertions.assertThatThrownBy(
                () -> sut.getArticleTicket(command)
        );
        // then
        thrown.isInstanceOf(ArticleTicketEventNotFoundException.class);
    }
}