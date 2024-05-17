package com.boldfaced7.board.controller.integration;

import com.boldfaced7.board.auth.SessionConst;
import com.boldfaced7.board.controller.ControllerTestTemplate;
import com.boldfaced7.board.dto.request.SaveMemberRequest;
import com.boldfaced7.board.dto.request.UpdateMemberNicknameRequest;
import com.boldfaced7.board.dto.request.UpdateMemberPasswordRequest;
import com.boldfaced7.board.service.MemberService;
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

import java.util.stream.Stream;

import static com.boldfaced7.board.TestUtil.*;

@ActiveProfiles("test")
@DisplayName("MemberController 단위 테스트")
@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired MemberService memberService;
    ControllerTestTemplate<MemberService> testTemplate;
    MockHttpSession session;

    @BeforeEach
    void setSessionAndTestTemplate() {
        session = new MockHttpSession();
        session.setAttribute(SessionConst.AUTH_RESPONSE, authResponse());
        testTemplate = new ControllerTestTemplate<>(mvc, session, memberService);
    }

    @DisplayName("[GET] 회원 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createGetMemberRequestTests")
    void GetMemberTest(String ignoredMessage, Long memberId, ResultMatcher status) throws Exception {
        testTemplate.doGet(memberUrl(memberId), status);
    }
    static Stream<Arguments> createGetMemberRequestTests() {
        return Stream.of(
                Arguments.of("정상 호출", 1L, OK),
                Arguments.of("존재하지 않는 회원", 100000L, NOT_FOUND)
        );
    }

    @DisplayName("[GET] 회원 리스트 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createGetMembersRequestTest")
    void GetMembersTest(String ignoredMessage, ResultMatcher status) throws Exception {
        testTemplate.doGet(memberUrl(), status);
    }
    static Stream<Arguments> createGetMembersRequestTest() {
        return Stream.of(
                Arguments.of("정상 호출", OK)
        );
    }

    @DisplayName("[POST] 회원 등록")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createPostRequestTests")
    void PostMemberTest(String ignoredMessage, String email, String password, String nickname, ResultMatcher status) throws Exception {
        testTemplate.doPost(signUpUrl(), new SaveMemberRequest(email, password, nickname), status);
    }
    static Stream<Arguments> createPostRequestTests() {
        return Stream.of(
                Arguments.of("정상 호출", EMAIL, PASSWORD, NICKNAME, CREATED),
                Arguments.of("비정상 호출: 이메일 형식 오류", "a", PASSWORD, NICKNAME, BAD_REQUEST),
                Arguments.of("비정상 호출: 이메일 누락", "", PASSWORD, NICKNAME, BAD_REQUEST),
                Arguments.of("비정상 호출: 비밀번호 누락", EMAIL, "", NICKNAME, BAD_REQUEST),
                Arguments.of("비정상 호출: 닉네임 누락", EMAIL, PASSWORD, "", BAD_REQUEST),
                Arguments.of("비정상 호출: 이메일 길이 초과", EXCEEDED_EMAIL, PASSWORD, NICKNAME, BAD_REQUEST),
                Arguments.of("비정상 호출: 비밀번호 길이 초과", EMAIL, EXCEEDED_PASSWORD, NICKNAME, BAD_REQUEST),
                Arguments.of("비정상 호출: 닉네임 길이 초과", EMAIL, PASSWORD, EXCEEDED_NICKNAME, BAD_REQUEST)
        );
    }

    @DisplayName("[PATCH] 회원 닉네임 수정")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createPatchNicknameRequestTests")
    void PatchMemberNicknameTest(String ignoredMessage, Long memberId, String nickname, ResultMatcher status) throws Exception {
        testTemplate.doPatch(memberNicknameUrl(memberId), new UpdateMemberNicknameRequest(nickname), status);
    }
    static Stream<Arguments> createPatchNicknameRequestTests() {
        return Stream.of(
                Arguments.of("정상 호출", 1L, NICKNAME, OK),
                Arguments.of("비정상 호출: 타 회원 시도", 2L, NICKNAME, FORBIDDEN),
                Arguments.of("비정상 호출: 닉네임 누락", 1L, "", BAD_REQUEST),
                Arguments.of("비정상 호출: 닉네임 길이 초과", 1L, EXCEEDED_NICKNAME, BAD_REQUEST)
        );
    }

    @DisplayName("[PATCH] 회원 비밀번호 수정")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createPatchPasswordRequestTests")
    void PatchMemberPasswordTest(String ignoredMessage, Long memberId, String password, ResultMatcher status) throws Exception {
        testTemplate.doPatch(memberPasswordUrl(memberId), new UpdateMemberPasswordRequest(password), status);
    }
    static Stream<Arguments> createPatchPasswordRequestTests() {
        return Stream.of(
                Arguments.of("정상 호출", 1L, PASSWORD, OK),
                Arguments.of("비정상 호출: 타 회원 시도", 2L, PASSWORD, FORBIDDEN),
                Arguments.of("비정상 호출: 비밀번호 누락", 1L, "", BAD_REQUEST),
                Arguments.of("비정상 호출: 비밀번호 길이 초과", 1L, EXCEEDED_PASSWORD, BAD_REQUEST)
        );
    }

    @DisplayName("[DELETE] 회원 삭제")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createDeleteRequestTests")
    void DeleteMemberTest(String ignoredMessage, Long memberId, ResultMatcher status) throws Exception {
        testTemplate.doDelete(memberUrl(memberId), status);
    }
    static Stream<Arguments> createDeleteRequestTests() {
        return Stream.of(
                Arguments.of("정상 호출", 1L, OK),
                Arguments.of("비정상 호출: 타 회원 시도", 2L, FORBIDDEN)
        );
    }
}