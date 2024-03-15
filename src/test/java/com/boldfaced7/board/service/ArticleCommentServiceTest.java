package com.boldfaced7.board.service;

import com.boldfaced7.board.Assertion;
import com.boldfaced7.board.Context;
import com.boldfaced7.board.auth.AuthInfoHolder;
import com.boldfaced7.board.domain.Article;
import com.boldfaced7.board.domain.ArticleComment;
import com.boldfaced7.board.domain.Member;
import com.boldfaced7.board.dto.ArticleCommentDto;
import com.boldfaced7.board.dto.ArticleDto;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static com.boldfaced7.board.TestUtil.*;
import static com.boldfaced7.board.RepoMethod.*;

@DisplayName("ArticleCommentService 테스트")
@ExtendWith(MockitoExtension.class)
class ArticleCommentServiceTest {

    @InjectMocks private ArticleCommentService articleCommentService;
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

    @DisplayName("댓글 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createGetArticleCommentRequestTests")
    void getArticleCommentTest(String ignoredMessage, List<Context<DependencyHolder>> contexts, Long request, List<Assertion<ArticleCommentDto>> assertions) {
        testTemplate.performRequest(contexts, articleCommentService::getArticleComment, request, assertions);
    }
    static Stream<Arguments> createGetArticleCommentRequestTests() {
        ArticleComment articleComment = createArticleComment();
        Map<String, List<Context<DependencyHolder>>> contexts = Map.of(
                VALID, List.of(new Context<>(findArticleCommentById, ARTICLE_COMMENT_ID, Optional.of(articleComment), articleCommentRepoFunc)),
                NOT_FOUND, List.of(new Context<>(findArticleCommentById, ARTICLE_COMMENT_ID, Optional.empty(), articleCommentRepoFunc))
        );
        Map<String, List<Assertion<ArticleCommentDto>>> assertions = Map.of(
                VALID, List.of(new Assertion<>(new ArticleCommentDto(articleComment))),
                NOT_FOUND, List.of(new Assertion<>(ArticleCommentNotFoundException.class))
        );
        return Stream.of(
                Arguments.of("id를 입력하면, 댓글을 반환", contexts.get(VALID), ARTICLE_COMMENT_ID, assertions.get(VALID)),
                Arguments.of("잘못된 id를 입력하면, 조회 없이 예외를 던짐", contexts.get(NOT_FOUND), ARTICLE_COMMENT_ID, assertions.get(NOT_FOUND))
        );
    }

    @DisplayName("댓글 목록 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createGetArticleCommentsRequestTests")
    void getArticleCommentsTest(String ignoredMessage, List<Context<DependencyHolder>> contexts, List<Assertion<List<ArticleCommentDto>>> assertions) {
        testTemplate.performRequest(contexts, articleCommentService::getArticleComments, assertions);
    }
    static Stream<Arguments> createGetArticleCommentsRequestTests() {
        Map<String, List<Context<DependencyHolder>>> contexts = Map.of(
                VALID, List.of(new Context<>(findArticleComments, List.of(createArticleComment()), articleCommentRepoFunc))
        );
        Map<String, List<Assertion<List<ArticleCommentDto>>>> assertions = Map.of(
                VALID, List.of(new Assertion<>())
        );
        return Stream.of(Arguments.of("댓글 목록을 반환", contexts.get(VALID), assertions.get(VALID)));
    }

    @DisplayName("게시글의 댓글 목록 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createGetArticleCommentsOfArticleRequestTests")
    void getArticleCommentsOfArticleTest(String ignoredMessage, List<Context<DependencyHolder>> contexts, ArticleDto request, List<Assertion<List<ArticleCommentDto>>> assertions) {
        testTemplate.performRequest(contexts, articleCommentService::getArticleComments, request, assertions);
    }
    static Stream<Arguments> createGetArticleCommentsOfArticleRequestTests() {
        Article article = createArticle();
        ArticleDto requestDto = ArticleDto.builder().articleId(ARTICLE_ID).build();

        Map<String, List<Context<DependencyHolder>>> contexts = Map.of(
                VALID, List.of(
                        new Context<>(findArticleById, requestDto.getArticleId(), Optional.of(article), articleRepoFunc),
                        new Context<>(findArticleCommentsByArticle, article, List.of(createArticleComment()), articleCommentRepoFunc)
                ),
                NOT_FOUND, List.of(new Context<>(findArticleById, requestDto.getArticleId(), Optional.empty(), articleRepoFunc))
        );
        Map<String, List<Assertion<ArticleCommentDto>>> assertions = Map.of(
                VALID, List.of(new Assertion<>()),
                NOT_FOUND, List.of(new Assertion<>(ArticleNotFoundException.class))
        );
        return Stream.of(
                Arguments.of("회원 id를 입력하면, 댓글과 관련 댓글 리스트를 반환", contexts.get(VALID), requestDto, assertions.get(VALID)),
                Arguments.of("잘못된 회원 id를 입력하면, 조회 없이 예외를 던짐", contexts.get(NOT_FOUND), requestDto, assertions.get(NOT_FOUND))
        );
    }

    @DisplayName("회원 작성 댓글 목록 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createGetArticleCommentsOfMemberRequestTests")
    void getArticleCommentsOfMemberTest(String ignoredMessage, List<Context<DependencyHolder>> contexts, MemberDto request, List<Assertion<List<ArticleCommentDto>>> assertions) {
        testTemplate.performRequest(contexts, articleCommentService::getArticleComments, request, assertions);
    }
    static Stream<Arguments> createGetArticleCommentsOfMemberRequestTests() {
        Member member = createMember();
        MemberDto requestMemberDto = new MemberDto(MEMBER_ID);

        Map<String, List<Context<DependencyHolder>>> contexts = Map.of(
                VALID, List.of(
                        new Context<>(findMemberById, requestMemberDto.getMemberId(), Optional.of(member), memberRepoFunc),
                        new Context<>(findArticleCommentsByMember, member, List.of(createArticleComment()), articleCommentRepoFunc)
                ),
                NOT_FOUND, List.of(new Context<>(findMemberById, requestMemberDto.getMemberId(), Optional.empty(), memberRepoFunc))
        );
        Map<String, List<Assertion<ArticleCommentDto>>> assertions = Map.of(
                VALID, List.of(new Assertion<>()),
                NOT_FOUND, List.of(new Assertion<>(MemberNotFoundException.class))
        );
        return Stream.of(
                Arguments.of("회원 id를 입력하면, 댓글과 관련 댓글 리스트를 반환", contexts.get(VALID), requestMemberDto, assertions.get(VALID)),
                Arguments.of("잘못된 회원 id를 입력하면, 조회 없이 예외를 던짐", contexts.get(NOT_FOUND), requestMemberDto, assertions.get(NOT_FOUND))
        );
    }

    @DisplayName("댓글 저장")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createPostArticleCommentRequestTests")
    void PostArticleCommentTests(String ignoredMessage, List<Context<DependencyHolder>> contexts, ArticleCommentDto request, List<Assertion<Long>> assertions) {
        testTemplate.performRequest(contexts, articleCommentService::saveArticleComment, request, assertions);
    }
    static Stream<Arguments> createPostArticleCommentRequestTests() {
        Article article = createArticle();
        Member member = createMember();
        ArticleCommentDto requestDto = createArticleCommentDto();

        Map<String, List<Context<DependencyHolder>>> contexts = Map.of(
                VALID, List.of(
                        new Context<>(findMemberById, member.getId(), Optional.of(member), memberRepoFunc),
                        new Context<>(findArticleById, article.getId(), Optional.of(article), articleRepoFunc),
                        new Context<>(saveArticleComment, requestDto.toEntityForSaving(article, member), createArticleComment(), articleCommentRepoFunc)
                ),
                ARTICLE_NOT_FOUND, List.of(
                        new Context<>(findMemberById, member.getId(), Optional.of(member), memberRepoFunc),
                        new Context<>(findArticleById, article.getId(), Optional.empty(), articleRepoFunc)
                ),
                MEMBER_NOT_FOUND, List.of(new Context<>(findMemberById, requestDto.getMemberId(), Optional.empty(), memberRepoFunc))
        );
        Map<String, List<Assertion<Long>>> assertions = Map.of(
                VALID, List.of(new Assertion<>(ARTICLE_COMMENT_ID)),
                ARTICLE_NOT_FOUND, List.of(new Assertion<>(ArticleNotFoundException.class)),
                MEMBER_NOT_FOUND, List.of(new Assertion<>(MemberNotFoundException.class))
        );
        return Stream.of(
                Arguments.of("댓글 작성 정보를 입력하면, 댓글을 저장", contexts.get(VALID), requestDto, assertions.get(VALID)),
                Arguments.of("게시글이 존재하지 않는다면, 저장 없이 예외를 던짐", contexts.get(ARTICLE_NOT_FOUND), requestDto, assertions.get(ARTICLE_NOT_FOUND)),
                Arguments.of("존재하지 않는 회원의 요청이면, 저장 없이 예외를 던짐", contexts.get(MEMBER_NOT_FOUND), requestDto, assertions.get(MEMBER_NOT_FOUND))
        );
    }

    @DisplayName("댓글 수정")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("UpdateArticleCommentRequestTests")
    void UpdateArticleCommentTests(String ignoredMessage, List<Context<DependencyHolder>> contexts, ArticleCommentDto request, List<Assertion<ArticleComment>> assertions, ArticleComment target) {
        testTemplate.performRequest(contexts, articleCommentService::updateArticleComment, request, assertions, target);
    }
    static Stream<Arguments> UpdateArticleCommentRequestTests() {
        ArticleComment articleComment = createArticleComment();
        ArticleComment forbiddenArticleComment = createArticleComment();
        ReflectionTestUtils.setField(forbiddenArticleComment.getMember(), "id", MEMBER_ID+1);

        ArticleCommentDto requestDto = ArticleCommentDto.builder().articleCommentId(ARTICLE_COMMENT_ID).content("New").build();

        Map<String, List<Context<DependencyHolder>>> contexts = Map.of(
                VALID, List.of(new Context<>(findArticleCommentById, ARTICLE_COMMENT_ID, Optional.of(articleComment), articleCommentRepoFunc)),
                NOT_FOUND, List.of(new Context<>(findArticleCommentById, ARTICLE_COMMENT_ID, Optional.empty(), articleCommentRepoFunc)),
                FORBIDDEN, List.of(new Context<>(findArticleCommentById, ARTICLE_COMMENT_ID, Optional.of(forbiddenArticleComment), articleCommentRepoFunc))
        );
        Map<String, List<Assertion<ArticleComment>>> assertions = Map.of(
                VALID, List.of(new Assertion<>(Map.of("content", requestDto.getContent()))),
                NOT_FOUND, List.of(new Assertion<>(ArticleCommentNotFoundException.class)),
                FORBIDDEN, List.of(new Assertion<>(ForbiddenException.class))
        );
        return Stream.of(
                Arguments.of("id와 댓글 수정 정보를 입력하면, 댓글을 수정", contexts.get(VALID), requestDto, assertions.get(VALID), articleComment),
                Arguments.of("잘못된 id를 입력하면, 수정 없이 예외를 던짐", contexts.get(NOT_FOUND), requestDto, assertions.get(NOT_FOUND), null),
                Arguments.of("잘못된 회원이 접근하면, 수정 없이 예외를 던짐", contexts.get(FORBIDDEN), requestDto, assertions.get(FORBIDDEN), null)
        );
    }

    @DisplayName("댓글 비활성화")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createInactiveArticleCommentRequestTests")
    void deactivateArticleCommentTest(String ignoredMessage, List<Context<DependencyHolder>> contexts, ArticleCommentDto request, List<Assertion<ArticleComment>> assertions, ArticleComment target) {
        testTemplate.performRequest(contexts, articleCommentService::softDeleteArticleComment, request, assertions, target);
    }
    static Stream<Arguments> createInactiveArticleCommentRequestTests() {
        ArticleComment articleComment = createArticleComment();
        ArticleComment forbiddenArticleComment = createArticleComment();
        ReflectionTestUtils.setField(forbiddenArticleComment.getMember(), "id", MEMBER_ID+1);

        ArticleCommentDto requestDto = ArticleCommentDto.builder().articleCommentId(ARTICLE_COMMENT_ID).build();

        Map<String, List<Context<DependencyHolder>>> contexts = Map.of(
                VALID, List.of(new Context<>(findArticleCommentById, ARTICLE_COMMENT_ID, Optional.of(articleComment), articleCommentRepoFunc)),
                NOT_FOUND, List.of(new Context<>(findArticleCommentById, ARTICLE_COMMENT_ID, Optional.empty(), articleCommentRepoFunc)),
                FORBIDDEN, List.of(new Context<>(findArticleCommentById, ARTICLE_COMMENT_ID, Optional.of(forbiddenArticleComment), articleCommentRepoFunc))
        );
        Map<String, List<Assertion<ArticleComment>>> assertions = Map.of(
                VALID, List.of(new Assertion<>(Map.of("isActive", false))),
                NOT_FOUND, List.of(new Assertion<>(ArticleCommentNotFoundException.class)),
                FORBIDDEN, List.of(new Assertion<>(ForbiddenException.class))
        );
        return Stream.of(
                Arguments.of("id를 입력하면, 댓글을 비활성화", contexts.get(VALID), requestDto, assertions.get(VALID), articleComment),
                Arguments.of("잘못된 id를 입력하면, 비활성화 없이 예외를 던짐", contexts.get(NOT_FOUND), requestDto, assertions.get(NOT_FOUND), null),
                Arguments.of("잘못된 회원이 접근하면, 비활성화 없이 예외를 던짐", contexts.get(FORBIDDEN), requestDto, assertions.get(FORBIDDEN), null)
        );
    }

    @DisplayName("댓글 삭제")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createDeleteArticleCommentRequestTests")
    void deleteArticleCommentTest(String ignoredMessage, List<Context<DependencyHolder>> contexts, ArticleCommentDto request, List<Assertion<ArticleComment>> assertions) {
        testTemplate.performRequest(contexts, articleCommentService::hardDeleteArticleComment, request, assertions, null);
    }
    static Stream<Arguments> createDeleteArticleCommentRequestTests() {
        ArticleComment articleComment = createArticleComment();
        ArticleComment forbiddenArticleComment = createArticleComment();
        ReflectionTestUtils.setField(forbiddenArticleComment.getMember(), "id", MEMBER_ID+1);

        ArticleCommentDto requestDto = ArticleCommentDto.builder().articleCommentId(ARTICLE_COMMENT_ID).build();

        Map<String, List<Context<DependencyHolder>>> contexts = Map.of(
                VALID, List.of(
                        new Context<>(findArticleCommentById, ARTICLE_COMMENT_ID, Optional.of(articleComment), articleCommentRepoFunc),
                        new Context<>(deleteArticleComment, articleComment, articleCommentRepoFunc)
                ),
                NOT_FOUND, List.of(new Context<>(findArticleCommentById, ARTICLE_COMMENT_ID, Optional.empty(), articleCommentRepoFunc)),
                FORBIDDEN, List.of(new Context<>(findArticleCommentById, ARTICLE_COMMENT_ID, Optional.of(forbiddenArticleComment), articleCommentRepoFunc))
        );
        Map<String, List<Assertion<ArticleComment>>> assertions = Map.of(
                VALID, List.of(),
                NOT_FOUND, List.of(new Assertion<>(ArticleCommentNotFoundException.class)),
                FORBIDDEN, List.of(new Assertion<>(ForbiddenException.class))
        );
        return Stream.of(
                Arguments.of("id를 입력하면, 댓글을 삭제", contexts.get(VALID), requestDto, assertions.get(VALID)),
                Arguments.of("잘못된 id를 입력하면, 삭제 없이 예외를 던짐", contexts.get(NOT_FOUND), requestDto, assertions.get(NOT_FOUND)),
                Arguments.of("잘못된 회원이 접근하면, 삭제 없이 예외를 던짐", contexts.get(FORBIDDEN), requestDto, assertions.get(FORBIDDEN))
        );
    }
}