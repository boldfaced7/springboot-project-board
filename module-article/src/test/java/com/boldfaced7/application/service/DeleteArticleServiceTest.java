package com.boldfaced7.application.service;

import com.boldfaced7.application.port.in.DeleteArticleCommand;
import com.boldfaced7.exception.article.ArticleNotFoundException;
import com.boldfaced7.exception.auth.UnauthorizedException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.boldfaced7.ArticleTestUtil.*;

class DeleteArticleServiceTest {

    @DisplayName("[deleteArticle] 게시글 작성 정보를 보내면, 삭제된 게시글 정보를 반환")
    @Test
    void givenUpdateArticleCommand_whenUpdating_thenReturnsSavedArticle() {
        // given
        var dummy = Optional.of(article(ID, MEMBER_ID, TITLE, CONTENT));
        var sut = new DeleteArticleService(id -> dummy, article -> article);
        var command = new DeleteArticleCommand(ID, MEMBER_ID);

        // when
        var result = sut.deleteArticle(command);

        // then
        assertThat(result, ID, MEMBER_ID, TITLE, CONTENT);
        Assertions.assertThat(result.getDeletedAt()).isNotNull();
    }
    
    @DisplayName("[deleteArticle] 존재하지 않는 게시글 id를 보내면, 예외를 던짐")
    @Test
    void givenWrongArticleId_whenUpdating_thenThrowsException() {
        // given
        var sut = new DeleteArticleService(id -> Optional.empty(), null);
        var command = new DeleteArticleCommand(ID, MEMBER_ID);

        // when
        var thrown = Assertions.assertThatThrownBy(
                () -> sut.deleteArticle(command)
        );
        // then
        thrown.isInstanceOf(ArticleNotFoundException.class);
    }

    @DisplayName("[deleteArticle] 게시글 작성자의 요청이 아니면, 예외를 던짐")
    @Test
    void givenNoArticleTicketAvailable_whenSaving_thenThrowsException() {
        // given
        var dummy = Optional.of(article(ID, MEMBER_ID, TITLE, CONTENT));
        var sut = new DeleteArticleService(id -> dummy, null);
        var command = new DeleteArticleCommand(ID, INVALID_MEMBER_ID);

        // when
        var thrown = Assertions.assertThatThrownBy(
                () -> sut.deleteArticle(command)
        );
        // then
        thrown.isInstanceOf(UnauthorizedException.class);
    }
}