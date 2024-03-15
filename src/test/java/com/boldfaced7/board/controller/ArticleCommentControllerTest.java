package com.boldfaced7.board.controller;

import com.boldfaced7.board.Context;
import com.boldfaced7.board.auth.SessionConst;
import com.boldfaced7.board.domain.ArticleComment;
import com.boldfaced7.board.dto.ArticleCommentDto;
import com.boldfaced7.board.dto.ArticleDto;
import com.boldfaced7.board.dto.MemberDto;
import com.boldfaced7.board.dto.request.SaveArticleCommentRequest;
import com.boldfaced7.board.dto.request.UpdateArticleCommentRequest;
import com.boldfaced7.board.error.exception.article.ArticleNotFoundException;
import com.boldfaced7.board.error.exception.articlecomment.ArticleCommentNotFoundException;
import com.boldfaced7.board.error.exception.auth.ForbiddenException;
import com.boldfaced7.board.error.exception.member.MemberNotFoundException;
import com.boldfaced7.board.service.ArticleCommentService;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.boldfaced7.board.TestUtil.*;
import static com.boldfaced7.board.ServiceMethod.*;

@DisplayName("ArticleCommentController 테스트")
@WebMvcTest({ArticleCommentController.class})
class ArticleCommentControllerTest {

    @Autowired MockMvc mvc;
    @Autowired Gson gson;
    @MockBean ArticleCommentService articleCommentService;
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
    void GetArticleCommentTest(String ignoredMessage, Context<ArticleCommentService> context, List<ResultMatcher> resultMatchers) throws Exception {
        testTemplate.doGet(context, articleArticleCommentUrl(ARTICLE_ID, ARTICLE_COMMENT_ID), resultMatchers);
    }
    static Stream<Arguments> createGetArticleCommentRequestTests() {
        Map<String, Context<ArticleCommentService>> contexts = Map.of(
                VALID, new Context<>(getArticleComment, ARTICLE_COMMENT_ID, createArticleCommentDto()),
                NOT_FOUND, new Context<>(getArticleComment, ARTICLE_COMMENT_ID, new ArticleCommentNotFoundException())
        );
        List<ResultMatcher> exists = exists(List.of("articleId", "articleCommentId", "content", "author"), "");
        List<ResultMatcher> resultMatchers = Stream.of(exists, ok(), contentTypeJson()).flatMap(List::stream).toList();

        return Stream.of(
                Arguments.of("정상 호출", contexts.get(VALID), resultMatchers),
                Arguments.of("비정상 호출: 존재하지 않는 댓글", contexts.get(NOT_FOUND), notFound())
        );
    }

    @DisplayName("[GET] 댓글 리스트 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createGetArticleCommentsRequestTest")
    void GetArticleCommentsTest(String ignoredMessage, Context<ArticleCommentService> context, List<ResultMatcher> resultMatchers) throws Exception {
        testTemplate.doGet(context, articleCommentUrl(), resultMatchers);
    }
    static Stream<Arguments> createGetArticleCommentsRequestTest() {
        Map<String, Context<ArticleCommentService>> contexts = Map.of(
                VALID, new Context<>(getArticleComments, List.of(createArticleCommentDto()))
        );
        List<ResultMatcher> exists = exists(List.of("articleCommentId", "content", "author"), ".articleComments[0]");
        List<ResultMatcher> resultMatchers = Stream.of(exists, ok(), contentTypeJson()).flatMap(List::stream).toList();

        return Stream.of(
                Arguments.of("정상 호출", contexts.get(VALID), resultMatchers)
        );
    }

    @DisplayName("[GET] 게시글의 댓글 리스트 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createGetArticleCommentsOfArticleRequestTest")
    void GetArticleCommentsOfArticleTest(String ignoredMessage, Context<ArticleCommentService> context, List<ResultMatcher> resultMatchers) throws Exception {
        testTemplate.doGet(context, articleArticleCommentUrl(ARTICLE_ID), resultMatchers);
    }
    static Stream<Arguments> createGetArticleCommentsOfArticleRequestTest() {
        ArticleDto validArticleDto = ArticleDto.builder().articleId(ARTICLE_ID).build();

        Map<String, Context<ArticleCommentService>> contexts = Map.of(
                VALID, new Context<>(getArticleCommentsOfArticle, validArticleDto, List.of(createArticleCommentDto())),
                NOT_FOUND, new Context<>(getArticleCommentsOfArticle, validArticleDto, new ArticleNotFoundException())
        );
        List<ResultMatcher> exists = exists(List.of("articleCommentId", "content", "author"), ".articleComments[0]");
        List<ResultMatcher> resultMatchers = Stream.of(exists, ok(), contentTypeJson()).flatMap(List::stream).toList();

        return Stream.of(
                Arguments.of("정상 호출", contexts.get(VALID), resultMatchers),
                Arguments.of("비정상 호출: 존재하지 않는 게시글", contexts.get(NOT_FOUND), notFound())
        );
    }

    @DisplayName("[GET] 회원 작성 댓글 리스트 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createGetArticleCommentsOfMemberRequestTest")
    void GetArticleCommentsOfMemberTest(String ignoredMessage, Context<ArticleCommentService> context, List<ResultMatcher> resultMatchers) throws Exception {
        testTemplate.doGet(context, memberArticleCommentUrl(MEMBER_ID), resultMatchers);
    }
    static Stream<Arguments> createGetArticleCommentsOfMemberRequestTest() {
        MemberDto validMemberDto = MemberDto.builder().memberId(MEMBER_ID).build();

        Map<String, Context<ArticleCommentService>> contexts = Map.of(
                VALID, new Context<>(getArticleCommentsOfMember, validMemberDto, List.of(createArticleCommentDto())),
                NOT_FOUND, new Context<>(getArticleCommentsOfMember, validMemberDto, new MemberNotFoundException()),
                FORBIDDEN, new Context<>(getArticleCommentsOfMember, validMemberDto, new ForbiddenException())
        );
        List<ResultMatcher> exists = exists(List.of("articleCommentId", "content", "author"), ".articleComments[0]");
        List<ResultMatcher> resultMatchers = Stream.of(exists, ok(), contentTypeJson()).flatMap(List::stream).toList();

        return Stream.of(
                Arguments.of("정상 호출", contexts.get(VALID), resultMatchers),
                Arguments.of("비정상 호출: 존재하지 않는 회원", contexts.get(NOT_FOUND), notFound()),
                Arguments.of("비정상 호출: 타 회원 시도", contexts.get(FORBIDDEN), forbidden())
        );
    }

    @DisplayName("[POST] 댓글 등록")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createPostRequestTests")
    <T> void PostArticleCommentTest(String ignoredMessage, Context<ArticleCommentService> context, T request, List<ResultMatcher> resultMatchers) throws Exception {
        testTemplate.doPost(context, articleArticleCommentUrl(ARTICLE_ID), request, resultMatchers);
    }
    static Stream<Arguments> createPostRequestTests() {
        SaveArticleCommentRequest validRequest = new SaveArticleCommentRequest(CONTENT);
        ArticleCommentDto validRequestDto = validRequest.toDto(ARTICLE_ID);

        Map<String, Context<ArticleCommentService>> contexts = Map.of(
                VALID, new Context<>(saveArticleComment, validRequestDto, ARTICLE_COMMENT_ID)
        );
        return Stream.of(
                Arguments.of("정상 호출", contexts.get(VALID), validRequest, created()),
                Arguments.of("비정상 호출: 내용 누락", null, new SaveArticleCommentRequest(""), badRequest()),
                Arguments.of("비정상 호출: 내용 길이 초과", null, new SaveArticleCommentRequest("a".repeat(ArticleComment.MAX_CONTENT_LENGTH + 1)), badRequest())
        );
    }

    @DisplayName("[PATCH] 댓글 수정")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createPatchRequestTests")
    <T> void PatchArticleCommentTest(String ignoredMessage, Context<ArticleCommentService> context, T request, List<ResultMatcher> resultMatchers) throws Exception {
        testTemplate.doPatch(context, articleArticleCommentUrl(ARTICLE_ID, ARTICLE_COMMENT_ID), request, resultMatchers);
    }
    static Stream<Arguments> createPatchRequestTests() {
        UpdateArticleCommentRequest validRequest = new UpdateArticleCommentRequest(CONTENT);
        ArticleCommentDto validRequestDto = validRequest.toDto(ARTICLE_COMMENT_ID);

        Map<String, Context<ArticleCommentService>> contexts = Map.of(
                VALID, new Context<>(updateArticleComment, validRequestDto),
                NOT_FOUND, new Context<>(updateArticleComment, validRequestDto, new ArticleCommentNotFoundException()),
                FORBIDDEN, new Context<>(updateArticleComment, validRequestDto, new ForbiddenException())
        );
        return Stream.of(
                Arguments.of("정상 호출", contexts.get(VALID), validRequest, ok()),
                Arguments.of("비정상 호출: 존재하지 않는 댓글", contexts.get(NOT_FOUND), validRequest, notFound()),
                Arguments.of("비정상 호출: 타 회원 시도", contexts.get(FORBIDDEN), validRequest, forbidden()),
                Arguments.of("비정상 호출: 내용 누락", null, new UpdateArticleCommentRequest(""), badRequest()),
                Arguments.of("비정상 호출: 내용 길이 초과", null, new UpdateArticleCommentRequest("a".repeat(ArticleComment.MAX_CONTENT_LENGTH + 1)), badRequest())
        );
    }

    @DisplayName("[DELETE] 댓글 삭제")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createDeleteRequestTests")
    void DeleteArticleCommentTest(String ignoredMessage, Context<ArticleCommentService> context, List<ResultMatcher> resultMatchers) throws Exception {
        testTemplate.doDelete(context, articleArticleCommentUrl(ARTICLE_ID,ARTICLE_COMMENT_ID), resultMatchers);
    }
    static Stream<Arguments> createDeleteRequestTests() {
        ArticleCommentDto validArticleCommentDto = ArticleCommentDto.builder().articleCommentId(ARTICLE_COMMENT_ID).build();

        Map<String, Context<ArticleCommentService>> contexts = Map.of(
                VALID, new Context<>(softDeleteArticleComment, validArticleCommentDto),
                NOT_FOUND, new Context<>(softDeleteArticleComment, validArticleCommentDto, new ArticleCommentNotFoundException()),
                FORBIDDEN, new Context<>(softDeleteArticleComment, validArticleCommentDto, new ForbiddenException())
        );
        return Stream.of(
                Arguments.of("정상 호출", contexts.get(VALID), ok()),
                Arguments.of("비정상 호출: 존재하지 않는 댓글", contexts.get(NOT_FOUND), notFound()),
                Arguments.of("비정상 호출: 타 회원 시도", contexts.get(FORBIDDEN), forbidden())
        );
    }
}