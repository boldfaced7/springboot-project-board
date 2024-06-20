package com.boldfaced7.board.controller;

import com.boldfaced7.board.Mocker;
import com.boldfaced7.board.auth.AuthInfoHolder;
import com.boldfaced7.board.auth.SessionConst;
import com.boldfaced7.board.dto.request.AuthRequest;
import com.boldfaced7.board.error.exception.auth.InvalidAuthValueException;
import com.boldfaced7.board.service.AuthService;
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

import java.util.stream.Stream;

import static com.boldfaced7.board.TestUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@DisplayName("AuthController 테스트")
@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired MockMvc mvc;
    @MockBean  AuthService authService;
    ControllerTestTemplate<AuthService> testTemplate;
    MockHttpSession session;

    @BeforeEach
    void setSessionAndTestTemplate() {
        session = new MockHttpSession();
        AuthInfoHolder.setAuthInfo(authResponse());
        testTemplate = new ControllerTestTemplate<>(mvc, session, authService);
    }

    @DisplayName("[POST] 로그인")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    void loginTest(Mocker<AuthService> mock, String email, String password, ResultMatcher status) throws Exception {
        testTemplate.doPost(mock, loginUrl(), new AuthRequest(email, password), status);
    }
    static Stream<Arguments> loginTest() {
        Mocker<AuthService> valid = new Mocker<>("정상 호출");
        valid.mocks(a -> a.login(any()), responseAuthDto());

        Mocker<AuthService> notFound = new Mocker<>("비정상 호출: 존재하지 않는 ID/PW");
        notFound.mocksFunction(a -> a.login(any()), InvalidAuthValueException.class);

        return Stream.of(
                Arguments.of(valid, EMAIL, PASSWORD, OK),
                Arguments.of(notFound, EMAIL, PASSWORD, UNAUTHORIZED),
                Arguments.of(new Mocker<>("비정상 호출: 이메일 형식 불일치"), "a", PASSWORD, BAD_REQUEST),
                Arguments.of(new Mocker<>("비정상 호출: 이메일 누락"), "", PASSWORD, BAD_REQUEST),
                Arguments.of(new Mocker<>("비정상 호출: 비밀번호 누락"), EMAIL, "", BAD_REQUEST),
                Arguments.of(new Mocker<>("비정상 호출: 이메일 길이 초과"), EXCEEDED_EMAIL, PASSWORD, BAD_REQUEST),
                Arguments.of(new Mocker<>("비정상 호출: 비밀번호 길이 초과"), EMAIL, EXCEEDED_PASSWORD, BAD_REQUEST)
        );
    }

    @DisplayName("[API][GET] 로그아웃")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createLogoutRequestTests")
    void logoutTest(Mocker<AuthService> mock, ResultMatcher status) throws Exception {
        assertThat(session.isInvalid()).isFalse();
        testTemplate.doGet(mock, logoutUrl(), status);
        assertThat(session.isInvalid()).isTrue();
    }
    static Stream<Arguments> createLogoutRequestTests() {
        return Stream.of(
                Arguments.of(new Mocker<>("정상 호출"), OK)
        );
    }
}