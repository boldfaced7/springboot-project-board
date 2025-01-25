package com.boldfaced7.application.service;

import com.boldfaced7.application.port.in.ListArticleTicketsByEventCommand;
import com.boldfaced7.application.port.out.GetTicketEventInfoResponse;
import com.boldfaced7.domain.ResolvedArticleTicket;
import com.boldfaced7.exception.articleticketevent.ArticleTicketEventNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.boldfaced7.ArticleTicketTestUtil.*;

class ListArticleTicketsByEventServiceTest {

    @DisplayName("[listArticleTicketsByEvent] 이벤트 Id와 페이지 번호를 보내면, 티켓 리스트를 반환")
    @Test
    void givenListArticleTicketsByEventCommand_whenRetrieving_thenReturnsResolvedArticleTickets() {
        // given
        var dummy = List.of(articleTicket(ID, EVENT_ID, MEMBER_ID, UNUSED));
        var sut = new ListArticleTicketsByEventService(
                request -> Optional.of(new GetTicketEventInfoResponse(
                        EVENT_ID, DISPLAY_NAME, EXPIRING_AT, ISSUE_LIMIT, VALID)),
                (eventId, pageNumber, pageSize) -> dummy,
                request -> ResolvedArticleTicket.resolveAll(
                        dummy, NICKNAME, DISPLAY_NAME, EXPIRING_AT, ISSUE_LIMIT)
        );
        var command = new ListArticleTicketsByEventCommand(EVENT_ID, PAGE_NUMBER);

        // when
        var results = sut.listArticleTicketsByEvent(command);

        // then
        results.forEach(result -> assertThat(
                result, ID, EVENT_ID, MEMBER_ID, NICKNAME,
                DISPLAY_NAME, EXPIRING_AT, ISSUE_LIMIT, UNUSED
        ));
    }

    @DisplayName("[listArticleTicketsByEvent] 존재하지 않는 이벤트 id를 보내면, 예외를 던짐")
    @Test
    void givenWrongEventId_whenRetrieving_thenThrowException() {
        // given
        var sut = new ListArticleTicketsByEventService(
                request -> Optional.empty(),
                null,
                null
        );
        var command = new ListArticleTicketsByEventCommand(EVENT_ID, PAGE_NUMBER);

        // when
        var thrown = Assertions.assertThatThrownBy(
                () -> sut.listArticleTicketsByEvent(command)
        );

        // then
        thrown.isInstanceOf(ArticleTicketEventNotFoundException.class);
    }
}