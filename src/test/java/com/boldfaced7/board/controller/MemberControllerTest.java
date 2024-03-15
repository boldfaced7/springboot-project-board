package com.boldfaced7.board.controller;

import com.boldfaced7.board.Context;
import com.boldfaced7.board.auth.SessionConst;
import com.boldfaced7.board.domain.Member;
import com.boldfaced7.board.dto.MemberDto;
import com.boldfaced7.board.dto.request.SaveMemberRequest;
import com.boldfaced7.board.dto.request.UpdateMemberNicknameRequest;
import com.boldfaced7.board.dto.request.UpdateMemberPasswordRequest;
import com.boldfaced7.board.error.exception.member.MemberNotFoundException;
import com.boldfaced7.board.error.exception.auth.ForbiddenException;
import com.boldfaced7.board.service.MemberService;
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

@DisplayName("MemberController 테스트")
@WebMvcTest({MemberController.class})
class MemberControllerTest {

    @Autowired MockMvc mvc;
    @Autowired Gson gson;
    @MockBean MemberService memberService;
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
    void GetMemberTest(String ignoredMessage, Context<MemberService> context, List<ResultMatcher> resultMatchers) throws Exception {
        testTemplate.doGet(context, memberUrl(MEMBER_ID), resultMatchers);
    }
    static Stream<Arguments> createGetMemberRequestTests() {
        Map<String, Context<MemberService>> contexts = Map.of(
                VALID, new Context<>(getMember, MEMBER_ID, createMemberDto()),
                NOT_FOUND, new Context<>(getMember, MEMBER_ID, new MemberNotFoundException())
        );
        List<ResultMatcher> exists = exists(List.of("memberId", "email", "nickname"), "");
        List<ResultMatcher> doesNotExists = doesNotExists(List.of("password"), "");
        List<ResultMatcher> resultMatchers = Stream.of(exists, doesNotExists, ok(), contentTypeJson()).flatMap(List::stream).toList();

        return Stream.of(
                Arguments.of("정상 호출", contexts.get(VALID), resultMatchers),
                Arguments.of("존재하지 않는 회원", contexts.get(NOT_FOUND), notFound())
        );
    }

    @DisplayName("[GET] 회원 리스트 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createGetMembersRequestTest")
    void GetMembersTest(String ignoredMessage, Context<MemberService> context, List<ResultMatcher> resultMatchers) throws Exception {
        testTemplate.doGet(context, memberUrl(), resultMatchers);
    }
    static Stream<Arguments> createGetMembersRequestTest() {
        Map<String, Context<MemberService>> contexts = Map.of(
                VALID, new Context<>(getMembers, List.of(createMemberDto()))
        );
        List<ResultMatcher> exists = exists(List.of("memberId", "email", "nickname"), ".members[0]");
        List<ResultMatcher> doesNotExists = doesNotExists(List.of("password"), ".members[0]");
        List<ResultMatcher> resultMatchers = Stream.of(exists, doesNotExists, ok(), contentTypeJson()).flatMap(List::stream).toList();

        return Stream.of(
                Arguments.of("정상 호출", contexts.get(VALID), resultMatchers)
        );
    }

    @DisplayName("[POST] 회원 등록")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createPostRequestTests")
    <T> void PostMemberTest(String ignoredMessage, Context<MemberService> context, T request, List<ResultMatcher> resultMatchers) throws Exception {
        testTemplate.doPost(context, signUpUrl(), request, resultMatchers);
    }
    static Stream<Arguments> createPostRequestTests() {
        SaveMemberRequest validRequest = new SaveMemberRequest(EMAIL, PASSWORD, NICKNAME);
        MemberDto validRequestDto = validRequest.toDto();

        Map<String, Context<MemberService>> contexts = Map.of(
                VALID, new Context<>(saveMember, validRequestDto, MEMBER_ID)
        );
        return Stream.of(
                Arguments.of("정상 호출", contexts.get(VALID), validRequest, created()),
                Arguments.of("비정상 호출: 이메일 형식 오류", null, new SaveMemberRequest("a", PASSWORD, NICKNAME), badRequest()),
                Arguments.of("비정상 호출: 이메일 누락", null, new SaveMemberRequest("", PASSWORD, NICKNAME), badRequest()),
                Arguments.of("비정상 호출: 비밀번호 누락", null, new SaveMemberRequest(EMAIL, "", NICKNAME), badRequest()),
                Arguments.of("비정상 호출: 닉네임 누락", null, new SaveMemberRequest(EMAIL, PASSWORD, ""), badRequest()),
                Arguments.of("비정상 호출: 이메일 길이 초과", null, new SaveMemberRequest("a".repeat(Member.MAX_EMAIL_LENGTH + 1), PASSWORD, NICKNAME), badRequest()),
                Arguments.of("비정상 호출: 비밀번호 길이 초과", null, new SaveMemberRequest(EMAIL, "a".repeat(Member.MAX_PASSWORD_LENGTH + 1), NICKNAME), badRequest()),
                Arguments.of("비정상 호출: 닉네임 길이 초과", null, new SaveMemberRequest(EMAIL, PASSWORD, "a".repeat(Member.MAX_NICKNAME_LENGTH + 1)), badRequest())
        );
    }
    @DisplayName("[PATCH] 회원 닉네임 수정")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createPatchNicknameRequestTests")
    <T> void PatchMemberNicknameTest(String ignoredMessage, Context<MemberService> context, T request, List<ResultMatcher> resultMatchers) throws Exception {
        testTemplate.doPatch(context, memberNicknameUrl(MEMBER_ID), request, resultMatchers);
    }
    static Stream<Arguments> createPatchNicknameRequestTests() {
        UpdateMemberNicknameRequest validRequest = new UpdateMemberNicknameRequest(NICKNAME);
        MemberDto validRequestDto = validRequest.toDto(MEMBER_ID);

        Map<String, Context<MemberService>> contexts = Map.of(
                VALID, new Context<>(updateMemberNickname, validRequestDto),
                NOT_FOUND, new Context<>(updateMemberNickname, validRequestDto, new MemberNotFoundException()),
                FORBIDDEN, new Context<>(updateMemberNickname, validRequestDto, new ForbiddenException())
        );
        return Stream.of(
                Arguments.of("정상 호출", contexts.get(VALID), validRequest, ok()),
                Arguments.of("비정상 호출: 존재하지 않는 회원", contexts.get(NOT_FOUND), validRequest, notFound()),
                Arguments.of("비정상 호출: 타 회원 시도", contexts.get(FORBIDDEN), validRequest, forbidden()),
                Arguments.of("비정상 호출: 닉네임 누락", null, new UpdateMemberNicknameRequest(""), badRequest()),
                Arguments.of("비정상 호출: 닉네임 길이 초과", null, new UpdateMemberNicknameRequest("a".repeat(Member.MAX_NICKNAME_LENGTH + 1)), badRequest())
        );
    }

    @DisplayName("[PATCH] 회원 비밀번호 수정")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createPatchPasswordRequestTests")
    <T> void PatchMemberPasswordTest(String ignoredMessage, Context<MemberService> context, T request, List<ResultMatcher> resultMatchers) throws Exception {
        testTemplate.doPatch(context, memberPasswordUrl(MEMBER_ID), request, resultMatchers);
    }
    static Stream<Arguments> createPatchPasswordRequestTests() {
        UpdateMemberPasswordRequest validRequest = new UpdateMemberPasswordRequest(PASSWORD);
        MemberDto validRequestDto = validRequest.toDto(MEMBER_ID);

        Map<String, Context<MemberService>> contexts = Map.of(
                VALID, new Context<>(updateMemberPassword, validRequestDto),
                NOT_FOUND, new Context<>(updateMemberPassword, validRequestDto, new MemberNotFoundException()),
                FORBIDDEN, new Context<>(updateMemberPassword, validRequestDto, new ForbiddenException())
        );
        return Stream.of(
                Arguments.of("정상 호출", contexts.get(VALID), validRequest, ok()),
                Arguments.of("비정상 호출: 존재하지 않는 회원", contexts.get(NOT_FOUND), validRequest, notFound()),
                Arguments.of("비정상 호출: 타 회원 시도", contexts.get(FORBIDDEN), validRequest, forbidden()),
                Arguments.of("비정상 호출: 비밀번호 누락", null, new UpdateMemberPasswordRequest(""), badRequest()),
                Arguments.of("비정상 호출: 비밀번호 길이 초과", null, new UpdateMemberPasswordRequest("a".repeat(Member.MAX_EMAIL_LENGTH + 1)), badRequest())
        );
    }

    @DisplayName("[DELETE] 회원 삭제")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createDeleteRequestTests")
    void DeleteMemberTest(String ignoredMessage, Context<MemberService> context, List<ResultMatcher> resultMatchers) throws Exception {
        testTemplate.doDelete(context, memberUrl(MEMBER_ID), resultMatchers);
    }
    static Stream<Arguments> createDeleteRequestTests() {
        MemberDto validMemberDto = MemberDto.builder().memberId(MEMBER_ID).build();

        Map<String, Context<MemberService>> contexts = Map.of(
                VALID, new Context<>(softDeleteMember, validMemberDto),
                NOT_FOUND, new Context<>(softDeleteMember, validMemberDto, new MemberNotFoundException()),
                FORBIDDEN, new Context<>(softDeleteMember, validMemberDto, new ForbiddenException())
        );
        return Stream.of(
                Arguments.of("정상 호출", contexts.get(VALID), ok()),
                Arguments.of("비정상 호출: 존재하지 않는 회원", contexts.get(NOT_FOUND), notFound()),
                Arguments.of("비정상 호출: 타 회원 시도", contexts.get(FORBIDDEN), forbidden())
        );
    }
}