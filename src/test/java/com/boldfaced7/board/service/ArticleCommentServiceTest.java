package com.boldfaced7.board.service;

import com.boldfaced7.board.Context;
import com.boldfaced7.board.auth.AuthInfoHolder;
import com.boldfaced7.board.domain.ArticleComment;
import com.boldfaced7.board.dto.ArticleCommentDto;
import com.boldfaced7.board.dto.ArticleDto;
import com.boldfaced7.board.dto.CustomPage;
import com.boldfaced7.board.dto.MemberDto;
import com.boldfaced7.board.error.exception.article.ArticleNotFoundException;
import com.boldfaced7.board.error.exception.articlecomment.ArticleCommentNotFoundException;
import com.boldfaced7.board.error.exception.auth.ForbiddenException;
import com.boldfaced7.board.error.exception.member.MemberNotFoundException;
import com.boldfaced7.board.repository.ArticleCommentRepository;
import com.boldfaced7.board.repository.ArticleRepository;
import com.boldfaced7.board.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.stream.Stream;

import static com.boldfaced7.board.TestUtil.*;
import static com.boldfaced7.board.service.Facade.*;
import static com.boldfaced7.board.service.ServiceTestTemplate.doTest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@DisplayName("ArticleCommentService 테스트")
@ExtendWith(MockitoExtension.class)
class ArticleCommentServiceTest {
    @InjectMocks private ArticleCommentService articleCommentService;
    @Mock private ArticleRepository mockArticleRepository;
    @Mock private ArticleCommentRepository mockArticleCommentRepository;
    @Mock private MemberRepository mockMemberRepository;
    Facade facade;

    @BeforeEach
    void setUp() {
        AuthInfoHolder.setAuthInfo(authResponse());
        facade = builder()
                .mockArticleRepository(mockArticleRepository)
                .mockArticleCommentRepository(mockArticleCommentRepository)
                .mockMemberRepository(mockMemberRepository)
                .build();
    }

    @AfterEach
    void clear() {
        AuthInfoHolder.releaseAuthInfo();
    }

    @DisplayName("댓글 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    void getArticleCommentTest(Context<Facade, ArticleCommentDto> context, Long request) {
        doTest(() -> articleCommentService.getArticleComment(request), context, facade);
    }
    static Stream<Arguments> getArticleCommentTest() {
        Context<Facade, ArticleCommentDto> valid = new Context<>("id를 입력하면, 댓글을 반환");
        valid.mocks(articleCommentRepository, a -> a.findById(anyLong()), Optional.of(articleComment()));
        valid.asserts(dto -> assertThat(dto.getArticleCommentId()).isNotNull());

        Context<Facade, ?> notFound = new Context<>("잘못된 id를 입력하면, 반환 없이 예외를 던짐");
        notFound.mocks(articleCommentRepository, a -> a.findById(anyLong()), Optional.empty());
        notFound.assertsThrowable(t -> assertThat(t).isInstanceOf(ArticleCommentNotFoundException.class));

        return Stream.of(Arguments.of(valid, 1L), Arguments.of(notFound, 2L));
    }

    @DisplayName("댓글 목록 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    void getArticleCommentsTest(Context<Facade, CustomPage<?>> context, Pageable request) {
        doTest(() -> articleCommentService.getArticleComments(request), context, facade);
    }
    static Stream<Arguments> getArticleCommentsTest() {
        Context<Facade, CustomPage<?>> valid = new Context<>("댓글 리스트를 반환");
        valid.mocks(articleCommentRepository, a -> a.findAll(pageable()), articleComments());
        valid.asserts(dtos -> assertThat(dtos.getContent()).isNotEmpty());

        return Stream.of(Arguments.of(valid, pageable()));
    }

    @DisplayName("게시글의 댓글 목록 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    void getArticleCommentsOfArticleTest(Context<Facade,CustomPage<?>> context, ArticleDto request) {
        doTest(() -> articleCommentService.getArticleComments(request), context, facade);
    }
    static Stream<Arguments> getArticleCommentsOfArticleTest() {
        Context<Facade,CustomPage<?>> valid = new Context<>("게시글 id를 입력하면, 관련 댓글 리스트를 반환");
        valid.mocks(articleRepository, a -> a.findById(anyLong()), Optional.of(article()));
        valid.mocks(articleCommentRepository, a -> a.findAllByArticle(any(), any()), articleComments());
        valid.asserts(dtos -> assertThat(dtos.getContent()).isNotEmpty());

        Context<Facade, ?> notFound = new Context<>("잘못된 게시글 id를 입력하면, 반환 없이 예외를 던짐");
        notFound.mocks(articleRepository, a -> a.findById(anyLong()), Optional.empty());
        notFound.assertsThrowable(t -> assertThat(t).isInstanceOf(ArticleNotFoundException.class));

        return Stream.of(Arguments.of(valid, articleDto(1L)), Arguments.of(notFound, articleDto(2L)));
    }

    @DisplayName("회원 작성 댓글 목록 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    void getArticleCommentsOfMemberTest(Context<Facade,CustomPage<?>> context, MemberDto request) {
        doTest(() -> articleCommentService.getArticleComments(request), context, facade);
    }
    static Stream<Arguments> getArticleCommentsOfMemberTest() {
        Context<Facade,CustomPage<?>> valid = new Context<>("회원 id를 입력하면, 게시글 리스트를 반환");
        valid.mocks(memberRepository, m -> m.findById(anyLong()), Optional.of(member()));
        valid.mocks(articleCommentRepository, a -> a.findAllByMember(any(), any()), articleComments());
        valid.asserts(dtos -> assertThat(dtos.getContent()).isNotEmpty());

        Context<Facade, ?> notFound = new Context<>("잘못된 회원 id를 입력하면, 반환 없이 예외를 던짐");
        notFound.assertsThrowable(t -> assertThat(t).isInstanceOf(ForbiddenException.class));

        return Stream.of(Arguments.of(valid, memberDto(1L)), Arguments.of(notFound, memberDto(2L)));
    }

    @DisplayName("댓글 저장")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    void postArticleCommentTests(Context<Facade, Long> context, ArticleCommentDto request) {
        doTest(() -> articleCommentService.saveArticleComment(request), context, facade);
    }
    static Stream<Arguments> postArticleCommentTests() {
        Context<Facade, Long> valid = new Context<>("게시글 작성 정보를 입력하면, 게시글을 저장");
        valid.mocks(memberRepository, m -> m.findById(anyLong()), Optional.of(member()));
        valid.mocks(articleRepository, a -> a.findById(anyLong()), Optional.of(article()));
        valid.mocks(articleCommentRepository, a -> a.save(any()), articleComment());
        valid.asserts(id -> assertThat(id).isEqualTo(1L));

        Context<Facade, Long> articleNotFound = new Context<>("게시글이 존재하지 않는다면, 저장 없이 예외를 던짐");
        articleNotFound.mocks(memberRepository, m -> m.findById(anyLong()), Optional.of(member()));
        articleNotFound.mocks(articleRepository, a -> a.findById(anyLong()), Optional.empty());
        articleNotFound.assertsThrowable(t -> assertThat(t).isInstanceOf(ArticleNotFoundException.class));

        Context<Facade, ?> memberNotFound = new Context<>("존재하지 않는 회원의 요청이면, 저장 없이 예외를 던짐");
        memberNotFound.mocks(memberRepository, m -> m.findById(anyLong()), Optional.empty());
        memberNotFound.assertsThrowable(t -> assertThat(t).isInstanceOf(MemberNotFoundException.class));

        ArticleCommentDto request = articleCommentDto();
        return Stream.of(Arguments.of(valid, request),
                         Arguments.of(articleNotFound, request),
                         Arguments.of(memberNotFound, request));
    }

    @DisplayName("댓글 수정")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    void updateArticleCommentTests(Context<Facade, ?> context, ArticleCommentDto request) {
        doTest(() -> articleCommentService.updateArticleComment(request), context, facade);
    }
    static Stream<Arguments> updateArticleCommentTests() {
        ArticleComment target = articleComment();

        Context<Facade, ?> valid = new Context<>("id와 댓글 수정 정보를 입력하면, 댓글을 수정");
        valid.mocks(articleCommentRepository, a -> a.findById(anyLong()), Optional.of(target));
        valid.asserts(() -> assertThat(target).hasFieldOrPropertyWithValue("content", NEW));

        Context<Facade, ?> notFound = new Context<>("잘못된 id를 입력하면, 수정 없이 예외를 던짐");
        notFound.mocks(articleCommentRepository, a -> a.findById(anyLong()), Optional.empty());
        notFound.assertsThrowable(t -> assertThat(t).isInstanceOf(ArticleCommentNotFoundException.class));

        Context<Facade, ?> forbidden = new Context<>("잘못된 회원이 접근하면, 수정 없이 예외를 던짐");
        forbidden.mocks(articleCommentRepository, a -> a.findById(anyLong()), Optional.of(articleComment(2L)));
        forbidden.assertsThrowable(t -> assertThat(t).isInstanceOf(ForbiddenException.class));

        return Stream.of(Arguments.of(valid, articleCommentDto(NEW)), Arguments.of(notFound, articleCommentDto(NEW)),
                Arguments.of(forbidden, articleCommentDto(NEW)));
    }

    @DisplayName("댓글 비활성화")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    void deactivateArticleCommentTests(Context<Facade, ?> context, ArticleCommentDto request) {
        doTest(() -> articleCommentService.softDeleteArticleComment(request), context, facade);
    }
    static Stream<Arguments> deactivateArticleCommentTests() {
        ArticleComment target = articleComment();

        Context<Facade, ?> valid = new Context<>("id를 입력하면, 댓글을 비활성화");
        valid.mocks(articleCommentRepository, a -> a.findById(anyLong()), Optional.of(target));
        valid.asserts(() -> assertThat(target).hasFieldOrPropertyWithValue("isActive", false));

        Context<Facade, ?> notFound = new Context<>("잘못된 id를 입력하면, 비활성화 없이 예외를 던짐");
        notFound.mocks(articleCommentRepository, a -> a.findById(anyLong()), Optional.empty());
        notFound.assertsThrowable(t -> assertThat(t).isInstanceOf(ArticleCommentNotFoundException.class));

        Context<Facade, ?> forbidden = new Context<>("잘못된 회원이 접근하면, 비활성화 없이 예외를 던짐");
        forbidden.mocks(articleCommentRepository, a -> a.findById(anyLong()), Optional.of(articleComment(2L)));
        forbidden.assertsThrowable(t -> assertThat(t).isInstanceOf(ForbiddenException.class));

        return Stream.of(Arguments.of(valid, articleCommentDto()), Arguments.of(notFound, articleCommentDto()),
                Arguments.of(forbidden, articleCommentDto()));
    }

    @DisplayName("댓글 삭제")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    void deleteArticleCommentTests(Context<Facade, ?> context, ArticleCommentDto request) {
        doTest(() -> articleCommentService.hardDeleteArticleComment(request), context, facade);
    }
    static Stream<Arguments> deleteArticleCommentTests() {
        Context<Facade, ?> valid = new Context<>("id를 입력하면, 댓글을 삭제");
        valid.mocks(articleCommentRepository, a -> a.findById(anyLong()), Optional.of(articleComment()));
        valid.mocks(articleCommentRepository, a -> a.delete(any()));

        Context<Facade, ?> notFound = new Context<>("잘못된 id를 입력하면, 삭제 없이 예외를 던짐");
        notFound.mocks(articleCommentRepository, a -> a.findById(anyLong()), Optional.empty());
        notFound.assertsThrowable(t -> assertThat(t).isInstanceOf(ArticleCommentNotFoundException.class));

        Context<Facade, ?> forbidden = new Context<>("잘못된 회원이 접근하면, 삭제 없이 예외를 던짐");
        forbidden.mocks(articleCommentRepository, a -> a.findById(anyLong()), Optional.of(articleComment(2L)));
        forbidden.assertsThrowable(t -> assertThat(t).isInstanceOf(ForbiddenException.class));

        return Stream.of(Arguments.of(valid, articleCommentDto()), Arguments.of(notFound, articleCommentDto()),
                Arguments.of(forbidden, articleCommentDto()));
    }
}