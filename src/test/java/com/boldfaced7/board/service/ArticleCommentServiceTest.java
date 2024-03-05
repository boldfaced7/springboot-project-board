package com.boldfaced7.board.service;

import com.boldfaced7.board.auth.AuthInfoHolder;
import com.boldfaced7.board.domain.Article;
import com.boldfaced7.board.domain.ArticleComment;
import com.boldfaced7.board.domain.Member;
import com.boldfaced7.board.dto.ArticleCommentDto;
import com.boldfaced7.board.dto.ArticleDto;
import com.boldfaced7.board.dto.MemberDto;
import com.boldfaced7.board.error.ErrorCode;
import com.boldfaced7.board.error.exception.auth.ForbiddenException;
import com.boldfaced7.board.error.exception.articlecomment.ArticleCommentNotFoundException;
import com.boldfaced7.board.error.exception.article.ArticleNotFoundException;
import com.boldfaced7.board.repository.ArticleCommentRepository;
import com.boldfaced7.board.repository.ArticleRepository;
import com.boldfaced7.board.repository.MemberRepository;
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

@DisplayName("ArticleCommentService 테스트")
@ExtendWith(MockitoExtension.class)
class ArticleCommentServiceTest {

    @InjectMocks private ArticleCommentService articleCommentService;
    @Mock private ArticleCommentRepository articleCommentRepository;
    @Mock private ArticleRepository articleRepository;
    @Mock private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        AuthInfoHolder.setAuthInfo(createAuthResponse());
    }

    @AfterEach
    void clear() {
        AuthInfoHolder.releaseAuthInfo();
    }

    @DisplayName("[조회] 댓글 목록을 반환")
    @Test
    void givenNothing_whenSearchingArticleComments_thenReturnsArticleComments() {
        //Given
        ArticleComment articleComment = createArticleComment();
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
        ArticleComment articleComment = createArticleComment();
        Article article = articleComment.getArticle();
        ArticleDto articleDto = new ArticleDto(ARTICLE_ID);

        given(articleRepository.findById(articleDto.getArticleId())).willReturn(Optional.of(article));
        given(articleCommentRepository.findAllByArticle(article)).willReturn(List.of(articleComment));

        // When
        List<ArticleCommentDto> articleComments = articleCommentService.getArticleComments(articleDto);

        // Then
        assertThat(articleComments).isNotEmpty();
        then(articleRepository).should().findById(ARTICLE_ID);
        then(articleCommentRepository).should().findAllByArticle(article);
    }

    @DisplayName("[조회] 잘못된 게시글 id를 입력하면, 반환 없이 예외를 던짐")
    @Test
    void givenWrongArticleId_whenSearchingArticleComments_thenThrowsException() {
        //Given
        Long wrongArticleId = 2L;
        ArticleDto articleDto = new ArticleDto(wrongArticleId);

        given(articleRepository.findById(articleDto.getArticleId())).willReturn(Optional.empty());

        // When
        Throwable t = catchThrowable(() -> articleCommentService.getArticleComments(articleDto));

        // Then
        assertThat(wrongArticleId).isNotEqualTo(ARTICLE_ID);
        assertThat(t)
                .isInstanceOf(ArticleNotFoundException.class)
                .hasMessage(ErrorCode.ARTICLE_NOT_FOUND.getMessage());

        then(articleRepository).should().findById(wrongArticleId);
    }

    @DisplayName("[조회] 회원이 접근하면, 해당 회원의 댓글 목록을 반환")
    @Test
    void givenMemberId_whenSearchingArticleComments_thenReturnsArticleComments() {
        ArticleComment articleComment = createArticleComment();
        Member member = articleComment.getMember();
        MemberDto memberDto = new MemberDto(MEMBER_ID);

        given(memberRepository.findById(memberDto.getMemberId())).willReturn(Optional.of(member));
        given(articleCommentRepository.findAllByMember(member)).willReturn(List.of(articleComment));

        // When
        List<ArticleCommentDto> articleComments = articleCommentService.getArticleComments(memberDto);

        // Then
        assertThat(articleComments).isNotEmpty();
        then(memberRepository).should().findById(MEMBER_ID);
        then(articleCommentRepository).should().findAllByMember(member);

    }

    @DisplayName("[조회] 잘못된 회원이 접근하면, 반환 없이 예외를 던짐")
    @Test
    void givenWrongMember_whenSearchingArticleComments_thenThrowsException() {
        //Given
        Long wrongMemberId = 2L;
        AuthInfoHolder.setAuthInfo(createAuthResponse(wrongMemberId));

        MemberDto memberDto = new MemberDto(MEMBER_ID);

        // When
        Throwable t = catchThrowable(() -> articleCommentService.getArticleComments(memberDto));

        // Then
        assertThat(wrongMemberId).isNotEqualTo(MEMBER_ID);
        assertThat(t)
                .isInstanceOf(ForbiddenException.class)
                .hasMessage(ErrorCode.FORBIDDEN.getMessage());

    }

    @DisplayName("[조회] id를 입력하면, 댓글을 반환")
    @Test
    void givenArticleCommentId_whenSearchingArticleComment_thenReturnsArticleComment() {
        //Given
        ArticleComment articleComment = createArticleComment();

        given(articleCommentRepository.findById(articleComment.getId())).willReturn(Optional.of(articleComment));

        // When
        ArticleCommentDto dto = articleCommentService.getArticleComment(ARTICLE_COMMENT_ID);

        // Then
        assertThat(dto)
                .hasFieldOrPropertyWithValue("content", articleComment.getContent());

        then(articleCommentRepository).should().findById(ARTICLE_COMMENT_ID);
    }

    @DisplayName("[조회] 잘못된 id를 입력하면, 반환 없이 예외를 던짐")
    @Test
    void givenWrongArticleCommentId_whenSearchingArticleComment_thenThrowsException() {
        //Given
        Long wrongArticleCommentId = 2L;

        given(articleCommentRepository.findById(wrongArticleCommentId)).willReturn(Optional.empty());

        // When
        Throwable t = catchThrowable(() -> articleCommentService.getArticleComment(wrongArticleCommentId));

        // Then
        assertThat(t)
                .isInstanceOf(ArticleCommentNotFoundException.class)
                .hasMessage(ErrorCode.ARTICLE_COMMENT_NOT_FOUND.getMessage());

        then(articleCommentRepository).should().findById(wrongArticleCommentId);
    }

    @DisplayName("[저장] 댓글 작성 정보를 입력하면, 댓글을 저장")
    @Test
    void givenArticleCommentInfo_whenSavingArticleComment_thenSavesArticleComment() {
        //Given
        ArticleCommentDto dto = createArticleCommentDto();
        dto.setArticleId(ARTICLE_ID);
        dto.setMemberId(MEMBER_ID);

        given(memberRepository.findById(dto.getMemberId())).willReturn(Optional.of(createMember()));
        given(articleRepository.findById(dto.getArticleId())).willReturn(Optional.of(createArticle()));
        given(articleCommentRepository.save(dto.toEntityForUpdating()))
                .willReturn(createArticleComment());

        // When
        Long returnedArticleCommentId = articleCommentService.saveArticleComment(dto);

        // Then
        assertThat(returnedArticleCommentId).isNotNull();
        then(memberRepository).should().findById(MEMBER_ID);
        then(articleRepository).should().findById(ARTICLE_ID);
        then(articleCommentRepository).should().save(any(ArticleComment.class));
    }

    @DisplayName("[수정] id와 댓글 수정 정보를 입력하면, 댓글을 수정")
    @Test
    void givenArticleCommentIdAndModifiedArticleCommentInfo_whenUpdatingArticleComment_thenUpdatesArticleComment() {
        //Given
        ArticleComment articleComment = createArticleComment();
        ArticleCommentDto dto = createArticleCommentDto(ARTICLE_COMMENT_ID);
        dto.setContent("new content");

        given(articleCommentRepository.findById(dto.getArticleCommentId())).willReturn(Optional.of(articleComment));

        // When
        articleCommentService.updateArticleComment(dto);

        // Then
        assertThat(articleComment)
                .hasFieldOrPropertyWithValue("content", dto.getContent());

        then(articleCommentRepository).should().findById(ARTICLE_COMMENT_ID);
    }

    @DisplayName("[수정] 잘못된 id를 입력하면, 반환 없이 예외를 던짐")
    @Test
    void givenWrongArticleCommentIdAndModifiedArticleCommentInfo_whenUpdatingArticleComment_thenThrowsException() {
        //Given
        Long wrongArticleCommentId = 2L;
        ArticleCommentDto dto = createArticleCommentDto(wrongArticleCommentId);
        dto.setContent("new content");

        given(articleCommentRepository.findById(dto.getArticleCommentId())).willReturn(Optional.empty());

        // When
        Throwable t = catchThrowable(() -> articleCommentService.updateArticleComment(dto));

        // Then
        assertThat(t)
                .isInstanceOf(ArticleCommentNotFoundException.class)
                .hasMessage(ErrorCode.ARTICLE_COMMENT_NOT_FOUND.getMessage());

        then(articleCommentRepository).should().findById(wrongArticleCommentId);
    }

    @DisplayName("[수정] 잘못된 회원이 접근하면, 반환 없이 예외를 던짐")
    @Test
    void givenWrongMemberAndModifiedArticleCommentInfo_whenUpdatingArticleComment_thenThrowsException() {
        //Given
        Long wrongMemberId = 2L;
        AuthInfoHolder.setAuthInfo(createAuthResponse(wrongMemberId));

        ArticleCommentDto dto = createArticleCommentDto(ARTICLE_COMMENT_ID);
        dto.setContent("new content");

        given(articleCommentRepository.findById(dto.getArticleCommentId()))
                .willReturn(Optional.of(createArticleComment()));

        // When
        Throwable t = catchThrowable(() -> articleCommentService.updateArticleComment(dto));

        // Then
        assertThat(wrongMemberId).isNotEqualTo(MEMBER_ID);
        assertThat(t)
                .isInstanceOf(ForbiddenException.class)
                .hasMessage(ErrorCode.FORBIDDEN.getMessage());

        then(articleCommentRepository).should().findById(ARTICLE_COMMENT_ID);
    }


    @DisplayName("[삭제] id를 입력하면, 댓글을 삭제(soft delete)")
    @Test
    void givenArticleCommentId_whenDeletingArticleComment_thenDeletesArticleCommentSoftVer() {
        //Given
        ArticleComment articleComment = createArticleComment();
        ArticleCommentDto dto = createArticleCommentDto(ARTICLE_COMMENT_ID);

        given(articleCommentRepository.findById(dto.getArticleCommentId())).willReturn(Optional.of(articleComment));

        // When
        articleCommentService.softDeleteArticleComment(dto);

        // Then
        assertThat(articleComment)
                .hasFieldOrPropertyWithValue("isActive", false);

        then(articleCommentRepository).should().findById(ARTICLE_COMMENT_ID);
    }

    @DisplayName("[삭제] 잘못된 id를 입력하면, 반환 없이 예외를 던짐(soft delete)")
    @Test
    void givenArticleCommentId_whenDeletingArticleComment_thenThrowsExceptionSoftVer() {
        //Given
        Long wrongArticleCommentId = 2L;
        ArticleCommentDto dto = createArticleCommentDto(wrongArticleCommentId);

        given(articleCommentRepository.findById(dto.getArticleCommentId())).willReturn(Optional.empty());

        // When
        Throwable t = catchThrowable(() -> articleCommentService.softDeleteArticleComment(dto));

        // Then
        assertThat(t)
                .isInstanceOf(ArticleCommentNotFoundException.class)
                .hasMessage(ErrorCode.ARTICLE_COMMENT_NOT_FOUND.getMessage());

        then(articleCommentRepository).should().findById(wrongArticleCommentId);
    }

    @DisplayName("[삭제] 잘못된 회원이 접근하면, 반환 없이 예외를 던짐(soft delete)")
    @Test
    void givenWrongMemberAndModifiedArticleCommentInfo_whenDeletingArticleComment_thenThrowsExceptionSoftVer() {
        //Given
        Long wrongMemberId = 2L;
        AuthInfoHolder.setAuthInfo(createAuthResponse(wrongMemberId));

        ArticleCommentDto dto = createArticleCommentDto(ARTICLE_COMMENT_ID);
        dto.setContent("new content");

        given(articleCommentRepository.findById(dto.getArticleCommentId()))
                .willReturn(Optional.of(createArticleComment()));

        // When
        Throwable t = catchThrowable(() -> articleCommentService.softDeleteArticleComment(dto));

        // Then
        assertThat(wrongMemberId).isNotEqualTo(MEMBER_ID);
        assertThat(t)
                .isInstanceOf(ForbiddenException.class)
                .hasMessage(ErrorCode.FORBIDDEN.getMessage());

        then(articleCommentRepository).should().findById(ARTICLE_COMMENT_ID);
    }

    @DisplayName("[삭제] id를 입력하면, 댓글을 삭제(hard delete)")
    @Test
    void givenArticleCommentId_whenDeletingArticleComment_thenDeletesArticleCommentHardVer() {
        //Given
        ArticleComment articleComment = createArticleComment();
        ArticleCommentDto dto = createArticleCommentDto(ARTICLE_COMMENT_ID);

        given(articleCommentRepository.findById(dto.getArticleCommentId())).willReturn(Optional.of(articleComment));
        willDoNothing().given(articleCommentRepository).delete(articleComment);

        // When
        articleCommentService.hardDeleteArticleComment(dto);

        // Then
        then(articleCommentRepository).should().findById(ARTICLE_COMMENT_ID);
        then(articleCommentRepository).should().delete(articleComment);
    }

    @DisplayName("[삭제] 잘못된 id를 입력하면, 반환 없이 예외를 던짐(hard delete)")
    @Test
    void givenArticleCommentId_whenDeletingArticleComment_thenThrowsExceptionHardVer() {
        //Given
        Long wrongArticleCommentId = 2L;
        ArticleCommentDto dto = createArticleCommentDto(wrongArticleCommentId);

        given(articleCommentRepository.findById(dto.getArticleCommentId())).willReturn(Optional.empty());

        // When
        Throwable t = catchThrowable(() -> articleCommentService.hardDeleteArticleComment(dto));

        // Then
        assertThat(t)
                .isInstanceOf(ArticleCommentNotFoundException.class)
                .hasMessage(ErrorCode.ARTICLE_COMMENT_NOT_FOUND.getMessage());

        then(articleCommentRepository).should().findById(wrongArticleCommentId);
    }

    @DisplayName("[삭제] 잘못된 회원이 접근하면, 반환 없이 예외를 던짐(hard delete)")
    @Test
    void givenWrongMemberAndModifiedArticleCommentInfo_whenDeletingArticleComment_thenThrowsExceptionHardVer() {
        //Given
        Long wrongMemberId = 2L;
        AuthInfoHolder.setAuthInfo(createAuthResponse(wrongMemberId));

        ArticleCommentDto dto = createArticleCommentDto(ARTICLE_COMMENT_ID);
        dto.setContent("new content");

        given(articleCommentRepository.findById(dto.getArticleCommentId()))
                .willReturn(Optional.of(createArticleComment()));

        // When
        Throwable t = catchThrowable(() -> articleCommentService.hardDeleteArticleComment(dto));

        // Then
        assertThat(wrongMemberId).isNotEqualTo(MEMBER_ID);
        assertThat(t)
                .isInstanceOf(ForbiddenException.class)
                .hasMessage(ErrorCode.FORBIDDEN.getMessage());

        then(articleCommentRepository).should().findById(ARTICLE_COMMENT_ID);
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
}