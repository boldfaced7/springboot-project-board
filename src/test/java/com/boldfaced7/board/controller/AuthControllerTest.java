package com.boldfaced7.board.controller;

import com.boldfaced7.board.Context;
import com.boldfaced7.board.domain.Member;
import com.boldfaced7.board.dto.AuthDto;
import com.boldfaced7.board.dto.request.AuthRequest;
import com.boldfaced7.board.auth.SessionConst;
import com.boldfaced7.board.service.AuthService;
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
import static org.assertj.core.api.Assertions.*;

@DisplayName("AuthController 테스트")
@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired MockMvc mvc;
    @Autowired Gson gson;
    @MockBean  AuthService authService;
    ControllerTestTemplate<AuthService> testTemplate;
    MockHttpSession session;

    @BeforeEach
    void setSessionAndTestTemplate() {
        session = new MockHttpSession();
        session.setAttribute(SessionConst.AUTH_RESPONSE, createAuthResponse());
        testTemplate = new ControllerTestTemplate<>(mvc, gson, session, authService);
    }

    @DisplayName("[API][POST] 로그인")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createLoginRequestTests")
    void loginTest(String ignoredMessage, Context<AuthService> context, AuthRequest request, List<ResultMatcher> resultMatchers) throws Exception {
        session.invalidate();
        testTemplate.doPost(context, loginUrl(), request, resultMatchers);
    }
    static Stream<Arguments> createLoginRequestTests() {
        AuthRequest validRequest = new AuthRequest(EMAIL, PASSWORD);
        AuthDto validRequestDto = validRequest.toDto();

        Map<String, Context<AuthService>> contexts = Map.of(
                VALID, new Context<>(login, validRequestDto, createResponseAuthDto())
        );
        return Stream.of(
                Arguments.of("정상 호출", contexts.get(VALID), validRequest, ok()),
                Arguments.of("비정상 호출: 이메일 형식 불일치", null, new AuthRequest("a", PASSWORD), badRequest()),
                Arguments.of("비정상 호출: 이메일 길이 초과", null, new AuthRequest("a".repeat(Member.MAX_EMAIL_LENGTH + 1), PASSWORD), badRequest()),
                Arguments.of("비정상 호출: 비밀번호 길이 초과", null, new AuthRequest(EMAIL, "a".repeat(Member.MAX_PASSWORD_LENGTH + 1)), badRequest())
        );
    }

    @DisplayName("[API][GET] 로그아웃")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createLogoutRequestTests")
    void logoutTest(String ignoredMessage, List<ResultMatcher> resultMatchers) throws Exception {
        assertThat(session.isInvalid()).isFalse();
        testTemplate.doGet(null, logoutUrl(), resultMatchers);
        assertThat(session.isInvalid()).isTrue();
    }
    static Stream<Arguments> createLogoutRequestTests() {
        return Stream.of(
                Arguments.of("정상 호출", ok())
        );
    }
}