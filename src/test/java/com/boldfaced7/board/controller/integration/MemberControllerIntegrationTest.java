package com.boldfaced7.board.controller.integration;

import com.boldfaced7.board.auth.SessionConst;
import com.boldfaced7.board.controller.ControllerTestTemplate;
import com.boldfaced7.board.domain.Member;
import com.boldfaced7.board.dto.request.SaveMemberRequest;
import com.boldfaced7.board.dto.request.UpdateMemberNicknameRequest;
import com.boldfaced7.board.dto.request.UpdateMemberPasswordRequest;
import com.boldfaced7.board.service.MemberService;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

import static com.boldfaced7.board.TestUtil.*;

@DisplayName("MemberController 단위 테스트")
@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired Gson gson;
    @Autowired MemberService memberService;
    ControllerTestTemplate<MemberService> testTemplate;
    MockHttpSession session;

    @BeforeEach
    void setSessionAndTestTemplate() {
        session = new MockHttpSession();
        session.setAttribute(SessionConst.AUTH_RESPONSE, createAuthResponse());
        testTemplate = new ControllerTestTemplate<>(mvc, gson, session, memberService);
    }

    @DisplayName("[GET] 회원 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createGetMemberRequestTests")
    void GetMemberTest(String ignoredMessage, Long memberId, List<ResultMatcher> resultMatchers) throws Exception {
        testTemplate.doGet(null, memberUrl(memberId), resultMatchers);
    }
    static Stream<Arguments> createGetMemberRequestTests() {
        List<ResultMatcher> exists = exists(List.of("memberId", "email", "nickname"), "");
        List<ResultMatcher> doesNotExists = doesNotExists(List.of("password"), "");
        List<ResultMatcher> resultMatchers = Stream.of(exists, doesNotExists, ok(), contentTypeJson()).flatMap(List::stream).toList();

        return Stream.of(
                Arguments.of("정상 호출", 1L, resultMatchers),
                Arguments.of("존재하지 않는 회원", 100000L, notFound())
        );
    }

    @DisplayName("[GET] 회원 리스트 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createGetMembersRequestTest")
    void GetMembersTest(String ignoredMessage, List<ResultMatcher> resultMatchers) throws Exception {
        testTemplate.doGet(null, memberUrl(), resultMatchers);
    }
    static Stream<Arguments> createGetMembersRequestTest() {
        List<ResultMatcher> exists = exists(List.of("memberId", "email", "nickname"), ".members.content[0]");
        List<ResultMatcher> doesNotExists = doesNotExists(List.of("password"), ".members.content[0]");
        List<ResultMatcher> resultMatchers = Stream.of(exists, doesNotExists, ok(), contentTypeJson()).flatMap(List::stream).toList();

        return Stream.of(
                Arguments.of("정상 호출", resultMatchers)
        );
    }

    @DisplayName("[POST] 회원 등록")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createPostRequestTests")
    <T> void PostMemberTest(String ignoredMessage, T request, List<ResultMatcher> resultMatchers) throws Exception {
        testTemplate.doPost(null, signUpUrl(), request, resultMatchers);
    }
    static Stream<Arguments> createPostRequestTests() {
        return Stream.of(
                Arguments.of("정상 호출", new SaveMemberRequest(EMAIL, PASSWORD, NICKNAME), created()),
                Arguments.of("비정상 호출: 이메일 형식 오류", new SaveMemberRequest("a", PASSWORD, NICKNAME), badRequest()),
                Arguments.of("비정상 호출: 이메일 누락", new SaveMemberRequest("", PASSWORD, NICKNAME), badRequest()),
                Arguments.of("비정상 호출: 비밀번호 누락", new SaveMemberRequest(EMAIL, "", NICKNAME), badRequest()),
                Arguments.of("비정상 호출: 닉네임 누락", new SaveMemberRequest(EMAIL, PASSWORD, ""), badRequest()),
                Arguments.of("비정상 호출: 이메일 길이 초과", new SaveMemberRequest("a".repeat(Member.MAX_EMAIL_LENGTH + 1), PASSWORD, NICKNAME), badRequest()),
                Arguments.of("비정상 호출: 비밀번호 길이 초과", new SaveMemberRequest(EMAIL, "a".repeat(Member.MAX_PASSWORD_LENGTH + 1), NICKNAME), badRequest()),
                Arguments.of("비정상 호출: 닉네임 길이 초과", new SaveMemberRequest(EMAIL, PASSWORD, "a".repeat(Member.MAX_NICKNAME_LENGTH + 1)), badRequest())
        );
    }

    @DisplayName("[PATCH] 회원 닉네임 수정")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createPatchNicknameRequestTests")
    <T> void PatchMemberNicknameTest(String ignoredMessage, Long memberId, T request, List<ResultMatcher> resultMatchers) throws Exception {
        testTemplate.doPatch(null, memberNicknameUrl(memberId), request, resultMatchers);
    }
    static Stream<Arguments> createPatchNicknameRequestTests() {
        return Stream.of(
                Arguments.of("정상 호출", 1L, new UpdateMemberNicknameRequest(NICKNAME), ok()),
                Arguments.of("비정상 호출: 타 회원 시도", 2L, new UpdateMemberNicknameRequest(NICKNAME), forbidden()),
                Arguments.of("비정상 호출: 닉네임 누락", 1L, new UpdateMemberNicknameRequest(""), badRequest()),
                Arguments.of("비정상 호출: 닉네임 길이 초과", 1L, new UpdateMemberNicknameRequest("a".repeat(Member.MAX_NICKNAME_LENGTH + 1)), badRequest())
        );
    }

    @DisplayName("[PATCH] 회원 비밀번호 수정")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createPatchPasswordRequestTests")
    <T> void PatchMemberPasswordTest(String ignoredMessage, Long memberId, T request, List<ResultMatcher> resultMatchers) throws Exception {
        testTemplate.doPatch(null, memberPasswordUrl(memberId), request, resultMatchers);
    }
    static Stream<Arguments> createPatchPasswordRequestTests() {
        return Stream.of(
                Arguments.of("정상 호출", 1L, new UpdateMemberPasswordRequest(PASSWORD), ok()),
                Arguments.of("비정상 호출: 타 회원 시도", 2L, new UpdateMemberPasswordRequest(PASSWORD), forbidden()),
                Arguments.of("비정상 호출: 비밀번호 누락", 1L, new UpdateMemberPasswordRequest(""), badRequest()),
                Arguments.of("비정상 호출: 비밀번호 길이 초과", 1L, new UpdateMemberPasswordRequest("a".repeat(Member.MAX_EMAIL_LENGTH + 1)), badRequest())
        );
    }

    @DisplayName("[DELETE] 회원 삭제")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createDeleteRequestTests")
    void DeleteMemberTest(String ignoredMessage, Long memberId, List<ResultMatcher> resultMatchers) throws Exception {
        testTemplate.doDelete(null, memberUrl(memberId), resultMatchers);
    }
    static Stream<Arguments> createDeleteRequestTests() {
        return Stream.of(
                Arguments.of("정상 호출", 1L, ok()),
                Arguments.of("비정상 호출: 타 회원 시도", 2L, forbidden())
        );
    }
}