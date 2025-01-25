package com.boldfaced7.application.service;

import com.boldfaced7.application.port.in.DeleteArticleTicketEventCommand;
import com.boldfaced7.exception.articleticketevent.ArticleTicketEventNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.boldfaced7.ArticleTicketEventTestUtil.*;

class DeleteArticleTicketEventServiceTest {

    @DisplayName("[deleteEvent] 이벤트 정보를 보내면, 삭제된 이벤트 정보를 반환")
    @Test
    void givenDeleteArticleTicketEventCommand_whenDeleting_thenReturnsDeletedArticleTicketEvent() {
        // given
        var sut = new DeleteArticleTicketEventService(
                id -> Optional.of(articleTicketEvent(ID, DISPLAY_NAME, EXPIRING_AT, ISSUE_LIMIT)),
                ticket -> ticket
        );
        var command = new DeleteArticleTicketEventCommand(ID, MEMBER_ID);

        // when
        var result = sut.deleteEvent(command);

        // then
        assertThat(result, ID, DISPLAY_NAME, EXPIRING_AT, ISSUE_LIMIT);
        Assertions.assertThat(result.getDeletedAt()).isNotNull();
    }
    
    @DisplayName("[deleteEvent] 존재하지 않는 이벤트 id를 보내면, 예외를 던짐")
    @Test
    void givenWrongArticleTicketId_whenDeleting_thenThrowsException() {
        // given
        var sut = new DeleteArticleTicketEventService(
                id -> Optional.empty(),
                null
        );
        var command = new DeleteArticleTicketEventCommand(ID, MEMBER_ID);

        // when
        var thrown = Assertions.assertThatThrownBy(
                () -> sut.deleteEvent(command)
        );
        // then
        thrown.isInstanceOf(ArticleTicketEventNotFoundException.class);
    }
}