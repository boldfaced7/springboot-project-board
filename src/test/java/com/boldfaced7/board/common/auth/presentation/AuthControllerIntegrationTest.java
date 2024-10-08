package com.boldfaced7.board.common.auth.presentation;

import com.boldfaced7.board.common.auth.AuthInfoHolder;
import com.boldfaced7.noboilerplate.ControllerTestTemplate;
import com.boldfaced7.board.common.auth.presentation.request.AuthRequest;
import com.boldfaced7.board.common.auth.application.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

import static com.boldfaced7.noboilerplate.TestUtil.*;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DisplayName("AuthController 통합 테스트")
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired AuthService authService;
    ControllerTestTemplate<AuthService> testTemplate;
    MockHttpSession session;

    @BeforeEach
    void setSessionAndTestTemplate() {
        session = new MockHttpSession();
        testTemplate = new ControllerTestTemplate<>(mvc, session, authService);
    }

    @DisplayName("[API][POST] 로그인")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createLoginRequestTests")
    void loginTest(String ignoredMessage, String email, String password, ResultMatcher status) throws Exception {
        session.invalidate();
        testTemplate.doPost(loginUrl(), new AuthRequest(email, password), status);
    }
    static Stream<Arguments> createLoginRequestTests() {
        return Stream.of(
                Arguments.of("정상 호출", EMAIL, PASSWORD, FOUND),
                Arguments.of("비정상 호출: 이메일 불일치", "wrong" + EMAIL, PASSWORD, UNAUTHORIZED),
                Arguments.of("비정상 호출: 비밀번호 불일치", EMAIL, "wrong" + PASSWORD, UNAUTHORIZED),
                Arguments.of("비정상 호출: 이메일 누락", "", PASSWORD, BAD_REQUEST),
                Arguments.of("비정상 호출: 비밀번호 누락", EMAIL, "", BAD_REQUEST),
                Arguments.of("비정상 호출: 이메일 길이 초과",  EXCEEDED_EMAIL, PASSWORD, BAD_REQUEST),
                Arguments.of("비정상 호출: 비밀번호 길이 초과",  EMAIL, EXCEEDED_PASSWORD, BAD_REQUEST),
                Arguments.of("비정상 호출: 이메일 형식 불일치",  "a", PASSWORD, BAD_REQUEST)
        );
    }

    @DisplayName("[API][GET] 로그아웃")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createLogoutRequestTests")
    void logoutTest(String ignoredMessage, ResultMatcher status) throws Exception {
        AuthInfoHolder.setAuthInfo(authResponse());
        assertThat(AuthInfoHolder.isEmpty()).isFalse();
        testTemplate.doPost(logoutUrl(), null, status);
        assertThat(AuthInfoHolder.isEmpty()).isTrue();
    }
    static Stream<Arguments> createLogoutRequestTests() {
        return Stream.of(
                Arguments.of("정상 호출", OK)
        );
    }
}