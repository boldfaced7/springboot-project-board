package com.boldfaced7.application.service;

import com.boldfaced7.application.port.in.RegisterArticleTicketEventCommand;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.boldfaced7.ArticleTicketEventTestUtil.*;

class RegisterArticleTicketEventServiceTest {

    @DisplayName("[registerEvent] 이벤트 등록 정보를 보내면, 등록된 이벤트 정보를 반환")
    @Test
    void givenUpdateArticleTicketEventCommand_whenRegisteringTicket_thenReturnsUpdatedArticleTicketEvent() {
        // given
        var sut = new RegisterArticleTicketEventService(
                ticket -> setField(ticket, "id", ID)
        );
        var command = new RegisterArticleTicketEventCommand(
                DISPLAY_NAME, EXPIRING_AT, ISSUE_LIMIT, MEMBER_ID);

        // when
        var result = sut.registerEvent(command);

        // then
        assertThat(result, ID, DISPLAY_NAME, EXPIRING_AT, ISSUE_LIMIT);
        Assertions.assertThat(result.getUpdatedAt()).isNull();
        Assertions.assertThat(result.getDeletedAt()).isNull();
    }
}