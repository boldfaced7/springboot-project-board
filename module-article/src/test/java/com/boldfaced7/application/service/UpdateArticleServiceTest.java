package com.boldfaced7.application.service;

import com.boldfaced7.application.port.in.UpdateArticleCommand;
import com.boldfaced7.exception.article.ArticleNotFoundException;
import com.boldfaced7.exception.auth.UnauthorizedException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.boldfaced7.ArticleTestUtil.*;

class UpdateArticleServiceTest {

    @DisplayName("[updateArticle] 게시글 작성 정보를 보내면, 수정된 게시글 정보를 반환")
    @Test
    void givenUpdateArticleCommand_whenUpdating_thenReturnsSavedArticle() {
        // given
        var dummy = Optional.of(article(ID, MEMBER_ID, TITLE, CONTENT));
        var sut = new UpdateArticleService(id -> dummy, article -> article);
        var command = new UpdateArticleCommand(ID, MEMBER_ID, NEW_TITLE, NEW_CONTENT);

        // when
        var result = sut.updateArticle(command);

        // then
        assertThat(result, ID, MEMBER_ID, NEW_TITLE, NEW_CONTENT);
        Assertions.assertThat(result.getUpdatedAt()).isNotNull();
        Assertions.assertThat(result.getDeletedAt()).isNull();
    }

    @DisplayName("[updateArticle] 존재하지 않는 게시글 id를 보내면, 예외를 던짐")
    @Test
    void givenWrongArticleId_whenUpdating_thenThrowsException() {
        // given
        var sut = new UpdateArticleService(id -> Optional.empty(), null);
        var command = new UpdateArticleCommand(ID, MEMBER_ID, TITLE, CONTENT);

        // when
        var thrown = Assertions.assertThatThrownBy(
                () -> sut.updateArticle(command)
        );
        // then
        thrown.isInstanceOf(ArticleNotFoundException.class);
    }

    @DisplayName("[updateArticle] 게시글 작성자의 요청이 아니면, 예외를 던짐")
    @Test
    void givenNoArticleTicketAvailable_whenSaving_thenThrowsException() {
        // given
        var dummy = article(ID, MEMBER_ID, TITLE, CONTENT);
        var sut = new UpdateArticleService(id -> Optional.of(dummy), null);
        var command = new UpdateArticleCommand(ID, INVALID_MEMBER_ID, TITLE, CONTENT);

        // when
        var thrown = Assertions.assertThatThrownBy(
                () -> sut.updateArticle(command)
        );
        // then
        thrown.isInstanceOf(UnauthorizedException.class);
    }
}