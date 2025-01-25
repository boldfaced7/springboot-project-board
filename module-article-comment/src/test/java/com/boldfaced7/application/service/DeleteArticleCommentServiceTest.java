package com.boldfaced7.application.service;

import com.boldfaced7.application.port.in.DeleteArticleCommentCommand;
import com.boldfaced7.exception.articlecomment.ArticleCommentNotFoundException;
import com.boldfaced7.exception.auth.UnauthorizedException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.boldfaced7.ArticleCommentTestUtil.*;

class DeleteArticleCommentServiceTest {

    @DisplayName("[deleteArticleComment] 댓글 작성 정보를 보내면, 삭제된 댓글 정보를 반환")
    @Test
    void givenUpdateArticleCommentCommand_whenUpdating_thenReturnsSavedArticleComment() {
        // given
        var dummy = Optional.of(articleComment(ID, ARTICLE_ID, MEMBER_ID, CONTENT));
        var sut = new DeleteArticleCommentService(id -> dummy, comment -> comment);
        var command = new DeleteArticleCommentCommand(ID, MEMBER_ID);

        // when
        var result = sut.deleteArticleComment(command);

        // then
        assertThat(result, ID, ARTICLE_ID, MEMBER_ID, CONTENT);
        Assertions.assertThat(result.getDeletedAt()).isNotNull();
    }
    
    @DisplayName("[deleteArticleComment] 존재하지 않는 댓글 id를 보내면, 예외를 던짐")
    @Test
    void givenWrongArticleId_whenUpdating_thenThrowsException() {
        // given
        var sut = new DeleteArticleCommentService(id -> Optional.empty(), null);
        var command = new DeleteArticleCommentCommand(ID, MEMBER_ID);

        // when
        var thrown = Assertions.assertThatThrownBy(
                () -> sut.deleteArticleComment(command)
        );
        // then
        thrown.isInstanceOf(ArticleCommentNotFoundException.class);
    }

    @DisplayName("[deleteArticleComment] 댓글 작성자의 요청이 아니면, 예외를 던짐")
    @Test
    void givenNoArticleTicketAvailable_whenSaving_thenThrowsException() {
        // given
        var dummy = Optional.of(articleComment(ID, ARTICLE_ID, MEMBER_ID, CONTENT));
        var sut = new DeleteArticleCommentService(id -> dummy, null);
        var command = new DeleteArticleCommentCommand(ID, INVALID_MEMBER_ID);

        // when
        var thrown = Assertions.assertThatThrownBy(
                () -> sut.deleteArticleComment(command)
        );
        // then
        thrown.isInstanceOf(UnauthorizedException.class);
    }
}