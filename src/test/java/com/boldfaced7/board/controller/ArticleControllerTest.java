package com.boldfaced7.board.controller;

import com.boldfaced7.board.Mocker;
import com.boldfaced7.board.auth.SessionConst;
import com.boldfaced7.board.dto.ArticleDto;
import com.boldfaced7.board.dto.CustomPage;
import com.boldfaced7.board.dto.MemberDto;
import com.boldfaced7.board.dto.request.SaveArticleRequest;
import com.boldfaced7.board.dto.request.UpdateArticleRequest;
import com.boldfaced7.board.dto.response.ArticleListResponse;
import com.boldfaced7.board.dto.response.ArticleResponse;
import com.boldfaced7.board.error.exception.article.ArticleNotFoundException;
import com.boldfaced7.board.error.exception.auth.ForbiddenException;
import com.boldfaced7.board.error.exception.member.MemberNotFoundException;
import com.boldfaced7.board.service.ArticleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.stream.Stream;

import static com.boldfaced7.board.TestUtil.*;
import static org.mockito.ArgumentMatchers.any;

@DisplayName("ArticleController 테스트")
@WebMvcTest({ArticleController.class})
class ArticleControllerTest {

    @Autowired MockMvc mvc;
    @MockBean ArticleService articleService;
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
    @MethodSource
    <T> void getArticleTest(Mocker<ArticleService> mock, ResultMatcher status, T response) throws Exception {
        testTemplate.doGet(mock, articleUrl(1L), status, response);
    }
    static Stream<Arguments> getArticleTest() {
        Mocker<ArticleService> valid = new Mocker<>("정상 호출");
        ArticleDto dto = articleDto();
        valid.mocks(a -> a.getArticle(any()), dto);

        Mocker<ArticleService> notFound = new Mocker<>("비정상 호출: 존재하지 않는 게시글");
        notFound.mocksFunction(a -> a.getArticle(any()), ArticleNotFoundException.class);

        return Stream.of(
                Arguments.of(valid, OK, new ArticleResponse(dto)),
                Arguments.of(notFound, NOT_FOUND, null)
        );
    }

    @DisplayName("[GET] 게시글 리스트 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    <T> void getArticlesTest(Mocker<ArticleService> mock, ResultMatcher status, T response) throws Exception {
        testTemplate.doGet(mock, articleUrl(), status, response);
    }
    static Stream<Arguments> getArticlesTest() {
        Mocker<ArticleService> valid = new Mocker<>("정상 호출");
        CustomPage<ArticleDto> page = articleDtos();
        valid.mocks(a -> a.getArticles(any(Pageable.class)), page);

        return Stream.of(
                Arguments.of(valid, OK, new ArticleListResponse(page))
        );
    }

    @DisplayName("[GET] 회원 작성 게시글 리스트 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    <T> void getArticlesOfMemberTest(Mocker<ArticleService> mock, ResultMatcher status, T response) throws Exception {
        testTemplate.doGet(mock, memberArticleUrl(1L), status, response);
    }
    static Stream<Arguments> getArticlesOfMemberTest() {
        Mocker<ArticleService> valid = new Mocker<>("정상 호출");
        CustomPage<ArticleDto> page = articleDtos();
        valid.mocks(a -> a.getArticles(any(MemberDto.class)), page);

        Mocker<ArticleService> notFound = new Mocker<>("비정상 호출: 존재하지 않는 회원");
//        notFound.mocksFunction(a -> a.getArticles(any(MemberDto.class)), ArticleNotFoundException.class);
        notFound.mocksFunction(a -> a.getArticles(any(MemberDto.class)), MemberNotFoundException.class);

        return Stream.of(
                Arguments.of(valid, OK, new ArticleListResponse(page)),
                Arguments.of(notFound, NOT_FOUND, null)
        );
    }

    @DisplayName("[POST] 게시글 등록")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    void postArticleTest(Mocker<ArticleService> mock, String title, String content, ResultMatcher status) throws Exception {
        testTemplate.doPost(mock, articleUrl(), new SaveArticleRequest(title, content), status);
    }
    static Stream<Arguments> postArticleTest() {
        Mocker<ArticleService> valid = new Mocker<>("정상 호출");
        valid.mocks(a -> a.saveArticle(any()), 1L);

        return Stream.of(
                Arguments.of(valid, TITLE, CONTENT, CREATED),
                Arguments.of(new Mocker<>("비정상 호출: 제목 누락"), "", CONTENT, BAD_REQUEST),
                Arguments.of(new Mocker<>("비정상 호출: 내용 누락"), TITLE, "", BAD_REQUEST),
                Arguments.of(new Mocker<>("비정상 호출: 제목 길이 초과"),EXCEEDED_ARTICLE_TITLE, CONTENT, BAD_REQUEST),
                Arguments.of(new Mocker<>("비정상 호출: 내용 길이 초과"), TITLE, EXCEEDED_ARTICLE_CONTENT, BAD_REQUEST)
        );
    }

    @DisplayName("[PATCH] 게시글 수정")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    void patchArticleTest(Mocker<ArticleService> mock, String title, String content, ResultMatcher status) throws Exception {
        testTemplate.doPatch(mock, articleUrl(1L), new UpdateArticleRequest(title, content), status);
    }
    static Stream<Arguments> patchArticleTest() {
        Mocker<ArticleService> valid = new Mocker<>("정상 호출");
        valid.mocks(a -> a.updateArticle(any()));

        Mocker<ArticleService> notFound = new Mocker<>("비정상 호출: 존재하지 않는 게시글");
        notFound.mocksConsumer(a -> a.updateArticle(any()), ArticleNotFoundException.class);

        Mocker<ArticleService> forbidden = new Mocker<>("비정상 호출: 타 회원 시도");
        forbidden.mocksConsumer(a -> a.updateArticle(any()), ForbiddenException.class);

        return Stream.of(
                Arguments.of(valid, TITLE, CONTENT, OK),
                Arguments.of(notFound, TITLE, CONTENT, NOT_FOUND),
                Arguments.of(forbidden, TITLE, CONTENT, FORBIDDEN),
                Arguments.of(new Mocker<>("비정상 호출: 제목 누락"), "", CONTENT, BAD_REQUEST),
                Arguments.of(new Mocker<>("비정상 호출: 내용 누락"), TITLE, "", BAD_REQUEST),
                Arguments.of(new Mocker<>("비정상 호출: 제목 길이 초과"), EXCEEDED_ARTICLE_TITLE, CONTENT, BAD_REQUEST),
                Arguments.of(new Mocker<>("비정상 호출: 내용 길이 초과"), TITLE, EXCEEDED_ARTICLE_CONTENT, BAD_REQUEST)
        );
    }

    @DisplayName("[DELETE] 게시글 삭제")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    void deleteArticleTest(Mocker<ArticleService> mock, ResultMatcher status) throws Exception {
        testTemplate.doDelete(mock, articleUrl(1L), status);
    }
    static Stream<Arguments> deleteArticleTest() {
        Mocker<ArticleService> valid = new Mocker<>("정상 호출");
        valid.mocks(a -> a.softDeleteArticle(any()));

        Mocker<ArticleService> notFound = new Mocker<>("비정상 호출: 존재하지 않는 게시글");
        notFound.mocksConsumer(a -> a.softDeleteArticle(any()), ArticleNotFoundException.class);

        Mocker<ArticleService> forbidden = new Mocker<>("비정상 호출: 타 회원 시도");
        forbidden.mocksConsumer(a -> a.softDeleteArticle(any()), ForbiddenException.class);

        return Stream.of(
                Arguments.of(valid, OK),
                Arguments.of(notFound, NOT_FOUND),
                Arguments.of(forbidden, FORBIDDEN)
        );
    }
}