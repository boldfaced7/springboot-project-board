package com.boldfaced7.application.service;

import com.boldfaced7.application.port.in.GetArticleCommentCommand;
import com.boldfaced7.application.port.out.GetMemberInfoResponse;
import com.boldfaced7.exception.article.ArticleNotFoundException;
import com.boldfaced7.exception.member.MemberNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.boldfaced7.ArticleCommentTestUtil.*;

class GetArticleCommentServiceTest {

    @DisplayName("[getArticleComment] 댓글 id를 보내면, 댓글 정보를 반환")
    @Test
    void givenGetArticleCommentCommand_whenRetrieving_thenReturnsResolvedArticleComment() {
        // given
        var sut = new GetArticleCommentService(
                id -> Optional.of(articleComment(ID, ARTICLE_ID, MEMBER_ID, CONTENT)),
                request -> Optional.of(new GetMemberInfoResponse(MEMBER_ID, "", NICKNAME))
        );
        var command = new GetArticleCommentCommand(ID);

        // when
        var result = sut.getArticleComment(command);

        // then
        assertThat(result, ID, ARTICLE_ID, MEMBER_ID, CONTENT, NICKNAME);
    }

    @DisplayName("[getArticleComment] 존재하지 않는 댓글 id를 보내면, 예외를 던짐")
    @Test
    void givenWrongArticleId_whenRetrieving_thenThrowsException() {
        // given
        var sut = new GetArticleCommentService(id -> Optional.empty(), null);
        var command = new GetArticleCommentCommand(ID);

        // when
        var thrown = Assertions.assertThatThrownBy(
                () -> sut.getArticleComment(command)
        );
        // then
        thrown.isInstanceOf(ArticleNotFoundException.class);
    }

    @DisplayName("[getArticleComment] 존재하지 않는 회원 id를 보내면, 예외를 던짐")
    @Test
    void givenWrongMemberId_whenRetrieving_thenThrowsException() {
        // given
        var dummy = Optional.of(articleComment(ID, ARTICLE_ID, MEMBER_ID, CONTENT));
        var sut = new GetArticleCommentService(id -> dummy, request -> Optional.empty());
        var command = new GetArticleCommentCommand(ID);

        // when
        var thrown = Assertions.assertThatThrownBy(
                () -> sut.getArticleComment(command)
        );
        // then
        thrown.isInstanceOf(MemberNotFoundException.class);
    }
}