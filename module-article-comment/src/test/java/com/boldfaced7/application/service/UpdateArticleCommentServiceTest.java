package com.boldfaced7.application.service;

import com.boldfaced7.application.port.in.UpdateArticleCommentCommand;
import com.boldfaced7.exception.articlecomment.ArticleCommentNotFoundException;
import com.boldfaced7.exception.auth.UnauthorizedException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.boldfaced7.ArticleCommentTestUtil.*;

class UpdateArticleCommentServiceTest {

    @DisplayName("[updateArticleComment] 댓글 작성 정보를 보내면, 수정된 댓글 정보를 반환")
    @Test
    void givenUpdateArticleCommentCommand_whenUpdating_thenReturnsSavedArticleComment() {
        // given
        var dummy = Optional.of(articleComment(ID, ARTICLE_ID, MEMBER_ID, CONTENT));
        var sut = new UpdateArticleCommentService(id -> dummy, comment -> comment);
        var command = new UpdateArticleCommentCommand(ID, MEMBER_ID, NEW_CONTENT);

        // when
        var result = sut.updateArticleComment(command);

        // then
        assertThat(result, ID, ARTICLE_ID, MEMBER_ID, NEW_CONTENT);
        Assertions.assertThat(result.getUpdatedAt()).isNotNull();
        Assertions.assertThat(result.getDeletedAt()).isNull();
    }

    @DisplayName("[updateArticleComment] 존재하지 않는 댓글 id를 보내면, 예외를 던짐")
    @Test
    void givenWrongArticleId_whenUpdating_thenThrowsException() {
        // given
        var sut = new UpdateArticleCommentService(
                id -> Optional.empty(),
                null
        );
        var command = new UpdateArticleCommentCommand(ID, MEMBER_ID, NEW_CONTENT);

        // when
        var thrown = Assertions.assertThatThrownBy(
                () -> sut.updateArticleComment(command)
        );
        // then
        thrown.isInstanceOf(ArticleCommentNotFoundException.class);
    }

    @DisplayName("[updateArticleComment] 댓글 작성자의 요청이 아니면, 예외를 던짐")
    @Test
    void givenNoArticleTicketAvailable_whenSaving_thenThrowsException() {
        // given
        var dummy = articleComment(ID, ARTICLE_ID, MEMBER_ID, CONTENT);
        var sut = new UpdateArticleCommentService(
                id -> Optional.of(dummy),
                null
        );
        var command = new UpdateArticleCommentCommand(ID, INVALID_MEMBER_ID, NEW_CONTENT);

        // when
        var thrown = Assertions.assertThatThrownBy(
                () -> sut.updateArticleComment(command)
        );
        // then
        thrown.isInstanceOf(UnauthorizedException.class);
    }
}