package com.boldfaced7.application.service;

import com.boldfaced7.application.port.in.ChangeDisplayNameCommand;
import com.boldfaced7.exception.articleticketevent.ArticleTicketEventNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.boldfaced7.ArticleTicketEventTestUtil.*;

class UpdateArticleTicketEventServiceTest {

    @DisplayName("[updateEvent] 이벤트 작성 정보를 보내면, 수정된 이벤트 정보를 반환")
    @Test
    void givenUpdateArticleTicketEventCommand_whenUpdatingTicket_thenReturnsUpdatedArticleTicketEvent() {
        // given
        var sut = new ChangeDisplayNameService(
                id -> Optional.of(articleTicketEvent(ID, DISPLAY_NAME, EXPIRING_AT, ISSUE_LIMIT)),
                ticket -> ticket
        );
        var command = new ChangeDisplayNameCommand(ID, DISPLAY_NAME, MEMBER_ID);

        // when
        var result = sut.changeDisplayName(command);

        // then
        assertThat(result, ID, DISPLAY_NAME, EXPIRING_AT, ISSUE_LIMIT);
        Assertions.assertThat(result.getUpdatedAt()).isNotNull();
        Assertions.assertThat(result.getDeletedAt()).isNull();
    }

    @DisplayName("[updateEvent] 존재하지 않는 이벤트 id를 보내면, 예외를 던짐")
    @Test
    void givenWrongArticleTicketId_whenUpdatingTicket_thenThrowsException() {
        // given
        var sut = new ChangeDisplayNameService(
                id -> Optional.empty(),
                null
        );
        var command = new ChangeDisplayNameCommand(ID, DISPLAY_NAME, MEMBER_ID);

        // when
        var thrown = Assertions.assertThatThrownBy(
                () -> sut.changeDisplayName(command)
        );
        // then
        thrown.isInstanceOf(ArticleTicketEventNotFoundException.class);
    }
}