package com.boldfaced7.board.controller;

import com.boldfaced7.board.Mocker;
import com.boldfaced7.board.auth.SessionConst;
import com.boldfaced7.board.dto.ArticleCommentDto;
import com.boldfaced7.board.dto.ArticleDto;
import com.boldfaced7.board.dto.CustomPage;
import com.boldfaced7.board.dto.MemberDto;
import com.boldfaced7.board.dto.request.SaveArticleCommentRequest;
import com.boldfaced7.board.dto.request.UpdateArticleCommentRequest;
import com.boldfaced7.board.dto.response.ArticleCommentListResponse;
import com.boldfaced7.board.dto.response.ArticleCommentResponse;
import com.boldfaced7.board.error.exception.article.ArticleNotFoundException;
import com.boldfaced7.board.error.exception.articlecomment.ArticleCommentNotFoundException;
import com.boldfaced7.board.error.exception.auth.ForbiddenException;
import com.boldfaced7.board.service.ArticleCommentService;
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

@DisplayName("ArticleCommentController 테스트")
@WebMvcTest({ArticleCommentController.class})
class ArticleCommentControllerTest {

    @Autowired MockMvc mvc;
    @MockBean ArticleCommentService articleCommentService;
    ControllerTestTemplate<ArticleCommentService> testTemplate;
    MockHttpSession session;

    @BeforeEach
    void setSessionAndTestTemplate() {
        session = new MockHttpSession();
        session.setAttribute(SessionConst.AUTH_RESPONSE, authResponse());
        testTemplate = new ControllerTestTemplate<>(mvc, session, articleCommentService);
    }

    @DisplayName("[GET] 댓글 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    <T> void getArticleCommentTest(Mocker<ArticleCommentService> mock, ResultMatcher status, T response) throws Exception {
        testTemplate.doGet(mock, articleArticleCommentUrl(1L, 1L), status, response);
    }
    static Stream<Arguments> getArticleCommentTest() {
        Mocker<ArticleCommentService> valid = new Mocker<>("정상 호출");
        ArticleCommentDto dto = articleCommentDto();
        valid.mocks(a -> a.getArticleComment(any()), dto);

        Mocker<ArticleCommentService> notFound = new Mocker<>("비정상 호출: 존재하지 않는 댓글");
        notFound.mocksFunction(a -> a.getArticleComment(any()), ArticleCommentNotFoundException.class);

        return Stream.of(
                Arguments.of(valid, OK, new ArticleCommentResponse(dto)),
                Arguments.of(notFound, NOT_FOUND, null)
        );
    }

    @DisplayName("[GET] 댓글 리스트 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    <T> void getArticleCommentsTest(Mocker<ArticleCommentService> mock, ResultMatcher status, T response) throws Exception {
        testTemplate.doGet(mock, articleCommentUrl(), status, response);
    }
    static Stream<Arguments> getArticleCommentsTest() {
        Mocker<ArticleCommentService> valid = new Mocker<>("정상 호출");
        CustomPage<ArticleCommentDto> page = articleCommentDtos();
        valid.mocks(a -> a.getArticleComments(any(Pageable.class)), page);

        return Stream.of(Arguments.of(valid, OK, new ArticleCommentListResponse(page)));
    }

    @DisplayName("[GET] 게시글의 댓글 리스트 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    <T> void getArticleCommentsOfArticleTest(Mocker<ArticleCommentService> mock, ResultMatcher status, T response) throws Exception {
        testTemplate.doGet(mock, articleArticleCommentUrl(1L), status, response);
    }
    static Stream<Arguments> getArticleCommentsOfArticleTest() {
        Mocker<ArticleCommentService> valid = new Mocker<>("정상 호출");
        CustomPage<ArticleCommentDto> page = articleCommentDtos();
        valid.mocks(a -> a.getArticleComments(any(ArticleDto.class)), page);

        Mocker<ArticleCommentService> notFound = new Mocker<>("비정상 호출: 존재하지 않는 게시글");
        notFound.mocksFunction(a -> a.getArticleComments(any(ArticleDto.class)), ArticleNotFoundException.class);

        return Stream.of(
                Arguments.of(valid, OK, new ArticleCommentListResponse(page)),
                Arguments.of(notFound, NOT_FOUND, null)
        );
    }

    @DisplayName("[GET] 회원 작성 댓글 리스트 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    <T> void getArticleCommentsOfMemberTest(Mocker<ArticleCommentService> mock, ResultMatcher status, T response) throws Exception {
        testTemplate.doGet(mock, memberArticleCommentUrl(1L), status, response);
    }
    static Stream<Arguments> getArticleCommentsOfMemberTest() {
        Mocker<ArticleCommentService> valid = new Mocker<>("정상 호출");
        CustomPage<ArticleCommentDto> page = articleCommentDtos();
        valid.mocks(a -> a.getArticleComments(any(MemberDto.class)), page);

        Mocker<ArticleCommentService> forbidden = new Mocker<>("비정상 호출: 타 회원 시도");
        forbidden.mocksFunction(a -> a.getArticleComments(any(MemberDto.class)), ForbiddenException.class);

        return Stream.of(
                Arguments.of(valid, OK, new ArticleCommentListResponse(page)),
                Arguments.of(forbidden, FORBIDDEN, null)
        );
    }

    @DisplayName("[POST] 댓글 등록")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    void postArticleCommentTest(Mocker<ArticleCommentService> mock, String content, ResultMatcher status) throws Exception {
        testTemplate.doPost(mock, articleArticleCommentUrl(1L), new SaveArticleCommentRequest(content), status);
    }
    static Stream<Arguments> postArticleCommentTest() {
        Mocker<ArticleCommentService> valid = new Mocker<>("정상 호출");
        valid.mocks(a -> a.saveArticleComment(any()), 1L);

        return Stream.of(
                Arguments.of(valid, CONTENT, CREATED),
                Arguments.of(new Mocker<>("비정상 호출: 내용 누락"), "", BAD_REQUEST),
                Arguments.of(new Mocker<>("비정상 호출: 내용 길이 초과"), EXCEEDED_COMMENT_CONTENT, BAD_REQUEST)
        );
    }

    @DisplayName("[PATCH] 댓글 수정")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    void patchArticleCommentTest(Mocker<ArticleCommentService> mock, String content, ResultMatcher status) throws Exception {
        testTemplate.doPatch(mock, articleArticleCommentUrl(1L, 1L), new UpdateArticleCommentRequest(content), status);
    }
    static Stream<Arguments> patchArticleCommentTest() {
        Mocker<ArticleCommentService> valid = new Mocker<>("정상 호출");
        valid.mocks(a -> a.updateArticleComment(any()));

        Mocker<ArticleCommentService> notFound = new Mocker<>("비정상 호출: 존재하지 않는 댓글");
        notFound.mocksConsumer(a -> a.updateArticleComment(any()), ArticleCommentNotFoundException.class);

        Mocker<ArticleCommentService> forbidden = new Mocker<>("비정상 호출: 타 회원 시도");
        forbidden.mocksConsumer(a -> a.updateArticleComment(any()), ForbiddenException.class);

        return Stream.of(
                Arguments.of(valid, CONTENT, OK),
                Arguments.of(notFound, CONTENT, NOT_FOUND),
                Arguments.of(forbidden, CONTENT, FORBIDDEN),
                Arguments.of(new Mocker<>("비정상 호출: 내용 누락"), "", BAD_REQUEST),
                Arguments.of(new Mocker<>("비정상 호출: 내용 길이 초과"), EXCEEDED_COMMENT_CONTENT, BAD_REQUEST)
        );
    }

    @DisplayName("[DELETE] 댓글 삭제")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    void deleteArticleCommentTest(Mocker<ArticleCommentService> mock, ResultMatcher status) throws Exception {
        testTemplate.doDelete(mock, articleArticleCommentUrl(1L, 1L), status);
    }
    static Stream<Arguments> deleteArticleCommentTest() {
        Mocker<ArticleCommentService> valid = new Mocker<>("정상 호출");
        valid.mocks(a -> a.softDeleteArticleComment(any()));

        Mocker<ArticleCommentService> notFound = new Mocker<>("비정상 호출: 존재하지 않는 댓글");
        notFound.mocksConsumer(a -> a.softDeleteArticleComment(any()), ArticleCommentNotFoundException.class);

        Mocker<ArticleCommentService> forbidden = new Mocker<>("비정상 호출: 타 회원 시도");
        forbidden.mocksConsumer(a -> a.softDeleteArticleComment(any()), ForbiddenException.class);

        return Stream.of(
                Arguments.of(valid, OK),
                Arguments.of(notFound, NOT_FOUND),
                Arguments.of(forbidden, FORBIDDEN)
        );
    }
}