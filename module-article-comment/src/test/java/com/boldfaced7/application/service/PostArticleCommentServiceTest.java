package com.boldfaced7.application.service;

import com.boldfaced7.application.port.in.PostArticleCommentCommand;
import com.boldfaced7.application.port.out.GetArticleInfoResponse;
import com.boldfaced7.application.port.out.GetMemberInfoResponse;
import com.boldfaced7.exception.article.ArticleNotFoundException;
import com.boldfaced7.exception.member.MemberNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.boldfaced7.ArticleCommentTestUtil.*;

class PostArticleCommentServiceTest {

    @DisplayName("[postArticleComment] 댓글 작성 정보를 보내면, 저장된 댓글 정보를 반환")
    @Test
    void givenPostArticleCommentCommand_whenSaving_thenReturnsSavedArticleComment() {
        // given
        var sut = new PostArticleCommentService(
                request -> Optional.of(new GetMemberInfoResponse(MEMBER_ID, "", NICKNAME)),
                request -> Optional.of(new GetArticleInfoResponse(ARTICLE_ID, VALID)),
                articleComment -> setField(articleComment, "id", ID)
        );
        var command = new PostArticleCommentCommand(ARTICLE_ID, MEMBER_ID, CONTENT);

        // when
        var result = sut.postArticleComment(command);

        // then
        assertThat(result, ID, ARTICLE_ID, MEMBER_ID, CONTENT);
    }

    @DisplayName("[postArticleComment] 존재하지 않는 회원 id를 보내면, 예외를 던짐")
    @Test
    void givenWrongMemberId_whenSaving_thenThrowsException() {
        // given
        var sut = new PostArticleCommentService(
                request -> Optional.empty(),
                null,
                null
        );
        var command = new PostArticleCommentCommand(ARTICLE_ID, MEMBER_ID, CONTENT);

        // when
        var thrown = Assertions.assertThatThrownBy(
                () -> sut.postArticleComment(command)
        );
        // then
        thrown.isInstanceOf(MemberNotFoundException.class);
    }

    @DisplayName("[postArticleComment] 존재하지 않는 게시글 id를 보내면, 예외를 던짐")
    @Test
    void givenNoArticleTicketAvailable_whenSaving_thenThrowsException() {
        // given
        var sut = new PostArticleCommentService(
                request -> Optional.of(new GetMemberInfoResponse(MEMBER_ID, "", NICKNAME)),
                request -> Optional.empty(),
                null
        );
        var command = new PostArticleCommentCommand(ARTICLE_ID, MEMBER_ID, CONTENT);

        // when
        var thrown = Assertions.assertThatThrownBy(
                () -> sut.postArticleComment(command)
        );
        // then
        thrown.isInstanceOf(ArticleNotFoundException.class);
    }
}