package com.boldfaced7.board.controller;

import com.boldfaced7.board.Mocker;
import com.boldfaced7.board.auth.SessionConst;
import com.boldfaced7.board.dto.ArticleTicketDto;
import com.boldfaced7.board.dto.CustomPage;
import com.boldfaced7.board.dto.MemberDto;
import com.boldfaced7.board.dto.response.ArticleTicketListResponse;
import com.boldfaced7.board.dto.response.ArticleTicketResponse;
import com.boldfaced7.board.error.exception.articleticket.ArticleTicketNotFoundException;
import com.boldfaced7.board.error.exception.auth.ForbiddenException;
import com.boldfaced7.board.service.ArticleTicketService;
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

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static com.boldfaced7.board.TestUtil.*;
import static org.mockito.ArgumentMatchers.any;

@DisplayName("ArticleTicketController 테스트")
@WebMvcTest({ArticleTicketController.class})
class ArticleTicketControllerTest {

    @Autowired MockMvc mvc;
    @MockBean ArticleTicketService articleTicketService;
    ControllerTestTemplate<ArticleTicketService> testTemplate;
    MockHttpSession session;

    @BeforeEach
    void setSessionAndTestTemplate() {
        session = new MockHttpSession();
        session.setAttribute(SessionConst.AUTH_RESPONSE, authResponse());
        testTemplate = new ControllerTestTemplate<>(mvc, session, articleTicketService);
    }

    @DisplayName("[GET] 티켓 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    <T> void getArticleTicketTest(Mocker<ArticleTicketService> mock, ResultMatcher status, T response) throws Exception {
        testTemplate.doGet(mock, articleTicketUrl(1L), status, response);
    }
    static Stream<Arguments> getArticleTicketTest() {
        Mocker<ArticleTicketService> valid = new Mocker<>("정상 호출");
        ArticleTicketDto dto = new ArticleTicketDto(1L);
        valid.mocks(a -> a.getIssuedTicket(any()), dto);

        Mocker<ArticleTicketService> notFound = new Mocker<>("비정상 호출: 존재하지 않는 티켓");
        notFound.mocksFunction(a -> a.getIssuedTicket(any()), ArticleTicketNotFoundException.class);

        return Stream.of(
                Arguments.of(valid, OK, new ArticleTicketResponse(dto)),
                Arguments.of(notFound, NOT_FOUND, null)
        );
    }

    @DisplayName("[GET] 티켓 리스트 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    <T> void getArticleTicketsTest(Mocker<ArticleTicketService> mock, ResultMatcher status, T response) throws Exception {
        testTemplate.doGet(mock, articleTicketUrl()+"?date="+LocalDate.now(), status, response);
    }
    static Stream<Arguments> getArticleTicketsTest() {
        Mocker<ArticleTicketService> valid = new Mocker<>("정상 호출");
        CustomPage<ArticleTicketDto> page = new CustomPage<>(List.of(new ArticleTicketDto(1L)), 0, 0, 1);
        valid.mocks(a -> a.getIssuedTickets(any(Pageable.class), any()), page);

        return Stream.of(Arguments.of(valid, OK, new ArticleTicketListResponse(page)));

    }

    @DisplayName("[GET] 회원 티켓 리스트 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    <T> void getArticleTicketsOfMemberTest(Mocker<ArticleTicketService> mock, ResultMatcher status, T response) throws Exception {
        testTemplate.doGet(mock, memberArticleTicketUrl(1L), status, response);
    }
    static Stream<Arguments> getArticleTicketsOfMemberTest() {
        Mocker<ArticleTicketService> valid = new Mocker<>("정상 호출");
        CustomPage<ArticleTicketDto> page = new CustomPage<>(List.of(new ArticleTicketDto(1L)), 0, 0, 1);
        valid.mocks(a -> a.getIssuedTickets(any(MemberDto.class)), page);

        Mocker<ArticleTicketService> forbidden = new Mocker<>("비정상 호출: 타 회원 시도");
        forbidden.mocksFunction(a -> a.getIssuedTickets(any(MemberDto.class)), ForbiddenException.class);

        return Stream.of(
                Arguments.of(valid, OK, new ArticleTicketListResponse(page)),
                Arguments.of(forbidden, FORBIDDEN, null)
        );

    }

    @DisplayName("[POST] 티켓 발급")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    void issueTicketTest(Mocker<ArticleTicketService> mock, ResultMatcher status) throws Exception {
        testTemplate.doPost(mock, articleTicketUrl(), null, status);
    }
    static Stream<Arguments> issueTicketTest() {
        Mocker<ArticleTicketService> valid = new Mocker<>("정상 호출");
        valid.mocks(ArticleTicketService::issueTicket, 1L);
        return Stream.of(Arguments.of(valid, CREATED));
    }
}