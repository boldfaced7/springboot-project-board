package com.boldfaced7.board.controller.integration;

import com.boldfaced7.board.auth.SessionConst;
import com.boldfaced7.board.controller.ControllerTestTemplate;
import com.boldfaced7.board.service.ArticleTicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.time.LocalDate;
import java.util.stream.Stream;

import static com.boldfaced7.board.TestUtil.*;

@ActiveProfiles("test")
@DisplayName("ArticleTicketController 통합 테스트")
@SpringBootTest
@AutoConfigureMockMvc
class ArticleTicketControllerIntegrationTest {

    @Autowired MockMvc mvc;
    @Autowired ArticleTicketService articleTicketService;
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
    void getArticleTicketTest(String ignoredMessage, Long articleTicketId, ResultMatcher status) throws Exception {
        testTemplate.doGet(articleTicketUrl(articleTicketId), status);
    }
    static Stream<Arguments> getArticleTicketTest() {
        return Stream.of(
                Arguments.of("정상 호출", 1L, OK),
                Arguments.of("비정상 호출: 존재하지 않는 티켓", 100000L, NOT_FOUND)
        );
    }

    @DisplayName("[GET] 티켓 리스트 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    void getArticleTicketsTest(String ignoredMessage, ResultMatcher status) throws Exception {
        testTemplate.doGet(articleTicketUrl()+"?date="+LocalDate.now(), status);
    }
    static Stream<Arguments> getArticleTicketsTest() {
        return Stream.of(Arguments.of("정상 호출", OK));

    }

    @DisplayName("[GET] 회원 티켓 리스트 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    void getArticleTicketsOfMemberTest(String ignoredMessage, Long memberId, ResultMatcher status) throws Exception {
        testTemplate.doGet(memberArticleTicketUrl(memberId), status);
    }
    static Stream<Arguments> getArticleTicketsOfMemberTest() {
        return Stream.of(
                Arguments.of("정상 호출", 1L, OK),
                Arguments.of("비정상 호출: 타 회원 시도", 100000L, FORBIDDEN)
        );

    }
    @DisplayName("[POST] 티켓 발급")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    void issueTicketTest(String ignoredMessage, ResultMatcher status) throws Exception {
        testTemplate.doPost(articleTicketUrl(), null, status);
    }
    static Stream<Arguments> issueTicketTest() {
        return Stream.of(Arguments.of("정상 호출", CREATED));
    }
}