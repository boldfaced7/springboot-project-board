package com.boldfaced7.board.controller.integration;

import com.boldfaced7.board.auth.AuthInfoHolder;
import com.boldfaced7.board.auth.SessionConst;
import com.boldfaced7.board.controller.ControllerTestTemplate;
import com.boldfaced7.board.dto.request.SaveArticleCommentRequest;
import com.boldfaced7.board.dto.request.UpdateArticleCommentRequest;
import com.boldfaced7.board.service.ArticleCommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

import static com.boldfaced7.board.TestUtil.*;

@ActiveProfiles("test")
@DisplayName("ArticleCommentController 통합 테스트")
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class ArticleCommentControllerIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired ArticleCommentService articleCommentService;
    ControllerTestTemplate<ArticleCommentService> testTemplate;
    MockHttpSession session;

    @BeforeEach
    void setSessionAndTestTemplate() {
        session = new MockHttpSession();
        AuthInfoHolder.setAuthInfo(authResponse());
        testTemplate = new ControllerTestTemplate<>(mvc, session, articleCommentService);
    }

    @DisplayName("[GET] 댓글 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createGetArticleCommentRequestTests")
    void GetArticleCommentTest(String ignoredMessage, Long articleCommentId, ResultMatcher status) throws Exception {
        testTemplate.doGet(articleArticleCommentUrl(1L, articleCommentId), status);
    }
    static Stream<Arguments> createGetArticleCommentRequestTests() {
        return Stream.of(
                Arguments.of("정상 호출", 1L, OK),
                Arguments.of("비정상 호출: 존재하지 않는 댓글", 100000L, NOT_FOUND)
        );
    }

    @DisplayName("[GET] 댓글 리스트 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createGetArticleCommentsRequestTest")
    void GetArticleCommentsTest(String ignoredMessage, ResultMatcher status) throws Exception {
        testTemplate.doGet(articleCommentUrl(), status);
    }
    static Stream<Arguments> createGetArticleCommentsRequestTest() {
        return Stream.of(
                Arguments.of("정상 호출", OK)
        );
    }

    @DisplayName("[GET] 게시글의 댓글 리스트 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createGetArticleCommentsOfArticleRequestTest")
    void GetArticleCommentsOfArticleTest(String ignoredMessage, Long articleId, ResultMatcher status) throws Exception {
        testTemplate.doGet(articleArticleCommentUrl(articleId), status);
    }
    static Stream<Arguments> createGetArticleCommentsOfArticleRequestTest() {
        return Stream.of(
                Arguments.of("정상 호출", 1L, OK),
                Arguments.of("비정상 호출: 존재하지 않는 게시글", 100000L, NOT_FOUND)
        );
    }

    @DisplayName("[GET] 회원 작성 댓글 리스트 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createGetArticleCommentsOfMemberRequestTest")
    void GetArticleCommentsOfMemberTest(String ignoredMessage, Long memberId, ResultMatcher status) throws Exception {
        testTemplate.doGet(memberArticleCommentUrl(memberId), status);
    }
    static Stream<Arguments> createGetArticleCommentsOfMemberRequestTest() {
        return Stream.of(
                Arguments.of("정상 호출", 1L, OK),
                Arguments.of("비정상 호출: 타 회원 시도", 2L, FORBIDDEN)
        );
    }

    @DisplayName("[POST] 댓글 등록")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createPostRequestTests")
    void PostArticleCommentTest(String ignoredMessage, String content, ResultMatcher status) throws Exception {
        testTemplate.doPost(articleArticleCommentUrl(1L), new SaveArticleCommentRequest(content), status);
    }
    static Stream<Arguments> createPostRequestTests() {
        return Stream.of(
                Arguments.of("정상 호출", CONTENT, CREATED),
                Arguments.of("비정상 호출: 내용 누락", "", BAD_REQUEST),
                Arguments.of("비정상 호출: 내용 길이 초과", EXCEEDED_COMMENT_CONTENT, BAD_REQUEST)
        );
    }

    @DisplayName("[PATCH] 댓글 수정")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createPatchRequestTests")
    void PatchArticleCommentTest(String ignoredMessage, Long articleId, Long articleCommentId, String content, ResultMatcher status) throws Exception {
        testTemplate.doPatch(articleArticleCommentUrl(articleId, articleCommentId), new UpdateArticleCommentRequest(content), status);
    }
    static Stream<Arguments> createPatchRequestTests() {
        return Stream.of(
                Arguments.of("정상 호출", 1L, 1L, CONTENT, OK),
                Arguments.of("비정상 호출: 존재하지 않는 댓글", 1L, 100000L, CONTENT, NOT_FOUND),
                Arguments.of("비정상 호출: 타 회원 시도", 2L, 2L, CONTENT, FORBIDDEN),
                Arguments.of("비정상 호출: 내용 누락", 1L, 1L, "", BAD_REQUEST),
                Arguments.of("비정상 호출: 내용 길이 초과", 1L, 1L, EXCEEDED_COMMENT_CONTENT, BAD_REQUEST)
        );
    }

    @DisplayName("[DELETE] 댓글 삭제")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createDeleteRequestTests")
    void DeleteArticleCommentTest(String ignoredMessage, Long articleId, Long articleCommentId, ResultMatcher status) throws Exception {
        testTemplate.doDelete(articleArticleCommentUrl(articleId, articleCommentId), status);
    }
    static Stream<Arguments> createDeleteRequestTests() {
        return Stream.of(
                Arguments.of("정상 호출", 1L, 1L, OK),
                Arguments.of("비정상 호출: 존재하지 않는 댓글", 1L, 100000L, NOT_FOUND),
                Arguments.of("비정상 호출: 타 회원 시도", 2L, 2L, FORBIDDEN)
        );
    }
}