package com.boldfaced7.board.service;

import com.boldfaced7.board.auth.AuthInfoHolder;
import com.boldfaced7.board.domain.Article;
import com.boldfaced7.board.domain.ArticleComment;
import com.boldfaced7.board.domain.Member;
import com.boldfaced7.board.dto.ArticleDto;
import com.boldfaced7.board.dto.MemberDto;
import com.boldfaced7.board.repository.ArticleCommentRepository;
import com.boldfaced7.board.repository.ArticleRepository;
import com.boldfaced7.board.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.boldfaced7.board.TestUtil.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@DisplayName("ArticleService 테스트")
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @InjectMocks private ArticleService articleService;
    @Mock private ArticleRepository articleRepository;
    @Mock private ArticleCommentRepository articleCommentRepository;
    @Mock private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        AuthInfoHolder.setAuthInfo(createAuthResponse());
    }

    @AfterEach
    void clear() {
        AuthInfoHolder.releaseAuthInfo();
    }

    @DisplayName("[조회] 게시글 목록을 반환")
    @Test
    void givenNothing_whenSearchingArticles_thenReturnsArticles() {
        //Given
        Article article = createArticle();
        given(articleRepository.findAll()).willReturn(List.of(article));

        // When
        List<ArticleDto> articles = articleService.getArticles();

        // Then
        assertThat(articles).isNotEmpty();
        then(articleRepository).should().findAll();
    }

    @DisplayName("[조회] id를 입력하면, 게시글과 관련 댓글 리스트를 반환")
    @Test
    void givenArticleId_whenSearchingArticle_thenReturnsArticle() {
        //Given
        ArticleComment articleComment = createArticleComment();
        Article article = articleComment.getArticle();
        List<ArticleComment> articleComments = List.of(articleComment);

        given(articleRepository.findById(ARTICLE_ID)).willReturn(Optional.of(article));
        given(articleCommentRepository.findAllByArticle(article)).willReturn(articleComments);

        // When
        ArticleDto dto = articleService.getArticle(ARTICLE_ID);

        // Then
        assertThat(dto)
                .hasFieldOrPropertyWithValue("articleId", article.getId())
                .hasFieldOrPropertyWithValue("title", article.getTitle())
                .hasFieldOrPropertyWithValue("content", article.getContent());

        assertThat(dto.getArticleComments().get(0))
                .hasFieldOrPropertyWithValue("articleCommentId", articleComment.getId())
                .hasFieldOrPropertyWithValue("articleId", article.getId())
                .hasFieldOrPropertyWithValue("content", articleComment.getContent());

        then(articleRepository).should().findById(ARTICLE_ID);
        then(articleCommentRepository).should().findAllByArticle(article);
    }

    @DisplayName("[조회] 잘못된 id를 입력하면, 반환 없이 예외를 던짐")
    @Test
    void givenWrongArticleId_whenSearchingArticle_thenThrowsException() {
        //Given
        given(articleRepository.findById(ARTICLE_ID)).willReturn(Optional.empty());

        // When
        Throwable t = catchThrowable(() -> articleService.getArticle(ARTICLE_ID));

        // Then
        assertThat(t)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(ArticleService.NO_ARTICLE_MESSAGE + ARTICLE_ID);
        then(articleRepository).should().findById(ARTICLE_ID);
    }

    @DisplayName("[조회] 회원 id를 입력하면, 해당 회원의 게시글 목록을 반환")
    @Test
    void givenMemberId_whenSearchingArticles_thenReturnsArticles() {
        Article article = createArticle();
        Member member = article.getMember();
        MemberDto memberDto = MemberDto.builder()
                .memberId(member.getId())
                .build();

        given(memberRepository.findById(MEMBER_ID)).willReturn(Optional.of(member));
        given(articleRepository.findAllByMember(member)).willReturn(List.of(article));

        // When
        List<ArticleDto> articles = articleService.getArticles(memberDto);

        // Then
        assertThat(articles).isNotEmpty();
        then(memberRepository).should().findById(MEMBER_ID);
        then(articleRepository).should().findAllByMember(member);

    }

    @DisplayName("[조회] 잘못된 회원 id를 입력하면, 반환 없이 예외를 던짐")
    @Test
    void givenWrongMember_whenSearchingArticles_thenThrowsException() {
        //Given
        Long wrongMemberId = 2L;
        MemberDto memberDto = MemberDto.builder()
                .memberId(wrongMemberId)
                .build();

        given(memberRepository.findById(wrongMemberId)).willReturn(Optional.empty());

        // When
        Throwable t = catchThrowable(() -> articleService.getArticles(memberDto));

        // Then
        assertThat(t)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(ArticleService.NO_MEMBER_MESSAGE + wrongMemberId);
        then(memberRepository).should().findById(wrongMemberId);
    }


    @DisplayName("[저장] 게시글 작성 정보를 입력하면, 게시글을 저장")
    @Test
    void givenArticleInfo_whenSavingArticle_thenSavesArticle() {
        //Given
        ArticleDto dto = createArticleDto();
        dto.setMemberId(MEMBER_ID);
        given(articleRepository.save(dto.toEntityForUpdating())).willReturn(createArticle());
        given(memberRepository.findById(dto.getMemberId())).willReturn(Optional.of(createMember()));

        // When
        Long returnedArticleId = articleService.saveArticle(dto);

        // Then
        assertThat(returnedArticleId).isEqualTo(ARTICLE_ID);
        then(articleRepository).should().save(dto.toEntityForUpdating());
        then(memberRepository).should().findById(dto.getMemberId());
    }

    @DisplayName("[수정] id와 게시글 수정 정보를 입력하면, 게시글을 수정")
    @Test
    void givenArticleIdAndModifiedArticleInfo_whenUpdatingArticle_thenUpdatesArticle() {
        //Given
        Article article = createArticle();

        ArticleDto dto = createArticleDto(ARTICLE_ID);
        dto.setTitle("new title");
        dto.setContent("new content");

        given(articleRepository.findById(ARTICLE_ID)).willReturn(Optional.of(article));

        // When
        articleService.updateArticle(dto);

        // Then
        assertThat(article)
                .hasFieldOrPropertyWithValue("title", dto.getTitle())
                .hasFieldOrPropertyWithValue("content", dto.getContent());
        then(articleRepository).should().findById(ARTICLE_ID);
    }

    @DisplayName("[수정] 잘못된 id를 입력하면, 수정 없이 예외를 던짐")
    @Test
    void givenWrongArticleIdAndModifiedArticleInfo_whenUpdatingArticle_thenThrowsException() {
        //Given
        Long wrongArticleId = 2L;
        ArticleDto dto = createArticleDto(wrongArticleId);

        given(articleRepository.findById(wrongArticleId)).willReturn(Optional.empty());

        // When
        Throwable t = catchThrowable(() -> articleService.updateArticle(dto));

        // Then
        assertThat(t)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(ArticleService.NO_ARTICLE_MESSAGE + wrongArticleId);
        then(articleRepository).should().findById(wrongArticleId);
    }

    @DisplayName("[수정] 잘못된 회원이 접근하면, 반환 없이 예외를 던짐")
    @Test
    void givenWrongMemberAndModifiedArticleInfo_whenUpdatingArticle_thenThrowsException() {
        //Given
        Long wrongMemberId = 2L;
        AuthInfoHolder.setAuthInfo(createAuthResponse(wrongMemberId));

        ArticleDto dto = createArticleDto(ARTICLE_ID);
        dto.setContent("new content");

        given(articleRepository.findById(dto.getArticleId()))
                .willReturn(Optional.of(createArticle()));

        // When
        Throwable t = catchThrowable(() -> articleService.updateArticle(dto));

        // Then
        assertThat(t)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(ArticleService.NO_ARTICLE_MESSAGE + dto.getArticleId());

        then(articleRepository).should().findById(dto.getArticleId());
    }


    @DisplayName("[삭제] id를 입력하면, 댓글을 삭제(soft delete)")
    @Test
    void givenArticleId_whenDeletingArticle_thenDeletesArticleSoftVer() {
        //Given
        Article article = createArticle();
        ArticleDto articleCommentDto = createArticleDto(ARTICLE_ID);

        given(articleRepository.findById(ARTICLE_ID)).willReturn(Optional.of(article));

        // When
        articleService.softDeleteArticle(articleCommentDto);

        // Then
        assertThat(article)
                .hasFieldOrPropertyWithValue("isActive", false);
        then(articleRepository).should().findById(ARTICLE_ID);
    }

    @DisplayName("[삭제] 잘못된 id를 입력하면, 반환 없이 예외를 던짐(soft delete)")
    @Test
    void givenArticleId_whenDeletingArticle_thenThrowsExceptionSoftVer() {
        //Given
        Long wrongArticleId = 2L;
        ArticleDto dto = createArticleDto(wrongArticleId);

        given(articleRepository.findById(wrongArticleId)).willReturn(Optional.empty());

        // When
        Throwable t = catchThrowable(() -> articleService.softDeleteArticle(dto));

        // Then
        assertThat(t)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(ArticleService.NO_ARTICLE_MESSAGE + wrongArticleId);

        then(articleRepository).should().findById(wrongArticleId);
    }

    @DisplayName("[삭제] 잘못된 회원이 접근하면, 반환 없이 예외를 던짐(soft delete)")
    @Test
    void givenWrongMemberAndModifiedArticleInfo_whenDeletingArticle_thenThrowsExceptionSoftVer() {
        //Given
        Long wrongMemberId = 2L;
        AuthInfoHolder.setAuthInfo(createAuthResponse(wrongMemberId));

        ArticleDto dto = createArticleDto(ARTICLE_ID);
        dto.setContent("new content");

        given(articleRepository.findById(dto.getArticleId()))
                .willReturn(Optional.of(createArticle()));

        // When
        Throwable t = catchThrowable(() -> articleService.softDeleteArticle(dto));

        // Then
        assertThat(t)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(ArticleService.NO_ARTICLE_MESSAGE + dto.getArticleId());

        then(articleRepository).should().findById(dto.getArticleId());
    }

    @DisplayName("[삭제] id를 입력하면, 댓글을 삭제(hard delete)")
    @Test
    void givenArticleId_whenDeletingArticle_thenDeletesArticleHardVer() {
        //Given
        Article article = createArticle();
        ArticleDto articleCommentDto = createArticleDto(ARTICLE_ID);

        given(articleRepository.findById(ARTICLE_ID)).willReturn(Optional.of(article));
        willDoNothing().given(articleRepository).delete(article);

        // When
        articleService.hardDeleteArticle(articleCommentDto);

        // Then
        then(articleRepository).should().findById(ARTICLE_ID);
        then(articleRepository).should().delete(article);
    }

    @DisplayName("[삭제] 잘못된 id를 입력하면, 반환 없이 예외를 던짐(hard delete)")
    @Test
    void givenArticleId_whenDeletingArticle_thenThrowsExceptionHardVer() {
        //Given
        Long wrongArticleId = 2L;
        ArticleDto dto = createArticleDto(wrongArticleId);

        given(articleRepository.findById(dto.getArticleId())).willReturn(Optional.empty());

        // When
        Throwable t = catchThrowable(() -> articleService.hardDeleteArticle(dto));

        // Then
        assertThat(t)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(ArticleService.NO_ARTICLE_MESSAGE + wrongArticleId);
        then(articleRepository).should().findById(dto.getArticleId());
    }

    @DisplayName("[삭제] 잘못된 회원이 접근하면, 반환 없이 예외를 던짐(hard delete)")
    @Test
    void givenWrongMemberAndModifiedArticleInfo_whenDeletingArticle_thenThrowsExceptionHardVer() {
        //Given
        Long wrongMemberId = 2L;
        AuthInfoHolder.setAuthInfo(createAuthResponse(wrongMemberId));

        ArticleDto dto = createArticleDto(ARTICLE_ID);
        dto.setContent("new content");

        given(articleRepository.findById(dto.getArticleId()))
                .willReturn(Optional.of(createArticle()));

        // When
        Throwable t = catchThrowable(() -> articleService.hardDeleteArticle(dto));

        // Then
        assertThat(t)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(ArticleService.NO_ARTICLE_MESSAGE + dto.getArticleId());

        then(articleRepository).should().findById(dto.getArticleId());
    }

    /*
    @DisplayName("[] ")
    @Test
    void given_when_then() {
        //Given


        // When
        articleService.

        // Then

    }
     */
}