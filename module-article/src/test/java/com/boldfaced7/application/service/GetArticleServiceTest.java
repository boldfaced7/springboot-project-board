package com.boldfaced7.application.service;

import com.boldfaced7.application.port.in.GetArticleCommand;
import com.boldfaced7.application.port.out.GetMemberInfoResponse;
import com.boldfaced7.application.port.out.ListAttachmentsInfoResponse;
import com.boldfaced7.exception.article.ArticleNotFoundException;
import com.boldfaced7.exception.member.MemberNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.boldfaced7.ArticleTestUtil.*;

class GetArticleServiceTest {

    @DisplayName("[getArticle] 게시글 id를 보내면, 게시글 정보를 반환")
    @Test
    void givenGetArticleCommand_whenRetrieving_thenReturnsResolvedArticle() {
        // given
        var sut = new GetArticleService(
                id -> Optional.of(article(ID, MEMBER_ID, TITLE, CONTENT)),
                request -> Optional.of(new GetMemberInfoResponse(MEMBER_ID, EMAIL, NICKNAME)),
                request -> new ListAttachmentsInfoResponse(ID, ATTACHMENTS)
        );
        var command = new GetArticleCommand(ID);

        // when
        var result = sut.getArticle(command);

        // then
        assertThat(result, ID, MEMBER_ID, TITLE, CONTENT, EMAIL, NICKNAME, ATTACHMENTS);
    }

    @DisplayName("[getArticle] 존재하지 않는 게시글 id를 보내면, 예외를 던짐")
    @Test
    void givenWrongArticleId_whenRetrieving_thenThrowsException() {
        // given
        var sut = new GetArticleService(id -> Optional.empty(), null, null);
        var command = new GetArticleCommand(ID);

        // when
        var thrown = Assertions.assertThatThrownBy(
                () -> sut.getArticle(command)
        );
        // then
        thrown.isInstanceOf(ArticleNotFoundException.class);
    }

    @DisplayName("[getArticle] 존재하지 않는 회원 id를 보내면, 예외를 던짐")
    @Test
    void givenWrongMemberId_whenRetrieving_thenThrowsException() {
        // given
        var dummy = Optional.of(article(ID, MEMBER_ID, TITLE, CONTENT));
        var sut = new GetArticleService(id -> dummy, request -> Optional.empty(), null);
        var command = new GetArticleCommand(ID);

        // when
        var thrown = Assertions.assertThatThrownBy(
                () -> sut.getArticle(command)
        );
        // then
        thrown.isInstanceOf(MemberNotFoundException.class);
    }
}