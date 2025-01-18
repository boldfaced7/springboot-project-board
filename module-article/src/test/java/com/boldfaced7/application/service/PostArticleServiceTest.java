package com.boldfaced7.application.service;

import com.boldfaced7.application.port.in.PostArticleCommand;
import com.boldfaced7.application.port.out.ConsumeArticleTicketResponse;
import com.boldfaced7.application.port.out.GetMemberInfoResponse;
import com.boldfaced7.exception.articleticket.ArticleTicketNotFoundException;
import com.boldfaced7.exception.member.MemberNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.boldfaced7.ArticleTestUtil.*;

class PostArticleServiceTest {

    @DisplayName("[postArticle] 게시글 작성 정보를 보내면, 저장된 게시글 정보를 반환")
    @Test
    void givenPostArticleCommand_whenSaving_thenReturnsSavedArticle() {
        // given
        var sut = new PostArticleService(
                request -> Optional.of(new GetMemberInfoResponse(MEMBER_ID, EMAIL, NICKNAME)),
                request -> Optional.of(new ConsumeArticleTicketResponse(MEMBER_ID, VALID)),
                article -> setField(article, "id", ID)
        );
        var command = new PostArticleCommand(MEMBER_ID, TITLE, CONTENT);

        // when
        var result = sut.postArticle(command);

        // then
        assertThat(result, ID, MEMBER_ID, TITLE, CONTENT);
    }

    @DisplayName("[postArticle] 존재하지 않는 회원 id를 보내면, 예외를 던짐")
    @Test
    void givenWrongMemberId_whenSaving_thenThrowsException() {
        // given
        var sut = new PostArticleService(request -> Optional.empty(), null, null);
        var command = new PostArticleCommand(MEMBER_ID, TITLE, CONTENT);

        // when
        var thrown = Assertions.assertThatThrownBy(
                () -> sut.postArticle(command)
        );
        // then
        thrown.isInstanceOf(MemberNotFoundException.class);
    }

    @DisplayName("[postArticle] 게시글 작성에 사용할 티켓이 없으면, 예외를 던짐")
    @Test
    void givenNoArticleTicketAvailable_whenSaving_thenThrowsException() {
        // given
        var sut = new PostArticleService(
                request -> Optional.of(new GetMemberInfoResponse(MEMBER_ID, EMAIL, NICKNAME)),
                request -> Optional.empty(),
                null
        );
        var command = new PostArticleCommand(MEMBER_ID, TITLE, CONTENT);

        // when
        var thrown = Assertions.assertThatThrownBy(
                () -> sut.postArticle(command)
        );
        // then
        thrown.isInstanceOf(ArticleTicketNotFoundException.class);
    }
}