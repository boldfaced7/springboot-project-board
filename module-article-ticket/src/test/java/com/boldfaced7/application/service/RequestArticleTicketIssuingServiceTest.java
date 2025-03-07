package com.boldfaced7.application.service;

import com.boldfaced7.application.port.in.RequestArticleTicketIssuingCommand;
import com.boldfaced7.application.port.out.ReduceAvailableTicketsResponse;
import com.boldfaced7.exception.articleticket.ArticleTicketSoldOutException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.boldfaced7.ArticleTicketTestUtil.*;

class RequestArticleTicketIssuingServiceTest {

    @DisplayName("[requestIssuing] 발급 가능한 티켓이 있으면, 예외를 던지지 않음")
    @Test
    void givenRequestArticleTicketIssuingCommand_whenRequesting_thenReturnsTrue() {
        // given
        var sut = new RequestArticleTicketIssuingService(
                request -> new ReduceAvailableTicketsResponse(EVENT_ID, VALID)
        );
        var command = new RequestArticleTicketIssuingCommand(EVENT_ID, MEMBER_ID);

        // when
        var notThrown = Assertions.assertThatCode(
                () -> sut.requestIssuing(command)
        );
        // then
        notThrown.doesNotThrowAnyException();
    }

    @DisplayName("[requestIssuing] 발급 가능한 티켓이 없으면, 예외를 던짐")
    @Test
    void givenRequestArticleTicketIssuingCommand_whenRequesting_thenReturnsFalse() {
        // given
        var sut = new RequestArticleTicketIssuingService(
                request -> new ReduceAvailableTicketsResponse(EVENT_ID, INVALID)
        );
        var command = new RequestArticleTicketIssuingCommand(EVENT_ID, MEMBER_ID);

        // when
        var thrown = Assertions.assertThatThrownBy(
                () -> sut.requestIssuing(command)
        );
        // then
        thrown.isInstanceOf(ArticleTicketSoldOutException.class);
    }
}