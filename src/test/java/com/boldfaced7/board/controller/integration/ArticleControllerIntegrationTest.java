package com.boldfaced7.board.controller.integration;

import com.boldfaced7.board.auth.SessionConst;
import com.boldfaced7.board.controller.ControllerTestTemplate;
import com.boldfaced7.board.dto.request.SaveArticleRequest;
import com.boldfaced7.board.dto.request.UpdateArticleRequest;
import com.boldfaced7.board.service.ArticleService;
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

import java.util.stream.Stream;

import static com.boldfaced7.board.TestUtil.*;

@DisplayName("ArticleController 통합 테스트")
@SpringBootTest
@AutoConfigureMockMvc
class ArticleControllerIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired ArticleService articleService;
    ControllerTestTemplate<ArticleService> testTemplate;
    MockHttpSession session;

    @BeforeEach
    void setSessionAndTestTemplate() {
        session = new MockHttpSession();
        session.setAttribute(SessionConst.AUTH_RESPONSE, authResponse());
        testTemplate = new ControllerTestTemplate<>(mvc, session, articleService);
    }

    @DisplayName("[GET] 게시글 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createGetArticleRequestTests")
    void GetArticleTest(String ignoredMessage, Long articleId, ResultMatcher status) throws Exception {
        testTemplate.doGet(articleUrl(articleId), status);
    }
    static Stream<Arguments> createGetArticleRequestTests() {
        return Stream.of(
                Arguments.of("정상 호출", 1L, OK),
                Arguments.of("존재하지 않는 게시글", 100000L, NOT_FOUND)
        );
    }

    @DisplayName("[GET] 게시글 리스트 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createGetArticlesRequestTest")
    void GetArticlesTest(String ignoredMessage, ResultMatcher status) throws Exception {
        testTemplate.doGet(articleUrl(), status);
    }
    static Stream<Arguments> createGetArticlesRequestTest() {
        return Stream.of(
                Arguments.of("정상 호출", OK)
        );
    }

    @DisplayName("[GET] 회원 작성 게시글 리스트 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createGetArticlesOfMemberRequestTest")
    void GetArticlesOfMemberTest(String ignoredMessage, Long memberId, ResultMatcher status) throws Exception {
        testTemplate.doGet(memberArticleUrl(memberId), status);
    }
    static Stream<Arguments> createGetArticlesOfMemberRequestTest() {
        return Stream.of(
                Arguments.of("정상 호출", 1L, OK),
                Arguments.of("존재하지 않는 회원", 100000L, NOT_FOUND)
        );
    }

    @DisplayName("[POST] 게시글 등록")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createPostRequestTests")
    void PostArticleTest(String ignoredMessage, String title, String content, ResultMatcher status) throws Exception {
        testTemplate.doPost(articleUrl(), new SaveArticleRequest(title, content), status);
    }
    static Stream<Arguments> createPostRequestTests() {
        return Stream.of(
                Arguments.of("정상 호출", TITLE, CONTENT, CREATED),
                Arguments.of("비정상 호출: 제목 누락", "", CONTENT, BAD_REQUEST),
                Arguments.of("비정상 호출: 내용 누락", TITLE, "", BAD_REQUEST),
                Arguments.of("비정상 호출: 제목 길이 초과", EXCEEDED_ARTICLE_TITLE, CONTENT, BAD_REQUEST),
                Arguments.of("비정상 호출: 내용 길이 초과", TITLE, EXCEEDED_ARTICLE_CONTENT, BAD_REQUEST)
        );
    }

    @DisplayName("[PATCH] 게시글 수정")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createPatchRequestTests")
    void PatchArticleTest(String ignoredMessage, Long articleId, String title, String content, ResultMatcher status) throws Exception {
        testTemplate.doPatch(articleUrl(articleId), new UpdateArticleRequest(title, content), status);
    }
    static Stream<Arguments> createPatchRequestTests() {
        return Stream.of(
                Arguments.of("정상 호출", 1L, TITLE, CONTENT, OK),
                Arguments.of("비정상 호출: 존재하지 않는 게시글", 100000L, TITLE, CONTENT, NOT_FOUND),
                Arguments.of("비정상 호출: 타 회원 시도", 2L, TITLE, CONTENT, FORBIDDEN),
                Arguments.of("비정상 호출: 제목 누락", 1L, "", CONTENT, BAD_REQUEST),
                Arguments.of("비정상 호출: 내용 누락", 1L, TITLE, "", BAD_REQUEST),
                Arguments.of("비정상 호출: 제목 길이 초과", 1L, EXCEEDED_ARTICLE_TITLE, CONTENT, BAD_REQUEST),
                Arguments.of("비정상 호출: 내용 길이 초과", 1L, TITLE, EXCEEDED_ARTICLE_CONTENT, BAD_REQUEST)
        );
    }

    @DisplayName("[DELETE] 게시글 삭제")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createDeleteRequestTests")
    void deleteArticleTest(String ignoredMessage, Long articleId, ResultMatcher status) throws Exception {
        testTemplate.doDelete(articleUrl(articleId), status);
    }
    static Stream<Arguments> createDeleteRequestTests() {
        return Stream.of(
                Arguments.of("정상 호출", 1L, OK),
                Arguments.of("비정상 호출: 존재하지 않는 게시글", 100000L, NOT_FOUND),
                Arguments.of("비정상 호출: 타 회원 시도", 2L, FORBIDDEN)
        );
    }
}