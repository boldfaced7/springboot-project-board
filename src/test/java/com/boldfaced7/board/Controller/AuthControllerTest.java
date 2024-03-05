package com.boldfaced7.board.Controller;

import com.boldfaced7.board.domain.Member;
import com.boldfaced7.board.dto.AuthDto;
import com.boldfaced7.board.dto.request.AuthRequest;
import com.boldfaced7.board.dto.response.AuthResponse;
import com.boldfaced7.board.auth.SessionConst;
import com.boldfaced7.board.service.AuthService;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.boldfaced7.board.TestUtil.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("API 컨트롤러 - 인증")
@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired MockMvc mvc;
    @Autowired Gson gson;
    @Autowired WebApplicationContext webApplicationContext;
    @MockBean  AuthService authService;

    final String loginUrlTemplate = "/api/login";
    final String logoutUrlTemplate = "/api/logout";

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .alwaysDo(print()) // 모든 요청에 대해 print() 적용
                .build();
    }

    @DisplayName("[API][POST] 로그인 - 정상 호출")
    @Test
    void givenLoginInfo_whenRequestingLogin_thenLogsIn() throws Exception {
        // Given
        MockHttpSession session = new MockHttpSession();

        AuthRequest authRequest = createAuthRequest();
        AuthDto authDto = authRequest.toDto();
        AuthDto returnedAuthDto = createResponseAuthDto();

        given(authService.login(authDto)).willReturn(returnedAuthDto);

        // When & Then
        mvc.perform(post(loginUrlTemplate)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(authRequest))
                )
                .andExpect(status().isOk());

        assertThat(session.isInvalid()).isFalse();

        then(authService).should().login(authDto);
    }

    @Test
    @DisplayName("[API][POST] 로그인 - 이메일 형식 불일치")
    void givenInvalidEmailFormat_whenRequestingSaving_thenReturnsBadRequest() throws Exception {
        // Given

        AuthRequest authRequest = new AuthRequest("notAnEmail", PASSWORD);
        // When & Then
        mvc.perform(post(loginUrlTemplate)
                        .content(new Gson().toJson(authRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("[API][POST] 로그인 - 비밀번호 길이 초과")
    void givenPasswordLengthExceeds_whenRequestingSaving_thenReturnsBadRequest() throws Exception {
        // Given
        String longPassword = "a".repeat(Member.MAX_PASSWORD_LENGTH + 1); // MAX_PASSWORD_LENGTH + 1
        AuthRequest authRequest = new AuthRequest(EMAIL, longPassword);
        // When & Then
        mvc.perform(post(loginUrlTemplate)
                        .content(new Gson().toJson(authRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @DisplayName("[API][GET] 로그아웃 - 정상 호출")
    @Test
    void givenSession_whenRequestingLogout_thenLogsOut() throws Exception {
        // Given
        MockHttpSession session = createSession();
        assertThat(session.isInvalid()).isFalse();

        // When & Then
        mvc.perform(get(logoutUrlTemplate)
                        .session(session)
                )
                .andExpect(status().isOk());

        assertThat(session.isInvalid()).isTrue();

    }

    MockHttpSession createSession() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.AUTH_RESPONSE,
                new AuthResponse(MEMBER_ID, EMAIL, NICKNAME));
        return session;
    }



}