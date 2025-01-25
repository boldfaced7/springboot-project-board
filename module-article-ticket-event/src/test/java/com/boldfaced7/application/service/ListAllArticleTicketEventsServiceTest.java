package com.boldfaced7.application.service;

import com.boldfaced7.application.port.in.ListAllArticleTicketEventsCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.boldfaced7.ArticleTicketEventTestUtil.*;

class ListAllArticleTicketEventsServiceTest {

    @DisplayName("[listAllEvents] 페이지 번호를 보내면, 이벤트 리스트를 반환")
    @Test
    void givenListAllArticleTicketsCommand_whenRetrieving_thenReturnsResolvedArticleTicketEvents() {
        // given
        var dummy = List.of(articleTicketEvent(ID, DISPLAY_NAME, EXPIRING_AT, ISSUE_LIMIT));
        var sut = new ListAllArticleTicketEventsService(
                (pageNumber, pageSize) -> dummy
        );
        var command = new ListAllArticleTicketEventsCommand(PAGE_NUMBER);

        // when
        var results = sut.listAllEvents(command);

        // then
        results.forEach(result -> assertThat(
                result, ID, DISPLAY_NAME, EXPIRING_AT, ISSUE_LIMIT
        ));
    }
}