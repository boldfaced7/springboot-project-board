package com.boldfaced7.board.controller.integration;

import com.boldfaced7.board.auth.SessionConst;
import com.boldfaced7.board.controller.ControllerTestTemplate;
import com.boldfaced7.board.domain.ArticleComment;
import com.boldfaced7.board.dto.request.SaveArticleCommentRequest;
import com.boldfaced7.board.dto.request.UpdateArticleCommentRequest;
import com.boldfaced7.board.service.ArticleCommentService;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.List;
import java.util.stream.Stream;

import static com.boldfaced7.board.TestUtil.*;

@DisplayName("ArticleCommentController 통합 테스트")
@SpringBootTest
@AutoConfigureMockMvc
class ArticleCommentControllerIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired Gson gson;
    @Autowired ArticleCommentService articleCommentService;
    ControllerTestTemplate<ArticleCommentService> testTemplate;
    MockHttpSession session;

    @BeforeEach
    void setSessionAndTestTemplate() {
        session = new MockHttpSession();
        session.setAttribute(SessionConst.AUTH_RESPONSE, createAuthResponse());
        testTemplate = new ControllerTestTemplate<>(mvc, gson, session, articleCommentService);
    }

    @DisplayName("[GET] 댓글 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createGetArticleCommentRequestTests")
    void GetArticleCommentTest(String ignoredMessage, Long articleCommentId, List<ResultMatcher> resultMatchers) throws Exception {
        testTemplate.doGet(null, articleArticleCommentUrl(articleCommentId, articleCommentId), resultMatchers);
    }
    static Stream<Arguments> createGetArticleCommentRequestTests() {
        List<ResultMatcher> exists = exists(List.of("articleId", "articleCommentId", "content", "author"), "");
        List<ResultMatcher> resultMatchers = Stream.of(exists, ok(), contentTypeJson()).flatMap(List::stream).toList();

        return Stream.of(
                Arguments.of("정상 호출", 1L, resultMatchers),
                Arguments.of("비정상 호출: 존재하지 않는 댓글", 100000L, notFound())
        );
    }

    @DisplayName("[GET] 댓글 리스트 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createGetArticleCommentsRequestTest")
    void GetArticleCommentsTest(String ignoredMessage, List<ResultMatcher> resultMatchers) throws Exception {
        testTemplate.doGet(null, articleCommentUrl(), resultMatchers);
    }
    static Stream<Arguments> createGetArticleCommentsRequestTest() {
        List<ResultMatcher> exists = exists(List.of("articleCommentId", "content", "author"), ".articleComments.content[0]");
        List<ResultMatcher> resultMatchers = Stream.of(exists, ok(), contentTypeJson()).flatMap(List::stream).toList();

        return Stream.of(
                Arguments.of("정상 호출", resultMatchers)
        );
    }

    @DisplayName("[GET] 게시글의 댓글 리스트 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createGetArticleCommentsOfArticleRequestTest")
    void GetArticleCommentsOfArticleTest(String ignoredMessage, Long articleId, List<ResultMatcher> resultMatchers) throws Exception {
        testTemplate.doGet(null, articleArticleCommentUrl(articleId), resultMatchers);
    }
    static Stream<Arguments> createGetArticleCommentsOfArticleRequestTest() {
        List<ResultMatcher> exists = exists(List.of("articleCommentId", "content", "author"), ".articleComments.content[0]");
        List<ResultMatcher> resultMatchers = Stream.of(exists, ok(), contentTypeJson()).flatMap(List::stream).toList();

        return Stream.of(
                Arguments.of("정상 호출", 1L, resultMatchers),
                Arguments.of("비정상 호출: 존재하지 않는 게시글", 100000L, notFound())
        );
    }

    @DisplayName("[GET] 회원 작성 댓글 리스트 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createGetArticleCommentsOfMemberRequestTest")
    void GetArticleCommentsOfMemberTest(String ignoredMessage, Long memberId, List<ResultMatcher> resultMatchers) throws Exception {
        testTemplate.doGet(null, memberArticleCommentUrl(memberId), resultMatchers);
    }
    static Stream<Arguments> createGetArticleCommentsOfMemberRequestTest() {
        List<ResultMatcher> exists = exists(List.of("articleCommentId", "content", "author"), ".articleComments.content[0]");
        List<ResultMatcher> resultMatchers = Stream.of(exists, ok(), contentTypeJson()).flatMap(List::stream).toList();

        return Stream.of(
                Arguments.of("정상 호출", 1L, resultMatchers),
                Arguments.of("비정상 호출: 타 회원 시도", 2L, forbidden())
        );
    }

    @DisplayName("[POST] 댓글 등록")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createPostRequestTests")
    <T> void PostArticleCommentTest(String ignoredMessage, T request, List<ResultMatcher> resultMatchers) throws Exception {
        testTemplate.doPost(null, articleArticleCommentUrl(ARTICLE_ID), request, resultMatchers);
    }
    static Stream<Arguments> createPostRequestTests() {
        return Stream.of(
                Arguments.of("정상 호출", new SaveArticleCommentRequest(CONTENT), created()),
                Arguments.of("비정상 호출: 내용 누락", new SaveArticleCommentRequest(""), badRequest()),
                Arguments.of("비정상 호출: 내용 길이 초과", new SaveArticleCommentRequest("a".repeat(ArticleComment.MAX_CONTENT_LENGTH + 1)), badRequest())
        );
    }

    @DisplayName("[PATCH] 댓글 수정")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createPatchRequestTests")
    <T> void PatchArticleCommentTest(String ignoredMessage, Long articleId, Long articleCommentId, T request, List<ResultMatcher> resultMatchers) throws Exception {
        testTemplate.doPatch(null, articleArticleCommentUrl(articleId, articleCommentId), request, resultMatchers);
    }
    static Stream<Arguments> createPatchRequestTests() {
        return Stream.of(
                Arguments.of("정상 호출", 1L, 1L, new UpdateArticleCommentRequest(CONTENT), ok()),
                Arguments.of("비정상 호출: 존재하지 않는 댓글", 1L, 100000L, new UpdateArticleCommentRequest(CONTENT), notFound()),
                Arguments.of("비정상 호출: 타 회원 시도", 2L, 2L, new UpdateArticleCommentRequest(CONTENT), forbidden()),
                Arguments.of("비정상 호출: 내용 누락", 1L, 1L, new UpdateArticleCommentRequest(""), badRequest()),
                Arguments.of("비정상 호출: 내용 길이 초과", 1L, 1L, new UpdateArticleCommentRequest("a".repeat(ArticleComment.MAX_CONTENT_LENGTH + 1)), badRequest())
        );
    }

    @DisplayName("[DELETE] 댓글 삭제")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createDeleteRequestTests")
    void DeleteArticleCommentTest(String ignoredMessage, Long articleId, Long articleCommentId, List<ResultMatcher> resultMatchers) throws Exception {
        testTemplate.doDelete(null, articleArticleCommentUrl(articleId, articleCommentId), resultMatchers);
    }
    static Stream<Arguments> createDeleteRequestTests() {
        return Stream.of(
                Arguments.of("정상 호출", 1L, 1L, ok()),
                Arguments.of("비정상 호출: 존재하지 않는 댓글", 1L, 100000L, notFound()),
                Arguments.of("비정상 호출: 타 회원 시도", 2L, 2L, forbidden())
        );
    }
}