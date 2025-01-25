package com.boldfaced7.application.service;

import com.boldfaced7.application.port.in.ListAllArticleTicketsCommand;
import com.boldfaced7.domain.ResolvedArticleTicket;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.boldfaced7.ArticleTicketTestUtil.*;

class ListAllArticleTicketsServiceTest {

    @DisplayName("[listAllArticleTickets] 페이지 번호를 보내면, 티켓 리스트를 반환")
    @Test
    void givenListAllArticleTicketsCommand_whenRetrieving_thenReturnsResolvedArticleTickets() {
        // given
        var dummy = List.of(articleTicket(ID, EVENT_ID, MEMBER_ID, UNUSED));
        var sut = new ListAllArticleTicketsService(
                (pageNumber, pageSize) -> dummy,
                request -> ResolvedArticleTicket.resolveAll(
                        dummy, NICKNAME, DISPLAY_NAME, EXPIRING_AT, ISSUE_LIMIT)
        );
        var command = new ListAllArticleTicketsCommand(PAGE_NUMBER);

        // when
        var results = sut.listAllArticleTickets(command);

        // then
        results.forEach(result -> assertThat(
                result, ID, EVENT_ID, MEMBER_ID, NICKNAME,
                DISPLAY_NAME, EXPIRING_AT, ISSUE_LIMIT, UNUSED
        ));
    }
}