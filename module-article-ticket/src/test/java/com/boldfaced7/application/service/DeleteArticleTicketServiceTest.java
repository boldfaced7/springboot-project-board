package com.boldfaced7.application.service;

import com.boldfaced7.application.port.in.DeleteArticleTicketCommand;
import com.boldfaced7.exception.articleticket.ArticleTicketNotFoundException;
import com.boldfaced7.exception.auth.UnauthorizedException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.boldfaced7.ArticleTicketTestUtil.*;

class DeleteArticleTicketServiceTest {

    @DisplayName("[deleteArticleTicket] 티켓 정보를 보내면, 삭제된 티켓 정보를 반환")
    @Test
    void givenDeleteArticleTicketCommand_whenDeleting_thenReturnsDeletedArticleTicket() {
        // given
        var sut = new DeleteArticleTicketService(
                id -> Optional.of(articleTicket(ID, EVENT_ID, MEMBER_ID, UNUSED)),
                ticket -> ticket
        );
        var command = new DeleteArticleTicketCommand(ID, MEMBER_ID);

        // when
        var result = sut.deleteArticleTicket(command);

        // then
        assertThat(result, ID, EVENT_ID, MEMBER_ID);
        Assertions.assertThat(result.getDeletedAt()).isNotNull();
    }
    
    @DisplayName("[deleteArticleTicket] 존재하지 않는 티켓 id를 보내면, 예외를 던짐")
    @Test
    void givenWrongArticleTicketId_whenDeleting_thenThrowsException() {
        // given
        var sut = new DeleteArticleTicketService(
                id -> Optional.empty(),
                null
        );
        var command = new DeleteArticleTicketCommand(ID, MEMBER_ID);

        // when
        var thrown = Assertions.assertThatThrownBy(
                () -> sut.deleteArticleTicket(command)
        );
        // then
        thrown.isInstanceOf(ArticleTicketNotFoundException.class);
    }

    @DisplayName("[deleteArticleTicket] 티켓 작성자의 요청이 아니면, 예외를 던짐")
    @Test
    void givenWrongMemberId_whenDeleting_thenThrowsException() {
        // given
        var sut = new DeleteArticleTicketService(
                id -> Optional.of(articleTicket(ID, EVENT_ID, MEMBER_ID, UNUSED)),
                null
        );
        var command = new DeleteArticleTicketCommand(ID, INVALID_MEMBER_ID);

        // when
        var thrown = Assertions.assertThatThrownBy(
                () -> sut.deleteArticleTicket(command)
        );
        // then
        thrown.isInstanceOf(UnauthorizedException.class);
    }
}