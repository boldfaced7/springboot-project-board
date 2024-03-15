package com.boldfaced7.board.service;

import com.boldfaced7.board.Assertion;
import com.boldfaced7.board.Context;
import com.boldfaced7.board.auth.AuthInfoHolder;
import com.boldfaced7.board.domain.Article;
import com.boldfaced7.board.domain.ArticleComment;
import com.boldfaced7.board.domain.Member;
import com.boldfaced7.board.dto.ArticleDto;
import com.boldfaced7.board.dto.MemberDto;
import com.boldfaced7.board.error.exception.auth.ForbiddenException;
import com.boldfaced7.board.error.exception.article.ArticleNotFoundException;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static com.boldfaced7.board.TestUtil.*;
import static com.boldfaced7.board.RepoMethod.*;

@DisplayName("ArticleService 테스트")
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @InjectMocks private ArticleService articleService;
    @Mock private ArticleRepository articleRepository;
    @Mock private ArticleCommentRepository articleCommentRepository;
    @Mock private MemberRepository memberRepository;
    ServiceTestTemplate testTemplate;

    @BeforeEach
    void setUp() {
        AuthInfoHolder.setAuthInfo(createAuthResponse());
        DependencyHolder dependencyHolder = DependencyHolder.builder().articleRepository(articleRepository)
                .articleCommentRepository(articleCommentRepository).memberRepository(memberRepository).build();

        testTemplate = new ServiceTestTemplate(dependencyHolder);
    }

    @AfterEach
    void clear() {
        AuthInfoHolder.releaseAuthInfo();
    }

    @DisplayName("게시글 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createGetArticleRequestTests")
    void getArticleTest(String ignoredMessage, List<Context<DependencyHolder>> contexts, Long request, List<Assertion<ArticleDto>> assertions) {
        testTemplate.performRequest(contexts, articleService::getArticle, request, assertions);
    }
    static Stream<Arguments> createGetArticleRequestTests() {
        Article article = createArticle();
        List<ArticleComment> articleComments = List.of(createArticleComment());

        Map<String, List<Context<DependencyHolder>>> contexts = Map.of(
                VALID, List.of(
                        new Context<>(findArticleById, ARTICLE_ID, Optional.of(article), articleRepoFunc),
                        new Context<>(findArticleCommentsByArticle, article, articleComments, articleCommentRepoFunc)
                ),
                NOT_FOUND, List.of(new Context<>(findArticleById, ARTICLE_ID, Optional.empty(), articleRepoFunc))
        );
        Map<String, List<Assertion<ArticleDto>>> assertions = Map.of(
                VALID, List.of(new Assertion<>(new ArticleDto(article, articleComments))),
                NOT_FOUND, List.of(new Assertion<>(ArticleNotFoundException.class))
        );
        return Stream.of(
                Arguments.of("id를 입력하면, 게시글과 관련 댓글 리스트를 반환", contexts.get(VALID), ARTICLE_ID, assertions.get(VALID)),
                Arguments.of("잘못된 id를 입력하면, 반환 없이 예외를 던짐", contexts.get(NOT_FOUND), ARTICLE_ID, assertions.get(NOT_FOUND))
        );
    }

    @DisplayName("게시글 목록 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createGetArticlesRequestTests")
    void getArticlesTest(String ignoredMessage, List<Context<DependencyHolder>> contexts, List<Assertion<List<ArticleDto>>> assertions) {
        testTemplate.performRequest(contexts, articleService::getArticles, assertions);
    }
    static Stream<Arguments> createGetArticlesRequestTests() {
        Map<String, List<Context<DependencyHolder>>> contexts = Map.of(
                VALID, List.of(new Context<>(findArticles, List.of(createArticle()), articleRepoFunc))
        );
        Map<String, List<Assertion<List<ArticleDto>>>> assertions = Map.of(VALID, List.of(new Assertion<>()));
        return Stream.of(Arguments.of("게시글 목록을 반환", contexts.get(VALID), assertions.get(VALID)));
    }

    @DisplayName("회원 작성 게시글 목록 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createGetArticlesOfMemberRequestTests")
    void getArticlesOfMemberTest(String ignoredMessage, List<Context<DependencyHolder>> contexts, MemberDto request, List<Assertion<List<ArticleDto>>> assertions) {
        testTemplate.performRequest(contexts, articleService::getArticles, request, assertions);
    }
    static Stream<Arguments> createGetArticlesOfMemberRequestTests() {
        Member member = createMember();
        MemberDto requestMemberDto = new MemberDto(MEMBER_ID);

        Map<String, List<Context<DependencyHolder>>> contexts = Map.of(
                VALID, List.of(
                        new Context<>(findMemberById, requestMemberDto.getMemberId(), Optional.of(member), memberRepoFunc),
                        new Context<>(findArticlesByMember, member, List.of(createArticle()), articleRepoFunc)
                ),
                NOT_FOUND, List.of(new Context<>(findMemberById, requestMemberDto.getMemberId(), Optional.empty(), memberRepoFunc))
        );
        Map<String, List<Assertion<ArticleDto>>> assertions = Map.of(
                VALID, List.of(new Assertion<>()),
                NOT_FOUND, List.of(new Assertion<>(MemberNotFoundException.class))
        );
        return Stream.of(
                Arguments.of("회원 id를 입력하면, 게시글과 관련 댓글 리스트를 반환", contexts.get(VALID), requestMemberDto, assertions.get(VALID)),
                Arguments.of("잘못된 회원 id를 입력하면, 반환 없이 예외를 던짐", contexts.get(NOT_FOUND), requestMemberDto, assertions.get(NOT_FOUND))
        );
    }

    @DisplayName("게시글 저장")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createPostArticleRequestTests")
    void PostArticleTests(String ignoredMessage, List<Context<DependencyHolder>> contexts, ArticleDto request, List<Assertion<Long>> assertions) {
        testTemplate.performRequest(contexts, articleService::saveArticle, request, assertions);
    }
    static Stream<Arguments> createPostArticleRequestTests() {
        Member member = createMember();
        ArticleDto requestDto = createArticleDto();

        Map<String, List<Context<DependencyHolder>>> contexts = Map.of(
                VALID, List.of(
                        new Context<>(findMemberById, member.getId(), Optional.of(member), memberRepoFunc),
                        new Context<>(saveArticle, requestDto.toEntityForSaving(member), createArticle(), articleRepoFunc)
                ),
                NOT_FOUND, List.of(new Context<>(findMemberById, requestDto.getMemberId(), Optional.empty(), memberRepoFunc))
        );
        Map<String, List<Assertion<Long>>> assertions = Map.of(
                VALID, List.of(new Assertion<>(ARTICLE_ID)),
                NOT_FOUND, List.of(new Assertion<>(MemberNotFoundException.class))
        );
        return Stream.of(
                Arguments.of("게시글 작성 정보를 입력하면, 게시글을 저장", contexts.get(VALID), requestDto, assertions.get(VALID)),
                Arguments.of("존재하지 않는 회원의 요청이면, 반환 없이 예외를 던짐", contexts.get(NOT_FOUND), requestDto, assertions.get(NOT_FOUND))
        );
    }

    @DisplayName("게시글 수정")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("UpdateArticleRequestTests")
    void UpdateArticleTests(String ignoredMessage, List<Context<DependencyHolder>> contexts, ArticleDto request, List<Assertion<Article>> assertions, Article target) {
        testTemplate.performRequest(contexts, articleService::updateArticle, request, assertions, target);
}
    static Stream<Arguments> UpdateArticleRequestTests() {
        Article article = createArticle();
        Article forbiddenArticle = createArticle();
        ReflectionTestUtils.setField(forbiddenArticle.getMember(), "id", MEMBER_ID+1);

        ArticleDto requestDto = ArticleDto.builder().articleId(ARTICLE_ID).title("New").content("New").build();

        Map<String, List<Context<DependencyHolder>>> contexts = Map.of(
                VALID, List.of(new Context<>(findArticleById, ARTICLE_ID, Optional.of(article), articleRepoFunc)),
                NOT_FOUND, List.of(new Context<>(findArticleById, ARTICLE_ID, Optional.empty(), articleRepoFunc)),
                FORBIDDEN, List.of(new Context<>(findArticleById, ARTICLE_ID, Optional.of(forbiddenArticle), articleRepoFunc))
        );
        Map<String, List<Assertion<Article>>> assertions = Map.of(
                VALID, List.of(new Assertion<>(Map.of("title", requestDto.getTitle(),"content", requestDto.getContent()))),
                NOT_FOUND, List.of(new Assertion<>(ArticleNotFoundException.class)),
                FORBIDDEN, List.of(new Assertion<>(ForbiddenException.class))
        );
        return Stream.of(
                Arguments.of("id와 게시글 수정 정보를 입력하면, 게시글을 수정", contexts.get(VALID), requestDto, assertions.get(VALID), article),
                Arguments.of("잘못된 id를 입력하면, 수정 없이 예외를 던짐", contexts.get(NOT_FOUND), requestDto, assertions.get(NOT_FOUND), null),
                Arguments.of("잘못된 회원이 접근하면, 수정 없이 예외를 던짐", contexts.get(FORBIDDEN), requestDto, assertions.get(FORBIDDEN), null)
        );
    }

    @DisplayName("게시글 비활성화")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createInactiveArticleRequestTests")
    void deactivateArticleTest(String ignoredMessage, List<Context<DependencyHolder>> contexts, ArticleDto request, List<Assertion<Article>> assertions, Article target) {
                     testTemplate.performRequest(contexts, articleService::softDeleteArticle, request, assertions, target);
    }
    static Stream<Arguments> createInactiveArticleRequestTests() {
        Article article = createArticle();
        Article forbiddenArticle = createArticle();
        ReflectionTestUtils.setField(forbiddenArticle.getMember(), "id", MEMBER_ID+1);

        ArticleDto requestDto = ArticleDto.builder().articleId(ARTICLE_ID).build();

        Map<String, List<Context<DependencyHolder>>> contexts = Map.of(
                VALID, List.of(new Context<>(findArticleById, ARTICLE_ID, Optional.of(article), articleRepoFunc)),
                NOT_FOUND, List.of(new Context<>(findArticleById, ARTICLE_ID, Optional.empty(), articleRepoFunc)),
                FORBIDDEN, List.of(new Context<>(findArticleById, ARTICLE_ID, Optional.of(forbiddenArticle), articleRepoFunc))
        );
        Map<String, List<Assertion<Article>>> assertions = Map.of(
                VALID, List.of(new Assertion<>(Map.of("isActive", false))),
                NOT_FOUND, List.of(new Assertion<>(ArticleNotFoundException.class)),
                FORBIDDEN, List.of(new Assertion<>(ForbiddenException.class))
        );
        return Stream.of(
            Arguments.of("id를 입력하면, 댓글을 비활성화", contexts.get(VALID), requestDto, assertions.get(VALID), article),
            Arguments.of("잘못된 id를 입력하면, 비활성화 없이 예외를 던짐", contexts.get(NOT_FOUND), requestDto, assertions.get(NOT_FOUND), null),
            Arguments.of("잘못된 회원이 접근하면, 비활성화 없이 예외를 던짐", contexts.get(FORBIDDEN), requestDto, assertions.get(FORBIDDEN), null)
        );
    }

    @DisplayName("게시글 삭제")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createDeleteArticleRequestTests")
    void deleteArticleTest(String ignoredMessage, List<Context<DependencyHolder>> contexts, ArticleDto request, List<Assertion<Article>> assertions) {
        testTemplate.performRequest(contexts, articleService::hardDeleteArticle, request, assertions, null);
    }
    static Stream<Arguments> createDeleteArticleRequestTests() {
        Article article = createArticle();
        Article forbiddenArticle = createArticle();
        ReflectionTestUtils.setField(forbiddenArticle.getMember(), "id", MEMBER_ID+1);

        ArticleDto requestDto = ArticleDto.builder().articleId(ARTICLE_ID).build();

        Map<String, List<Context<DependencyHolder>>> contexts = Map.of(
                VALID, List.of(
                        new Context<>(findArticleById, ARTICLE_ID, Optional.of(article), articleRepoFunc),
                        new Context<>(deleteArticle, article, articleRepoFunc)
                ),
                NOT_FOUND, List.of(new Context<>(findArticleById, ARTICLE_ID, Optional.empty(), articleRepoFunc)),
                FORBIDDEN, List.of(new Context<>(findArticleById, ARTICLE_ID, Optional.of(forbiddenArticle), articleRepoFunc))
        );
        Map<String, List<Assertion<Article>>> assertions = Map.of(
                VALID, List.of(),
                NOT_FOUND, List.of(new Assertion<>(ArticleNotFoundException.class)),
                FORBIDDEN, List.of(new Assertion<>(ForbiddenException.class))
        );
        return Stream.of(
                Arguments.of("id를 입력하면, 댓글을 삭제", contexts.get(VALID), requestDto, assertions.get(VALID)),
                Arguments.of("잘못된 id를 입력하면, 삭제 없이 예외를 던짐", contexts.get(NOT_FOUND), requestDto, assertions.get(NOT_FOUND)),
                Arguments.of("잘못된 회원이 접근하면, 삭제 없이 예외를 던짐", contexts.get(FORBIDDEN), requestDto, assertions.get(FORBIDDEN))
        );
    }
}