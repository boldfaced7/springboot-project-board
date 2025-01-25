package com.boldfaced7.application.service;

import com.boldfaced7.application.port.in.GetArticleTicketEventCommand;
import com.boldfaced7.exception.articleticketevent.ArticleTicketEventNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.boldfaced7.ArticleTicketEventTestUtil.*;

class GetArticleTicketEventServiceTest {

    @DisplayName("[getEvent] 이벤트 id를 보내면, 이벤트 정보를 반환")
    @Test
    void givenGetArticleTicketEventCommand_whenRetrieving_thenReturnsResolvedArticleTicketEvent() {
        // given
        var dummy = articleTicketEvent(ID, DISPLAY_NAME, EXPIRING_AT, ISSUE_LIMIT);
        var sut = new GetArticleTicketEventService(
                id -> Optional.of(dummy)
        );
        var command = new GetArticleTicketEventCommand(ID);

        // when
        var result = sut.getEvent(command);

        // then
        assertThat(result, ID, DISPLAY_NAME, EXPIRING_AT, ISSUE_LIMIT);
    }

    @DisplayName("[getEvent] 존재하지 않는 이벤트 id를 보내면, 예외를 던짐")
    @Test
    void givenWrongEventId_whenRetrieving_thenThrowsException() {
        // given
        var sut = new GetArticleTicketEventService(
                id -> Optional.empty()
        );
        var command = new GetArticleTicketEventCommand(ID);

        // when
        var thrown = Assertions.assertThatThrownBy(
                () -> sut.getEvent(command)
        );
        // then
        thrown.isInstanceOf(ArticleTicketEventNotFoundException.class);
    }
}