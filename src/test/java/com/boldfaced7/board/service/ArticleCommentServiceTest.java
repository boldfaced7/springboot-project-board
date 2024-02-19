package com.boldfaced7.board.service;

import com.boldfaced7.board.domain.Article;
import com.boldfaced7.board.domain.ArticleComment;
import com.boldfaced7.board.dto.ArticleCommentDto;
import com.boldfaced7.board.repository.ArticleCommentRepository;
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
import static org.mockito.BDDMockito.*;

@DisplayName("ArticleCommentService 테스트")
@ExtendWith(MockitoExtension.class)
class ArticleCommentServiceTest {

    @InjectMocks private ArticleCommentService articleCommentService;
    @Mock private ArticleCommentRepository articleCommentRepository;
    @Mock private ArticleRepository articleRepository;

    @DisplayName("[조회] 댓글 목록을 반환")
    @Test
    void givenNothing_whenSearchingArticleComments_thenReturnsArticleComments() {
        //Given
        Long articleId = 1L;
        Article article = createArticle(articleId);

        Long articleCommentId = 1L;
        ArticleComment articleComment = createArticleComment(articleCommentId, article);
        given(articleCommentRepository.findAll()).willReturn(List.of(articleComment));

        // When
        List<ArticleCommentDto> articleComments = articleCommentService.getArticleComments();

        // Then
        assertThat(articleComments).isNotEmpty();
        then(articleCommentRepository).should().findAll();
    }

    @DisplayName("[조회] 게시글 id를 입력하면, 해당 게시글의 댓글 목록을 반환")
    @Test
    void givenArticleId_whenSearchingArticleComments_thenReturnsArticleComments() {
        //Given
        Long articleId = 1L;
        Article article = createArticle(articleId);

        Long articleCommentId = 1L;
        ArticleComment articleComment = createArticleComment(articleCommentId, article);

        given(articleRepository.findById(articleId)).willReturn(Optional.of(article));
        given(articleCommentRepository.findAllByArticle(article)).willReturn(List.of(articleComment));

        // When
        List<ArticleCommentDto> articleComments = articleCommentService.getArticleComments(articleId);

        // Then
        assertThat(articleComments).isNotEmpty();
        then(articleRepository).should().findById(articleId);
        then(articleCommentRepository).should().findAllByArticle(article);
    }

    @DisplayName("[조회] 잘못된 게시글 id를 입력하면, 반환 없이 예외를 던짐")
    @Test
    void givenWrongArticleId_whenSearchingArticleComments_thenThrowsException() {
        //Given
        Long articleId = 1L;
        Long wrongArticleId = 2L;
        Article article = createArticle(articleId);

        given(articleRepository.findById(wrongArticleId)).willReturn(Optional.empty());

        // When
        Throwable t = catchThrowable(() -> articleCommentService.getArticleComments(wrongArticleId));

        // Then
        assertThat(wrongArticleId).isNotEqualTo(articleId);
        assertThat(t)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("게시글이 없습니다 - articleId: " + wrongArticleId);
        then(articleRepository).should().findById(wrongArticleId);
    }

    @DisplayName("[조회] id를 입력하면, 댓글을 반환")
    @Test
    void givenArticleCommentId_whenSearchingArticleComment_thenReturnsArticleComment() {
        //Given
        Long articleId = 1L;
        Article article = createArticle(articleId);

        Long articleCommentId = 1L;
        ArticleComment articleComment = createArticleComment(articleCommentId, article);

        given(articleCommentRepository.findById(articleCommentId)).willReturn(Optional.of(articleComment));

        // When
        ArticleCommentDto dto = articleCommentService.getArticleComment(articleCommentId);

        // Then
        assertThat(dto)
                .hasFieldOrPropertyWithValue("content", articleComment.getContent());
        then(articleCommentRepository).should().findById(articleCommentId);
    }

    @DisplayName("[조회] 잘못된 id를 입력하면, 반환 없이 예외를 던짐")
    @Test
    void givenWrongArticleCommentId_whenSearchingArticleComment_thenThrowsException() {
        //Given
        Long wrongArticleCommentId = 1L;
        given(articleCommentRepository.findById(wrongArticleCommentId)).willReturn(Optional.empty());

        // When
        Throwable t = catchThrowable(() -> articleCommentService.getArticleComment(wrongArticleCommentId));

        // Then
        assertThat(t)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("댓글이 없습니다 - articleCommentId: " + wrongArticleCommentId);

        then(articleCommentRepository).should().findById(wrongArticleCommentId);
    }

    @DisplayName("[저장] 댓글 작성 정보를 입력하면, 댓글을 저장")
    @Test
    void givenArticleCommentInfo_whenSavingArticleComment_thenSavesArticleComment() {
        //Given
        Long articleId = 1L;
        Article article = createArticle(articleId);

        Long articleCommentId = 1L;
        ArticleCommentDto dto = createArticleCommentDto(articleId);

        given(articleRepository.findById(articleId)).willReturn(Optional.of(article));
        given(articleCommentRepository.save(any(ArticleComment.class)))
                .willReturn(createArticleComment(1L, article));

        // When
        Long returnedArticleCommentId = articleCommentService.saveArticleComment(dto);

        // Then
        assertThat(returnedArticleCommentId).isEqualTo(articleCommentId);
        then(articleRepository).should().findById(articleId);
        then(articleCommentRepository).should().save(any(ArticleComment.class));
    }

    @DisplayName("id와 댓글 수정 정보를 입력하면, 댓글을 수정")
    @Test
    void givenArticleCommentIdAndModifiedArticleCommentInfo_whenUpdatingArticleComment_thenUpdatesArticleComment() {
        //Given
        Long articleCommentId = 1L;
        ArticleComment articleComment = createArticleComment(articleCommentId);
        ArticleCommentDto dto = createArticleCommentDto("new content");
        given(articleCommentRepository.findById(articleCommentId)).willReturn(Optional.of(articleComment));

        // When
        articleCommentService.updateArticleComment(articleCommentId, dto);

        // Then
        assertThat(articleComment)
                .hasFieldOrPropertyWithValue("content", dto.getContent());
        then(articleCommentRepository).should().findById(articleCommentId);
    }

    @DisplayName("[수정] 잘못된 id를 입력하면, 반환 없이 예외를 던짐")
    @Test
    void givenWrongArticleCommentIdAndModifiedArticleCommentInfo_whenUpdatingArticleComment_thenThrowsException() {
        //Given
        Long wrongArticleCommentId = 1L;
        ArticleCommentDto dto = createArticleCommentDto("new content");
        given(articleCommentRepository.findById(wrongArticleCommentId)).willReturn(Optional.empty());

        // When
        Throwable t = catchThrowable(() -> articleCommentService.updateArticleComment(wrongArticleCommentId, dto));

        // Then
        assertThat(t)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("댓글이 없습니다 - articleCommentId: " + wrongArticleCommentId);

        then(articleCommentRepository).should().findById(wrongArticleCommentId);
    }


    @DisplayName("[삭제] id를 입력하면, 댓글을 삭제(soft delete)")
    @Test
    void givenArticleCommentId_whenDeletingArticleComment_thenDeletesArticleCommentSoftVer() {
        //Given
        Long articleCommentId = 1L;
        ArticleComment articleComment = createArticleComment(articleCommentId);
        given(articleCommentRepository.findById(articleCommentId)).willReturn(Optional.of(articleComment));

        // When
        articleCommentService.softDeleteArticleComment(articleCommentId);

        // Then
        assertThat(articleComment)
                .hasFieldOrPropertyWithValue("isActive", false);
        then(articleCommentRepository).should().findById(articleCommentId);
    }

    @DisplayName("[삭제] 잘못된 id를 입력하면, 반환 없이 예외를 던짐(soft delete)")
    @Test
    void givenArticleCommentId_whenDeletingArticleComment_thenThrowsExceptionSoftVer() {
        //Given
        Long wrongArticleCommentId = 1L;
        given(articleCommentRepository.findById(wrongArticleCommentId)).willReturn(Optional.empty());

        // When
        Throwable t = catchThrowable(() -> articleCommentService.softDeleteArticleComment(wrongArticleCommentId));

        // Then
        assertThat(t)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("댓글이 없습니다 - articleCommentId: " + wrongArticleCommentId);

        then(articleCommentRepository).should().findById(wrongArticleCommentId);
    }

    @DisplayName("[삭제] id를 입력하면, 댓글을 삭제(hard delete)")
    @Test
    void givenArticleCommentId_whenDeletingArticleComment_thenDeletesArticleCommentHardVer() {
        //Given
        Long articleCommentId = 1L;
        ArticleComment articleComment = createArticleComment(articleCommentId);
        given(articleCommentRepository.findById(articleCommentId)).willReturn(Optional.of(articleComment));
        willDoNothing().given(articleCommentRepository).delete(articleComment);

        // When
        articleCommentService.hardDeleteArticleComment(articleCommentId);

        // Then
        then(articleCommentRepository).should().findById(articleCommentId);
        then(articleCommentRepository).should().delete(articleComment);
    }

    @DisplayName("[삭제] 잘못된 id를 입력하면, 반환 없이 예외를 던짐(hard delete)")
    @Test
    void givenArticleCommentId_whenDeletingArticleComment_thenThrowsExceptionHardVer() {
        //Given
        Long wrongArticleCommentId = 1L;
        given(articleCommentRepository.findById(wrongArticleCommentId)).willReturn(Optional.empty());

        // When
        Throwable t = catchThrowable(() -> articleCommentService.hardDeleteArticleComment(wrongArticleCommentId));

        // Then
        assertThat(t)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("댓글이 없습니다 - articleCommentId: " + wrongArticleCommentId);

        then(articleCommentRepository).should().findById(wrongArticleCommentId);
    }
    /*
    @DisplayName("")
    @Test
    void given_when_then() {
        //Given


        // When
        articleCommentService.

        // Then

    }
     */

    private ArticleComment createArticleComment(Long id) {
        ArticleComment articleComment = ArticleComment.builder()
                .content("content")
                .build();

        ReflectionTestUtils.setField(articleComment, "id", id);
        return articleComment;
    }

    private ArticleComment createArticleComment(Long id, Article article) {
        ArticleComment articleComment = ArticleComment.builder()
                .content("content")
                .article(article)
                .build();

        ReflectionTestUtils.setField(articleComment, "id", id);
        return articleComment;
    }

    private Article createArticle(Long id) {
        Article article = Article.builder()
                .title("title")
                .content("content")
                .build();

        ReflectionTestUtils.setField(article, "id", id);

        return article;
    }

    private ArticleCommentDto createArticleCommentDto() {
        return ArticleCommentDto.builder()
                .content("content")
                .author("boldfaced7")
                .build();
    }

    private ArticleCommentDto createArticleCommentDto(Long articleId) {
        return ArticleCommentDto.builder()
                .content("content")
                .author("boldfaced7")
                .articleId(articleId)
                .build();
    }

    private ArticleCommentDto createArticleCommentDto(String content) {
        return ArticleCommentDto.builder()
                .content(content)
                .author("boldfaced7")
                .build();
    }
}