package com.boldfaced7.board.repository;

import com.boldfaced7.board.config.JpaConfig;
import com.boldfaced7.board.domain.Article;
import com.boldfaced7.board.domain.ArticleComment;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("ArticleCommentRepository 테스트")
@Import(JpaConfig.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ArticleCommentRepositoryTest {

    @Autowired private ArticleCommentRepository articleCommentRepository;
    @Autowired private ArticleRepository articleRepository;

    @DisplayName("findById()가 isActive가 true인 ArticleComment 객체를 반환하는지 확인")
    @Test
    void givenArticleComment_whenSelecting_thenWorksFine() {
        // Given
        ArticleComment articleComment = articleCommentRepository.save(createArticleComment());

        // When
        ArticleComment foundComment = articleCommentRepository.findById(articleComment.getId()).get();

        // Then
        assertThat(foundComment.isActive()).isTrue();
    }

    @DisplayName("findById()가 isActive가 false인 ArticleComment 객체를 반환하지 않는지 확인")
    @Test
    void givenDeletedArticleComment_whenSelecting_thenWorksFine() {
        // Given
        ArticleComment articleComment = articleCommentRepository.save(createArticleComment());
        articleComment.deactivate();

        // When
        Throwable t = catchThrowable(() ->
                articleCommentRepository.findById(articleComment.getId())
                .orElseThrow(EntityNotFoundException::new));

        // Then
        assertThat(t).isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("findAll()이 isActive가 true인 ArticleComment 객체를 반환하는지 확인")
    @Test
    void givenArticleComment_whenSelectingAll_thenWorksFine() {
        // Given
        ArticleComment articleComment = articleCommentRepository.save(createArticleComment());

        // When
        List<ArticleComment> articleComments = articleCommentRepository.findAll();

        // Then
        assertThat(articleComments.get(0).isActive()).isTrue();

    }
    @DisplayName("findAll()이 isActive가 false인 ArticleComment 객체를 반환하지 않는지 확인")
    @Test
    void givenDeletedArticleComment_whenSelectingAll_thenWorksFine() {
        // Given
        ArticleComment articleComment = articleCommentRepository.save(createArticleComment());
        articleComment.deactivate();

        // When
        List<ArticleComment> articleComments = articleCommentRepository.findAll();

        // Then
        assertThat(articleComments).isEmpty();
    }

    @DisplayName("findAllByArticle()이 isActive가 true인 ArticleComment 객체를 반환하는지 확인")
    @Test
    void givenArticleAndArticleComment_whenSelectingArticleCommentsOfArticle_thenWorksFine() {
        //Given
        Article article = articleRepository.save(createArticle());
        ArticleComment articleComment = articleCommentRepository.save(createArticleComment(article));

        // When
        List<ArticleComment> articleComments = articleCommentRepository.findAllByArticle(article);

        // Then
        assertThat(articleComments.get(0).isActive()).isTrue();
    }

    @DisplayName("findAllByArticle()이 isActive가 false인 ArticleComment 객체를 반환하지 않는지 확인")
    @Test
    void givenArticleAndDeletedArticleComment_whenSelectingArticleCommentsOfArticle_thenWorksFine() {
        //Given
        Article article = articleRepository.save(createArticle());
        ArticleComment articleComment = articleCommentRepository.save(createArticleComment(article));
        articleComment.deactivate();

        // When
        List<ArticleComment> articleComments = articleCommentRepository.findAllByArticle(article);

        // Then
        assertThat(articleComments).isEmpty();
    }

    /*
    @DisplayName("")
    @Test
    void given_when_then() {
        //Given


        // When
        articleCommentRepository.

        // Then

    }
     */

    private ArticleComment createArticleComment() {
        return ArticleComment.builder()
                .content("content")
                .build();
    }

    private ArticleComment createArticleComment(Article article) {
        return ArticleComment.builder()
                .content("content")
                .article(article)
                .build();
    }

    private Article createArticle() {
        return Article.builder()
                .title("title")
                .content("content")
                .build();

    }
}