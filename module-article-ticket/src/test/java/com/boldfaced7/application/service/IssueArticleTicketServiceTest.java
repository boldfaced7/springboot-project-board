package com.boldfaced7.application.service;

import com.boldfaced7.application.port.in.IssueArticleTicketCommand;
import com.boldfaced7.application.port.out.GetMemberInfoResponse;
import com.boldfaced7.application.port.out.GetTicketEventInfoResponse;
import com.boldfaced7.exception.articleticketevent.ArticleTicketEventNotFoundException;
import com.boldfaced7.exception.member.MemberNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.boldfaced7.ArticleTicketTestUtil.*;

class IssueArticleTicketServiceTest {

    @DisplayName("[issueTicket] 티켓 발급 정보를 보내면, 발급된 티켓 정보를 반환")
    @Test
    void givenUpdateArticleTicketCommand_whenIssuingTicket_thenReturnsUpdatedArticleTicket() {
        // given
        var sut = new IssueArticleTicketService(
                request -> Optional.of(new GetMemberInfoResponse(
                        MEMBER_ID, "", "")),
                request -> Optional.of(new GetTicketEventInfoResponse(
                        EVENT_ID, DISPLAY_NAME, EXPIRING_AT, ISSUE_LIMIT, VALID)),
                ticket -> setField(ticket, "id", ID)
        );
        var command = new IssueArticleTicketCommand(EVENT_ID, MEMBER_ID);

        // when
        var result = sut.issueTicket(command);

        // then
        assertThat(result, ID, EVENT_ID, MEMBER_ID);
        Assertions.assertThat(result.getUsedAt()).isNull();
        Assertions.assertThat(result.getDeletedAt()).isNull();
    }

    @DisplayName("[issueTicket] 존재하지 않는 회원 id를 보내면, 예외를 던짐")
    @Test
    void givenWrongMemberId_whenIssuingTicket_thenThrowsException() {
        // given
        var sut = new IssueArticleTicketService(
                id -> Optional.empty(),
                null,
                null
        );
        var command = new IssueArticleTicketCommand(EVENT_ID, MEMBER_ID);

        // when
        var thrown = Assertions.assertThatThrownBy(
                () -> sut.issueTicket(command)
        );
        // then
        thrown.isInstanceOf(MemberNotFoundException.class);
    }

    @DisplayName("[issueTicket] 존재하지 않는 이벤트 id를 보내면, 예외를 던짐")
    @Test
    void givenWrongEventId_whenIssuingTicket_thenThrowsException() {
        // given
        var sut = new IssueArticleTicketService(
                request -> Optional.of(new GetMemberInfoResponse(
                        MEMBER_ID, "", "")),
                id -> Optional.empty(),
                null
        );
        var command = new IssueArticleTicketCommand(EVENT_ID, MEMBER_ID);

        // when
        var thrown = Assertions.assertThatThrownBy(
                () -> sut.issueTicket(command)
        );
        // then
        thrown.isInstanceOf(ArticleTicketEventNotFoundException.class);
    }
}