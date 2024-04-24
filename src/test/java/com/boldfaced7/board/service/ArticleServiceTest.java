package com.boldfaced7.board.service;

import com.boldfaced7.board.Context;
import com.boldfaced7.board.auth.AuthInfoHolder;
import com.boldfaced7.board.domain.Article;
import com.boldfaced7.board.dto.ArticleDto;
import com.boldfaced7.board.dto.CustomPage;
import com.boldfaced7.board.dto.MemberDto;
import com.boldfaced7.board.error.exception.article.ArticleNotFoundException;
import com.boldfaced7.board.error.exception.auth.ForbiddenException;
import com.boldfaced7.board.error.exception.member.MemberNotFoundException;
import com.boldfaced7.board.repository.ArticleCommentRepository;
import com.boldfaced7.board.repository.ArticleRepository;
import com.boldfaced7.board.repository.AttachmentRepository;
import com.boldfaced7.board.repository.MemberRepository;
import com.boldfaced7.board.repository.filestore.LocalFileStore;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.boldfaced7.board.TestUtil.*;
import static com.boldfaced7.board.service.Facade.*;
import static com.boldfaced7.board.service.ServiceTestTemplate.doTest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@DisplayName("ArticleService 테스트")
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @InjectMocks ArticleService articleService;
    @Mock ArticleRepository mockArticleRepository;
    @Mock ArticleCommentRepository mockArticleCommentRepository;
    @Mock MemberRepository mockMemberRepository;
    @Mock AttachmentRepository mockAttachmentRepository;
    @Mock LocalFileStore mockFileStore;
    Facade facade;

    @BeforeEach
    void setUp() {
        AuthInfoHolder.setAuthInfo(authResponse());
        facade = Facade.builder()
                .mockArticleRepository(mockArticleRepository)
                .mockArticleCommentRepository(mockArticleCommentRepository)
                .mockMemberRepository(mockMemberRepository)
                .mockAttachmentRepository(mockAttachmentRepository)
                .mockFileStore(mockFileStore)
                .build();
    }

    @AfterEach
    void clear() {
        AuthInfoHolder.releaseAuthInfo();
    }

    @DisplayName("게시글 단건 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    void getArticleTest(Context<Facade, ArticleDto> context, ArticleDto request) {
        doTest(() -> articleService.getArticle(request), context, facade);
    }
    static Stream<Arguments> getArticleTest() {
        Context<Facade, ArticleDto> valid = new Context<>("id를 입력하면, 게시글과 관련 댓글 리스트를 반환");
        valid.mocks(articleRepository, a -> a.findById(anyLong()), Optional.of(article()));
        valid.mocks(attachmentRepository, a -> a.findAllByArticle(any()), attachments());
        valid.mocks(fileStore, f -> f.getUrls(any()), List.of(STORED_URL));
        valid.mocks(articleCommentRepository, a -> a.findAllByArticle(any(), any()), articleComments());

        valid.asserts(dto -> assertThat(dto.getArticleId()).isNotNull());
        valid.asserts(dto -> assertThat(dto.getArticleComments().getContent()).isNotEmpty());
        valid.asserts(dto -> assertThat(dto.getAttachmentUrls()).first().isEqualTo(STORED_URL));

        Context<Facade, ?> notFound = new Context<>("잘못된 id를 입력하면, 반환 없이 예외를 던짐");
        notFound.mocks(articleRepository, a -> a.findById(any()), Optional.empty());
        notFound.assertsThrowable(t -> assertThat(t).isInstanceOf(ArticleNotFoundException.class));

        return Stream.of(Arguments.of(valid, articleDto(1L)), Arguments.of(notFound, articleDto(2L)));
    }

    @DisplayName("게시글 목록 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    void getArticlesTest(Context<Facade,CustomPage<?>> context, Pageable request) {
        doTest(() -> articleService.getArticles(request), context, facade);
    }
    static Stream<Arguments> getArticlesTest() {
        Context<Facade,CustomPage<?>> valid = new Context<>("게시글 목록을 반환");
        valid.mocks(articleRepository, a -> a.findAll(any(Pageable.class)), articles());
        valid.asserts(dtos -> assertThat(dtos.getContent()).isNotEmpty());

        return Stream.of(Arguments.of(valid, pageable()));
    }

    @DisplayName("회원 작성 게시글 목록 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    void getArticlesOfMemberTest(Context<Facade,CustomPage<?>> context, MemberDto request) {
        doTest(() -> articleService.getArticles(request), context, facade);
    }
    static Stream<Arguments> getArticlesOfMemberTest() {
        Context<Facade,CustomPage<?>> valid = new Context<>("회원 id를 입력하면, 게시글 리스트를 반환");
        valid.mocks(memberRepository, m -> m.findById(anyLong()), Optional.of(member()));
        valid.mocks(articleRepository, a -> a.findAllByMember(any(), any()), articles());
        valid.asserts(dtos -> assertThat(dtos.getContent()).isNotEmpty());

        Context<Facade, ?> notFound = new Context<>("잘못된 회원 id를 입력하면, 반환 없이 예외를 던짐");
        notFound.mocks(memberRepository, m -> m.findById(anyLong()), Optional.empty());
        notFound.assertsThrowable(t -> assertThat(t).isInstanceOf(MemberNotFoundException.class));

        return Stream.of(Arguments.of(valid, memberDto(1L)), Arguments.of(notFound, memberDto(2L)));
    }

    @DisplayName("게시글 저장")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    void postArticleTests(Context<Facade, Long> context, ArticleDto request) {
        doTest(() -> articleService.saveArticle(request), context, facade);
    }
    static Stream<Arguments> postArticleTests() {
        Context<Facade, Long> valid = new Context<>("게시글 작성 정보를 입력하면, 게시글을 저장");
        valid.mocks(memberRepository, m -> m.findById(anyLong()), Optional.of(member()));
        valid.mocks(articleRepository, a -> a.save(any()), article());
        valid.mocks(attachmentRepository, a -> a.updateAttachments(any(), any()), 1);
        valid.asserts(id -> assertThat(id).isEqualTo(1L));

        Context<Facade, ?> notFound = new Context<>("존재하지 않는 회원의 요청이면, 반환 없이 예외를 던짐");
        notFound.mocks(memberRepository, m -> m.findById(anyLong()), Optional.empty());
        notFound.assertsThrowable(t -> assertThat(t).isInstanceOf(MemberNotFoundException.class));

        return Stream.of(Arguments.of(valid, articleDto()), Arguments.of(notFound, articleDto()));
    }

    @DisplayName("게시글 수정")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    void updateArticleTests(Context<Facade, ?> context, ArticleDto request) {
        doTest(() -> articleService.updateArticle(request), context, facade);
    }
    static Stream<Arguments> updateArticleTests() {
        Article target = article();

        Context<Facade, ?> valid = new Context<>("id와 게시글 수정 정보를 입력하면, 게시글을 수정");
        valid.mocks(articleRepository, a -> a.findById(anyLong()), Optional.of(target));
        valid.mocks(attachmentRepository, a -> a.updateAttachments(any(), any()), 1);
        valid.asserts(() -> assertThat(target).hasFieldOrPropertyWithValue("title", NEW)
                                              .hasFieldOrPropertyWithValue("content", NEW));

        Context<Facade, ?> notFound = new Context<>("잘못된 id를 입력하면, 수정 없이 예외를 던짐");
        notFound.mocks(articleRepository, a -> a.findById(anyLong()), Optional.empty());
        notFound.assertsThrowable(t -> assertThat(t).isInstanceOf(ArticleNotFoundException.class));

        Context<Facade, ?> forbidden = new Context<>("잘못된 회원이 접근하면, 수정 없이 예외를 던짐");
        forbidden.mocks(articleRepository, a -> a.findById(anyLong()), Optional.of(article(2L)));
        forbidden.assertsThrowable(t -> assertThat(t).isInstanceOf(ForbiddenException.class));

        return Stream.of(Arguments.of(valid, articleDto(NEW, NEW)), Arguments.of(notFound, articleDto(NEW, NEW)),
                Arguments.of(forbidden, articleDto(NEW, NEW)));
    }

    @DisplayName("게시글 비활성화")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    void deactivateArticleTests(Context<Facade, ?> context, ArticleDto request) {
        doTest(() -> articleService.softDeleteArticle(request), context, facade);
    }
    static Stream<Arguments> deactivateArticleTests() {
        Article target = article();

        Context<Facade, ?> valid = new Context<>("id를 입력하면, 댓글을 비활성화");
        valid.mocks(articleRepository, a -> a.findById(anyLong()), Optional.of(target));
        valid.mocks(attachmentRepository, a -> a.deactivateAttachments(any()), 1);
        valid.asserts(() -> assertThat(target).hasFieldOrPropertyWithValue("isActive", false));

        Context<Facade, ?> notFound = new Context<>("잘못된 id를 입력하면, 비활성화 없이 예외를 던짐");
        notFound.mocks(articleRepository, a -> a.findById(anyLong()), Optional.empty());
        notFound.assertsThrowable(t -> assertThat(t).isInstanceOf(ArticleNotFoundException.class));

        Context<Facade, ?> forbidden = new Context<>("잘못된 회원이 접근하면, 비활성화 없이 예외를 던짐");
        forbidden.mocks(articleRepository, a -> a.findById(anyLong()), Optional.of(article(2L)));
        forbidden.assertsThrowable(t -> assertThat(t).isInstanceOf(ForbiddenException.class));

        return Stream.of(Arguments.of(valid, articleDto()), Arguments.of(notFound, articleDto()), Arguments.of(forbidden, articleDto()));
    }

    @DisplayName("게시글 삭제")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    void deleteArticleTests(Context<Facade, ?> context, ArticleDto request) {
        doTest(() -> articleService.hardDeleteArticle(request), context, facade);
    }
    static Stream<Arguments> deleteArticleTests() {
        Context<Facade, ?> valid = new Context<>("id를 입력하면, 댓글을 삭제");
        valid.mocks(articleRepository, a -> a.findById(anyLong()), Optional.of(article()));
        valid.mocks(articleRepository, a -> a.delete(any()));
        valid.mocks(attachmentRepository, a -> a.deleteAttachments(any()), 1);

        Context<Facade, ?> notFound = new Context<>("잘못된 id를 입력하면, 삭제 없이 예외를 던짐");
        notFound.mocks(articleRepository, a -> a.findById(anyLong()), Optional.empty());
        notFound.assertsThrowable(t -> assertThat(t).isInstanceOf(ArticleNotFoundException.class));

        Context<Facade, ?> forbidden = new Context<>("잘못된 회원이 접근하면, 삭제 없이 예외를 던짐");
        forbidden.mocks(articleRepository, a -> a.findById(anyLong()), Optional.of(article(2L)));
        forbidden.assertsThrowable(t -> assertThat(t).isInstanceOf(ForbiddenException.class));

        return Stream.of(Arguments.of(valid, articleDto()), Arguments.of(notFound, articleDto()), Arguments.of(forbidden, articleDto()));
    }
}