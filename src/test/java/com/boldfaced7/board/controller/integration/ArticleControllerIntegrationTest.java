package com.boldfaced7.board.controller.integration;

import com.boldfaced7.board.auth.SessionConst;
import com.boldfaced7.board.controller.ControllerTestTemplate;
import com.boldfaced7.board.domain.Article;
import com.boldfaced7.board.dto.request.SaveArticleRequest;
import com.boldfaced7.board.dto.request.UpdateArticleRequest;
import com.boldfaced7.board.service.ArticleService;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

import static com.boldfaced7.board.TestUtil.*;

@DisplayName("ArticleController 통합 테스트")
@SpringBootTest
@AutoConfigureMockMvc
class ArticleControllerIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired Gson gson;
    @Autowired ArticleService articleService;
    ControllerTestTemplate<ArticleService> testTemplate;
    MockHttpSession session;

    @BeforeEach
    void setSessionAndTestTemplate() {
        session = new MockHttpSession();
        session.setAttribute(SessionConst.AUTH_RESPONSE, createAuthResponse());
        testTemplate = new ControllerTestTemplate<>(mvc, gson, session, articleService);
    }

    @DisplayName("[GET] 게시글 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createGetArticleRequestTests")
    void GetArticleTest(String ignoredMessage, Long articleId, List<ResultMatcher> resultMatchers) throws Exception {
        testTemplate.doGet(null, articleUrl(articleId), resultMatchers);
    }
    static Stream<Arguments> createGetArticleRequestTests() {
        List<ResultMatcher> exists = exists(List.of("articleId", "title", "content", "author"), "");
        List<ResultMatcher> resultMatchers = Stream.of(exists, ok(), contentTypeJson()).flatMap(List::stream).toList();

        return Stream.of(
                Arguments.of("정상 호출", 1L, resultMatchers),
                Arguments.of("존재하지 않는 게시글", 100000L, notFound())
        );
    }

    @DisplayName("[GET] 게시글 리스트 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createGetArticlesRequestTest")
    void GetArticlesTest(String ignoredMessage, List<ResultMatcher> resultMatchers) throws Exception {
        testTemplate.doGet(null, articleUrl(), resultMatchers);
    }
    static Stream<Arguments> createGetArticlesRequestTest() {
        List<ResultMatcher> exists = exists(List.of("articleId", "title", "content", "author"), ".articles.content[0]");
        List<ResultMatcher> resultMatchers = Stream.of(exists, ok(), contentTypeJson()).flatMap(List::stream).toList();

        return Stream.of(
                Arguments.of("정상 호출", resultMatchers)
        );
    }

    @DisplayName("[GET] 회원 작성 게시글 리스트 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createGetArticlesOfMemberRequestTest")
    void GetArticlesOfMemberTest(String ignoredMessage, Long memberId, List<ResultMatcher> resultMatchers) throws Exception {
        testTemplate.doGet(null, memberArticleUrl(memberId), resultMatchers);
    }
    static Stream<Arguments> createGetArticlesOfMemberRequestTest() {
        List<ResultMatcher> exists = exists(List.of("articleId", "title", "content", "author"), ".articles.content[0]");
        List<ResultMatcher> resultMatchers = Stream.of(exists, ok(), contentTypeJson()).flatMap(List::stream).toList();

        return Stream.of(
                Arguments.of("정상 호출", 1L, resultMatchers),
                Arguments.of("존재하지 않는 회원", 100000L, notFound())
        );
    }

    @DisplayName("[POST] 게시글 등록")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createPostRequestTests")
    <T> void PostArticleTest(String ignoredMessage, T request, List<ResultMatcher> resultMatchers) throws Exception {
        testTemplate.doPost(null, articleUrl(), request, resultMatchers);
    }
    static Stream<Arguments> createPostRequestTests() {
        return Stream.of(
                Arguments.of("정상 호출", new SaveArticleRequest(TITLE, CONTENT), created()),
                Arguments.of("비정상 호출: 제목 누락", new SaveArticleRequest("", CONTENT), badRequest()),
                Arguments.of("비정상 호출: 내용 누락", new SaveArticleRequest(TITLE, ""), badRequest()),
                Arguments.of("비정상 호출: 제목 길이 초과", new SaveArticleRequest("a".repeat(Article.MAX_TITLE_LENGTH + 1), CONTENT), badRequest()),
                Arguments.of("비정상 호출: 내용 길이 초과", new SaveArticleRequest(TITLE, "a".repeat(Article.MAX_CONTENT_LENGTH + 1)), badRequest())
        );
    }

    @DisplayName("[PATCH] 게시글 수정")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createPatchRequestTests")
    <T> void PatchArticleTest(String ignoredMessage, Long articleId, T request, List<ResultMatcher> resultMatchers) throws Exception {
        testTemplate.doPatch(null, articleUrl(articleId), request, resultMatchers);
    }
    static Stream<Arguments> createPatchRequestTests() {
        return Stream.of(
                Arguments.of("정상 호출", 1L, new UpdateArticleRequest(TITLE, CONTENT), ok()),
                Arguments.of("비정상 호출: 존재하지 않는 게시글", 100000L, new UpdateArticleRequest(TITLE, CONTENT), notFound()),
                Arguments.of("비정상 호출: 타 회원 시도", 2L, new UpdateArticleRequest(TITLE, CONTENT), forbidden()),
                Arguments.of("비정상 호출: 제목 누락", 1L, new UpdateArticleRequest("", CONTENT), badRequest()),
                Arguments.of("비정상 호출: 내용 누락", 1L, new UpdateArticleRequest(TITLE, ""), badRequest()),
                Arguments.of("비정상 호출: 제목 길이 초과", 1L, new UpdateArticleRequest("a".repeat(Article.MAX_TITLE_LENGTH + 1), CONTENT), badRequest()),
                Arguments.of("비정상 호출: 내용 길이 초과", 1L, new UpdateArticleRequest(TITLE, "a".repeat(Article.MAX_CONTENT_LENGTH + 1)), badRequest())
        );
    }

    @DisplayName("[DELETE] 게시글 삭제")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createDeleteRequestTests")
    void deleteArticleTest(String ignoredMessage, Long articleId, List<ResultMatcher> resultMatchers) throws Exception {
        testTemplate.doDelete(null, articleUrl(articleId), resultMatchers);
    }
    static Stream<Arguments> createDeleteRequestTests() {
        return Stream.of(
                Arguments.of("정상 호출", 1L, ok()),
                Arguments.of("비정상 호출: 존재하지 않는 게시글", 100000L, notFound()),
                Arguments.of("비정상 호출: 타 회원 시도", 2L, forbidden())
        );
    }
}