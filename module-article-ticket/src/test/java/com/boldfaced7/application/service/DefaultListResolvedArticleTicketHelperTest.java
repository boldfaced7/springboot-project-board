package com.boldfaced7.application.service;

import com.boldfaced7.application.port.out.ListMembersInfoResponse;
import com.boldfaced7.application.port.out.ListTicketEventsInfoResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.boldfaced7.ArticleTicketTestUtil.*;
import static com.boldfaced7.ArticleTicketTestUtil.assertThat;

class DefaultListResolvedArticleTicketHelperTest {
    @DisplayName("[listResolvedArticleTicket] 티켓 리스트를 보내면, 리졸브된 티켓 리스트를 반환")
    @Test
    void givenArticleTicket_whenRetrieving_thenReturnsResolvedArticleTicket() {
        // given
        var sut = new DefaultListResolvedArticleTicketsHelper(
                request -> new ListMembersInfoResponse(List.of(NICKNAME)),
                request -> new ListTicketEventsInfoResponse(
                        List.of(EVENT_ID), List.of(DISPLAY_NAME),
                        List.of(EXPIRING_AT), List.of(ISSUE_LIMIT))
        );
        var tickets = List.of(articleTicket(ID, EVENT_ID, MEMBER_ID, UNUSED));

        // when
        var results = sut.listResolvedArticleTickets(tickets);

        // then
        results.forEach(result -> assertThat(
                result, ID, EVENT_ID, MEMBER_ID, NICKNAME,
                DISPLAY_NAME, EXPIRING_AT, ISSUE_LIMIT, UNUSED
        ));
    }

}