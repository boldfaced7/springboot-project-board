package com.boldfaced7.board.controller;

import com.boldfaced7.board.Mocker;
import com.boldfaced7.board.auth.SessionConst;
import com.boldfaced7.board.dto.CustomPage;
import com.boldfaced7.board.dto.MemberDto;
import com.boldfaced7.board.dto.request.SaveMemberRequest;
import com.boldfaced7.board.dto.request.UpdateMemberNicknameRequest;
import com.boldfaced7.board.dto.request.UpdateMemberPasswordRequest;
import com.boldfaced7.board.dto.response.MemberListResponse;
import com.boldfaced7.board.dto.response.MemberResponse;
import com.boldfaced7.board.error.exception.auth.ForbiddenException;
import com.boldfaced7.board.error.exception.member.MemberNotFoundException;
import com.boldfaced7.board.service.MemberService;
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

@DisplayName("MemberController 테스트")
@WebMvcTest({MemberController.class})
class MemberControllerTest {

    @Autowired MockMvc mvc;
    @MockBean MemberService memberService;
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
    @MethodSource
    <T> void getMemberTest(Mocker<MemberService> mock, ResultMatcher status, T response) throws Exception {
        testTemplate.doGet(mock, memberUrl(1L), status, response);
    }
    static Stream<Arguments> getMemberTest() {
        Mocker<MemberService> valid = new Mocker<>("정상 호출");
        MemberDto dto = memberDto();
        valid.mocks(m -> m.getMember(any()), dto);

        Mocker<MemberService> notFound = new Mocker<>("비정상 호출: 존재하지 않는 회원");
        notFound.mocksFunction(m -> m.getMember(any()), MemberNotFoundException.class);

        return Stream.of(
                Arguments.of(valid, OK, new MemberResponse(dto)),
                Arguments.of(notFound, NOT_FOUND, null)
        );
    }

    @DisplayName("[GET] 회원 리스트 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    <T> void getMembersTest(Mocker<MemberService> mock, ResultMatcher status, T response) throws Exception {
        testTemplate.doGet(mock, memberUrl(), status, response);
    }
    static Stream<Arguments> getMembersTest() {
        Mocker<MemberService> valid = new Mocker<>("정상 호출");
        CustomPage<MemberDto> page = memberDtos();
        valid.mocks(m -> m.getMembers(any(Pageable.class)), page);

        return Stream.of(
                Arguments.of(valid, OK, new MemberListResponse(page))
        );
    }

    @DisplayName("[POST] 회원 등록")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    void postMemberTest(Mocker<MemberService> mock, String email, String password, String nickname, ResultMatcher status) throws Exception {
        testTemplate.doPost(mock, signUpUrl(), new SaveMemberRequest(email, password, nickname), status);
    }
    static Stream<Arguments> postMemberTest() {
        Mocker<MemberService> valid = new Mocker<>("정상 호출");
        valid.mocks(m -> m.saveMember(any()), 1L);

        return Stream.of(
                Arguments.of(valid, EMAIL, PASSWORD, NICKNAME, CREATED),
                Arguments.of(new Mocker<>("비정상 호출: 이메일 형식 오류"), "a", PASSWORD, NICKNAME, BAD_REQUEST),
                Arguments.of(new Mocker<>("비정상 호출: 이메일 누락"), "", PASSWORD, NICKNAME, BAD_REQUEST),
                Arguments.of(new Mocker<>("비정상 호출: 비밀번호 누락"), EMAIL, "", NICKNAME, BAD_REQUEST),
                Arguments.of(new Mocker<>("비정상 호출: 닉네임 누락"), EMAIL, PASSWORD, "", BAD_REQUEST),
                Arguments.of(new Mocker<>("비정상 호출: 이메일 길이 초과"), EXCEEDED_EMAIL, PASSWORD, NICKNAME, BAD_REQUEST),
                Arguments.of(new Mocker<>("비정상 호출: 비밀번호 길이 초과"), EMAIL, EXCEEDED_PASSWORD, NICKNAME, BAD_REQUEST),
                Arguments.of(new Mocker<>("비정상 호출: 닉네임 길이 초과"), EMAIL, PASSWORD, EXCEEDED_NICKNAME, BAD_REQUEST)
        );
    }

    @DisplayName("[PATCH] 회원 닉네임 수정")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    void patchMemberNicknameTest(Mocker<MemberService> mock, String nickname, ResultMatcher status) throws Exception {
        testTemplate.doPatch(mock, memberNicknameUrl(1L), new UpdateMemberNicknameRequest(nickname), status);
    }
    static Stream<Arguments> patchMemberNicknameTest() {
        Mocker<MemberService> valid = new Mocker<>("정상 호출");
        valid.mocks(m -> m.updateMember(any()));

        Mocker<MemberService> forbidden = new Mocker<>("비정상 호출: 타 회원 시도");
        forbidden.mocksConsumer(m -> m.updateMember(any()), ForbiddenException.class);

        return Stream.of(
                Arguments.of(valid, NICKNAME, OK),
                Arguments.of(forbidden, NICKNAME, FORBIDDEN),
                Arguments.of(new Mocker<>("비정상 호출: 닉네임 누락"), "", BAD_REQUEST),
                Arguments.of(new Mocker<>("비정상 호출: 닉네임 길이 초과"), EXCEEDED_NICKNAME, BAD_REQUEST)
        );
    }

    @DisplayName("[PATCH] 회원 비밀번호 수정")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    void patchMemberPasswordTest(Mocker<MemberService> mock, String password, ResultMatcher status) throws Exception {
        testTemplate.doPatch(mock, memberPasswordUrl(1L), new UpdateMemberPasswordRequest(password), status);
    }
    static Stream<Arguments> patchMemberPasswordTest() {
        Mocker<MemberService> valid = new Mocker<>("정상 호출");
        valid.mocks(m -> m.updateMember(any()));

        Mocker<MemberService> forbidden = new Mocker<>("비정상 호출: 타 회원 시도");
        forbidden.mocksConsumer(m -> m.updateMember(any()), ForbiddenException.class);

        return Stream.of(
                Arguments.of(valid, PASSWORD, OK),
                Arguments.of(forbidden, PASSWORD, FORBIDDEN),
                Arguments.of(new Mocker<>("비정상 호출: 비밀번호 누락"), "", BAD_REQUEST),
                Arguments.of(new Mocker<>("비정상 호출: 비밀번호 길이 초과"), EXCEEDED_PASSWORD, BAD_REQUEST)
        );
    }

    @DisplayName("[DELETE] 회원 삭제")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    void deleteMemberTest(Mocker<MemberService> mock, ResultMatcher status) throws Exception {
        testTemplate.doDelete(mock, memberUrl(1L), status);
    }
    static Stream<Arguments> deleteMemberTest() {
        Mocker<MemberService> valid = new Mocker<>("정상 호출");
        valid.mocks(m -> m.softDeleteMember(any()));

        Mocker<MemberService> forbidden = new Mocker<>("비정상 호출: 타 회원 시도");
        forbidden.mocksConsumer(m -> m.softDeleteMember(any()), ForbiddenException.class);

        return Stream.of(
                Arguments.of(valid, OK),
                Arguments.of(forbidden, FORBIDDEN)
        );
    }
}