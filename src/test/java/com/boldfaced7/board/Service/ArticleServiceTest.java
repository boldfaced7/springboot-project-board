package com.boldfaced7.board.Service;

import com.boldfaced7.board.domain.Article;
import com.boldfaced7.board.dto.ArticleDto;
import com.boldfaced7.board.repository.ArticleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@DisplayName("ArticleService 테스트")
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @InjectMocks private ArticleService articleService;
    @Mock private ArticleRepository articleRepository;

    @DisplayName("[조회] 게시글 목록을 반환")
    @Test
    void givenNothing_whenSearchingArticles_thenReturnsArticles() {
        //Given
        Long articleId = 1L;
        Article article = createArticle(articleId);
        given(articleRepository.findAll()).willReturn(List.of(article));

        // When
        List<ArticleDto> articles = articleService.getArticles();

        // Then
        assertThat(articles).isNotEmpty();
        then(articleRepository).should().findAll();

    }

    @DisplayName("[조회] id를 입력하면, 게시글을 반환")
    @Test
    void givenArticleId_whenSearchingArticle_thenReturnsArticle() {
        //Given
        Long articleId = 1L;
        Article article = createArticle(articleId);
        given(articleRepository.findById(articleId)).willReturn(Optional.of(article));

        // When
        ArticleDto dto = articleService.getArticle(articleId);

        // Then
        assertThat(dto)
                .hasFieldOrPropertyWithValue("title", article.getTitle())
                .hasFieldOrPropertyWithValue("content", article.getContent());
        then(articleRepository).should().findById(articleId);
    }

    @DisplayName("[조회] 잘못된 id를 입력하면, 반환 없이 예외를 던짐")
    @Test
    void givenWrongArticleId_whenSearchingArticle_thenThrowsException() {
        //Given
        Long articleId = 1L;
        given(articleRepository.findById(articleId)).willReturn(Optional.empty());

        // When
        Throwable t = catchThrowable(() -> articleService.getArticle(articleId));

        // Then
        assertThat(t)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("게시글이 없습니다 - articleId: " + articleId);
        then(articleRepository).should().findById(articleId);
    }

    @DisplayName("[저장] 게시글 작성 정보를 입력하면, 게시글을 저장")
    @Test
    void givenArticleInfo_whenSavingArticle_thenSavesArticle() {
        //Given
        Long articleId = 1L;
        ArticleDto dto = createArticleDto();
        given(articleRepository.save(any(Article.class))).willReturn(createArticle(1L));

        // When
        Long returnedArticleId = articleService.saveArticle(dto);

        // Then
        assertThat(returnedArticleId).isEqualTo(articleId);
        then(articleRepository).should().save(any(Article.class));
    }

    @DisplayName("[수정] id와 게시글 수정 정보를 입력하면, 게시글을 수정")
    @Test
    void givenArticleIdAndModifiedArticleInfo_whenUpdatingArticle_thenUpdatesArticle() {
        //Given
        Long articleId = 1L;
        Article article = createArticle(articleId);
        ArticleDto dto = createArticleDto("new title", "new content", "new boldfaced7");
        given(articleRepository.findById(articleId)).willReturn(Optional.of(article));

        // When
        articleService.updateArticle(articleId, dto);

        // Then
        assertThat(article)
                .hasFieldOrPropertyWithValue("title", dto.getTitle())
                .hasFieldOrPropertyWithValue("content", dto.getContent());
        then(articleRepository).should().findById(articleId);
    }

    @DisplayName("[수정] 잘못된 id를 입력하면, 수정 없이 예외를 던짐")
    @Test
    void givenWrongArticleIdAndModifiedArticleInfo_whenUpdatingArticle_thenThrowsException() {
        //Given
        Long articleId = 1L;
        ArticleDto dto = createArticleDto();
        given(articleRepository.findById(articleId)).willReturn(Optional.empty());

        // When
        Throwable t = catchThrowable(() -> articleService.updateArticle(articleId, dto));

        // Then
        assertThat(t)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("게시글이 없습니다 - articleId: " + articleId);
        then(articleRepository).should().findById(articleId);
    }

    @DisplayName("[삭제] id를 입력하면, 게시글을 삭제(soft delete)")
    @Test
    void givenArticleId_whenDeletingArticle_thenDeletesArticleSoftVer() {
        //Given
        Long articleId = 1L;
        Article article = createArticle(articleId);
        given(articleRepository.findById(articleId)).willReturn(Optional.of(article));

        // When
        articleService.softDeleteArticle(articleId);

        // Then
        assertThat(article)
                .hasFieldOrPropertyWithValue("isActive", false);
        then(articleRepository).should().findById(articleId);
    }

    @DisplayName("[삭제] 잘못된 id를 입력하면, 삭제(soft delete) 없이 예외를 던짐")
    @Test
    void givenWrongArticleId_whenDeletingArticle_thenThrowsExceptionSoftVer() {
        //Given
        Long articleId = 1L;
        given(articleRepository.findById(articleId)).willReturn(Optional.empty());

        // When
        Throwable t = catchThrowable(() -> articleService.softDeleteArticle(articleId));

        // Then
        assertThat(t)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("게시글이 없습니다 - articleId: " + articleId);
        then(articleRepository).should().findById(articleId);
    }


    @DisplayName("[삭제] id를 입력하면, 게시글을 삭제(hard delete)")
    @Test
    void givenArticleId_whenDeletingArticle_thenDeletesArticleHardVer() {
        //Given
        Long articleId = 1L;
        Article article = createArticle(articleId);
        given(articleRepository.findById(articleId)).willReturn(Optional.of(article));
        willDoNothing().given(articleRepository).delete(article);

        // When
        articleService.hardDeleteArticle(articleId);

        // Then
        then(articleRepository).should().findById(articleId);
        then(articleRepository).should().delete(article);
    }

    @DisplayName("[삭제] 잘못된 id를 입력하면, 삭제(hard delete) 없이 예외를 던짐")
    @Test
    void givenWrongArticleId_whenDeletingArticle_thenThrowsExceptionHardVer() {
        //Given
        Long articleId = 1L;
        given(articleRepository.findById(articleId)).willReturn(Optional.empty());

        // When
        Throwable t = catchThrowable(() -> articleService.hardDeleteArticle(articleId));

        // Then
        assertThat(t)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("게시글이 없습니다 - articleId: " + articleId);
        then(articleRepository).should().findById(articleId);
    }

    /*
    @DisplayName("")
    @Test
    void given_when_then() {
        //Given


        // When
        articleRepository.

        // Then

    }
     */

    private Article createArticle(Long id) {
        Article article = Article.builder()
                .title("title")
                .content("content")
                .build();

        ReflectionTestUtils.setField(article, "id", id);

        return article;
    }

    private ArticleDto createArticleDto() {
        return ArticleDto.builder()
                .title("title")
                .content("content")
                .author("boldfaced7")
                .build();
    }
    private ArticleDto createArticleDto(String title, String content, String author) {
        return ArticleDto.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();
    }
}