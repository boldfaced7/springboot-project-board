package com.boldfaced7.board.controller;

import com.boldfaced7.board.Context;
import com.boldfaced7.board.auth.SessionConst;
import com.boldfaced7.board.domain.Article;
import com.boldfaced7.board.dto.ArticleDto;
import com.boldfaced7.board.dto.MemberDto;
import com.boldfaced7.board.dto.request.SaveArticleRequest;
import com.boldfaced7.board.dto.request.UpdateArticleRequest;
import com.boldfaced7.board.error.exception.article.ArticleNotFoundException;
import com.boldfaced7.board.error.exception.auth.ForbiddenException;
import com.boldfaced7.board.error.exception.member.MemberNotFoundException;
import com.boldfaced7.board.service.ArticleService;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.boldfaced7.board.TestUtil.*;
import static com.boldfaced7.board.ServiceMethod.*;

@DisplayName("ArticleController 테스트")
@WebMvcTest({ArticleController.class})
class ArticleControllerTest {

    @Autowired MockMvc mvc;
    @Autowired Gson gson;
    @MockBean ArticleService articleService;
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
    void GetArticleTest(String ignoredMessage, Context<ArticleService> context, List<ResultMatcher> resultMatchers) throws Exception {
        testTemplate.doGet(context, articleUrl(ARTICLE_ID), resultMatchers);
    }
    static Stream<Arguments> createGetArticleRequestTests() {
        ArticleDto requestDto = new ArticleDto(ARTICLE_ID, PageRequest.of(0, 20));

        Map<String, Context<ArticleService>> contexts = Map.of(
                VALID, new Context<>(getArticle, requestDto, createArticleDto()),
                NOT_FOUND, new Context<>(getArticle, requestDto, new ArticleNotFoundException())
        );
        List<ResultMatcher> exists = exists(List.of("articleId", "title", "content", "author"), "");
        List<ResultMatcher> resultMatchers = Stream.of(exists, ok(), contentTypeJson()).flatMap(List::stream).toList();

        return Stream.of(
                Arguments.of("정상 호출", contexts.get(VALID), resultMatchers),
                Arguments.of("존재하지 않는 게시글", contexts.get(NOT_FOUND), notFound())
        );
    }

    @DisplayName("[GET] 게시글 리스트 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createGetArticlesRequestTest")
    void GetArticlesTest(String ignoredMessage, Context<ArticleService> context, List<ResultMatcher> resultMatchers) throws Exception {
        testTemplate.doGet(context, articleUrl(), resultMatchers);
    }
    static Stream<Arguments> createGetArticlesRequestTest() {
        Map<String, Context<ArticleService>> contexts = Map.of(
                VALID, new Context<>(getArticles, PageRequest.of(0, 20), new PageImpl<>(List.of(createArticleDto())))
        );
        List<ResultMatcher> exists = exists(List.of("articleId", "title", "content", "author"), ".articles.content[0]");
        List<ResultMatcher> resultMatchers = Stream.of(exists, ok(), contentTypeJson()).flatMap(List::stream).toList();

        return Stream.of(
                Arguments.of("정상 호출", contexts.get(VALID), resultMatchers)
        );
    }

    @DisplayName("[GET] 회원 작성 게시글 리스트 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createGetArticlesOfMemberRequestTest")
    void GetArticlesOfMemberTest(String ignoredMessage, Context<ArticleService> context, List<ResultMatcher> resultMatchers) throws Exception {
        testTemplate.doGet(context, memberArticleUrl(MEMBER_ID), resultMatchers);
    }
    static Stream<Arguments> createGetArticlesOfMemberRequestTest() {
        MemberDto validMemberRequestDto = new MemberDto(MEMBER_ID, PageRequest.of(0, 20));

        Map<String, Context<ArticleService>> contexts = Map.of(
                VALID, new Context<>(getArticlesOfMember, validMemberRequestDto, new PageImpl<>(List.of(createArticleDto()))),
                NOT_FOUND, new Context<>(getArticlesOfMember, validMemberRequestDto, new MemberNotFoundException())
        );
        List<ResultMatcher> exists = exists(List.of("articleId", "title", "content", "author"), ".articles.content[0]");
        List<ResultMatcher> resultMatchers = Stream.of(exists, ok(), contentTypeJson()).flatMap(List::stream).toList();

        return Stream.of(
                Arguments.of("정상 호출", contexts.get(VALID), resultMatchers),
                Arguments.of("존재하지 않는 회원", contexts.get(NOT_FOUND), notFound())
        );
    }

    @DisplayName("[POST] 게시글 등록")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createPostRequestTests")
    <T> void PostArticleTest(String ignoredMessage, Context<ArticleService> context, T request, List<ResultMatcher> resultMatchers) throws Exception {
        testTemplate.doPost(context, articleUrl(), request, resultMatchers);
    }
    static Stream<Arguments> createPostRequestTests() {
        SaveArticleRequest validRequest = new SaveArticleRequest(TITLE, CONTENT);
        ArticleDto validRequestDto = validRequest.toDto();

        Map<String, Context<ArticleService>> contexts = Map.of(
                VALID, new Context<>(saveArticle, validRequestDto, ARTICLE_ID)
        );
        return Stream.of(
                Arguments.of("정상 호출", contexts.get(VALID), validRequest, created()),
                Arguments.of("비정상 호출: 제목 누락", null, new SaveArticleRequest("", CONTENT), badRequest()),
                Arguments.of("비정상 호출: 내용 누락", null, new SaveArticleRequest(TITLE, ""), badRequest()),
                Arguments.of("비정상 호출: 제목 길이 초과", null, new SaveArticleRequest("a".repeat(Article.MAX_TITLE_LENGTH + 1), CONTENT), badRequest()),
                Arguments.of("비정상 호출: 내용 길이 초과", null, new SaveArticleRequest(TITLE, "a".repeat(Article.MAX_CONTENT_LENGTH + 1)), badRequest())
        );
    }

    @DisplayName("[PATCH] 게시글 수정")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createPatchRequestTests")
    <T> void PatchArticleTest(String ignoredMessage, Context<ArticleService> context, T request, List<ResultMatcher> resultMatchers) throws Exception {
        testTemplate.doPatch(context, articleUrl(ARTICLE_ID), request, resultMatchers);
    }
    static Stream<Arguments> createPatchRequestTests() {
        UpdateArticleRequest validRequest = new UpdateArticleRequest(TITLE, CONTENT);
        ArticleDto validRequestDto = validRequest.toDto(ARTICLE_ID);

        Map<String, Context<ArticleService>> contexts = Map.of(
                VALID, new Context<>(updateArticle, validRequestDto),
                NOT_FOUND, new Context<>(updateArticle, validRequestDto, new ArticleNotFoundException()),
                FORBIDDEN, new Context<>(updateArticle, validRequestDto, new ForbiddenException())
        );
        return Stream.of(
                Arguments.of("정상 호출", contexts.get(VALID), validRequest, ok()),
                Arguments.of("비정상 호출: 존재하지 않는 게시글", contexts.get(NOT_FOUND), validRequest, notFound()),
                Arguments.of("비정상 호출: 타 회원 시도", contexts.get(FORBIDDEN), validRequest, forbidden()),
                Arguments.of("비정상 호출: 제목 누락", null, new UpdateArticleRequest("", CONTENT), badRequest()),
                Arguments.of("비정상 호출: 내용 누락", null, new UpdateArticleRequest(TITLE, ""), badRequest()),
                Arguments.of("비정상 호출: 제목 길이 초과", null, new UpdateArticleRequest("a".repeat(Article.MAX_TITLE_LENGTH + 1), CONTENT), badRequest()),
                Arguments.of("비정상 호출: 내용 길이 초과", null, new UpdateArticleRequest(TITLE, "a".repeat(Article.MAX_CONTENT_LENGTH + 1)), badRequest())
        );
    }

    @DisplayName("[DELETE] 게시글 삭제")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createDeleteRequestTests")
    void deleteArticleTest(String ignoredMessage, Context<ArticleService> context, List<ResultMatcher> resultMatchers) throws Exception {
        testTemplate.doDelete(context, articleUrl(ARTICLE_ID), resultMatchers);
    }
    static Stream<Arguments> createDeleteRequestTests() {
        ArticleDto validRequestDto = ArticleDto.builder().articleId(ARTICLE_ID).build();

        Map<String, Context<ArticleService>> contexts = Map.of(
                VALID, new Context<>(softDeleteArticle, validRequestDto),
                NOT_FOUND, new Context<>(softDeleteArticle, validRequestDto, new ArticleNotFoundException()),
                FORBIDDEN, new Context<>(softDeleteArticle, validRequestDto, new ForbiddenException())
        );
        return Stream.of(
                Arguments.of("정상 호출", contexts.get(VALID), ok()),
                Arguments.of("비정상 호출: 존재하지 않는 게시글", contexts.get(NOT_FOUND), notFound()),
                Arguments.of("비정상 호출: 타 회원 시도", contexts.get(FORBIDDEN), forbidden())
        );
    }
}