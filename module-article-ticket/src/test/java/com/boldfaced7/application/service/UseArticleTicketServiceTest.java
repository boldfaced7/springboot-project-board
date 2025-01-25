package com.boldfaced7.application.service;

import com.boldfaced7.application.port.in.UseArticleTicketCommand;
import com.boldfaced7.exception.articleticket.ArticleTicketAlreadyUsedException;
import com.boldfaced7.exception.articleticket.ArticleTicketNotFoundException;
import com.boldfaced7.exception.auth.UnauthorizedException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.boldfaced7.ArticleTicketTestUtil.*;

class UseArticleTicketServiceTest {

    @DisplayName("[useArticleTicket] 티켓 작성 정보를 보내면, 수정된 티켓 정보를 반환")
    @Test
    void givenUpdateArticleTicketCommand_whenUsingTicket_thenReturnsUpdatedArticleTicket() {
        // given
        var sut = new UseArticleTicketService(
                id -> Optional.of(articleTicket(ID, EVENT_ID, MEMBER_ID, UNUSED)),
                ticket -> ticket
        );
        var command = new UseArticleTicketCommand(ID, MEMBER_ID);

        // when
        var result = sut.useArticleTicket(command);

        // then
        assertThat(result, ID, EVENT_ID, MEMBER_ID);
        Assertions.assertThat(result.getUsedAt()).isNotNull();
        Assertions.assertThat(result.getDeletedAt()).isNull();
    }

    @DisplayName("[useArticleTicket] 존재하지 않는 티켓 id를 보내면, 예외를 던짐")
    @Test
    void givenWrongArticleTicketId_whenUsingTicket_thenThrowsException() {
        // given
        var sut = new UseArticleTicketService(
                id -> Optional.empty(),
                null
        );
        var command = new UseArticleTicketCommand(ID, MEMBER_ID);

        // when
        var thrown = Assertions.assertThatThrownBy(
                () -> sut.useArticleTicket(command)
        );
        // then
        thrown.isInstanceOf(ArticleTicketNotFoundException.class);
    }

    @DisplayName("[useArticleTicket] 이미 사용된 티켓이면, 예외를 던짐")
    @Test
    void givenUsedTicketId_whenUsingTicket_thenThrowsException() {
        // given
        var sut = new UseArticleTicketService(
                id -> Optional.of(articleTicket(ID, EVENT_ID, MEMBER_ID, USED)),
                null
        );
        var command = new UseArticleTicketCommand(ID, MEMBER_ID);

        // when
        var thrown = Assertions.assertThatThrownBy(
                () -> sut.useArticleTicket(command)
        );
        // then
        thrown.isInstanceOf(ArticleTicketAlreadyUsedException.class);
    }

    @DisplayName("[useArticleTicket] 티켓 주인의 요청이 아니면, 예외를 던짐")
    @Test
    void givenWrongMemberId_whenUsingTicket_thenThrowsException() {
        // given
        var sut = new UseArticleTicketService(
                id -> Optional.of(articleTicket(ID, EVENT_ID, MEMBER_ID, UNUSED)),
                null
        );
        var command = new UseArticleTicketCommand(ID, INVALID_MEMBER_ID);

        // when
        var thrown = Assertions.assertThatThrownBy(
                () -> sut.useArticleTicket(command)
        );
        // then
        thrown.isInstanceOf(UnauthorizedException.class);
    }
}