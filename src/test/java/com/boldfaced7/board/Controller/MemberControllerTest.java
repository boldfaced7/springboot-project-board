package com.boldfaced7.board.Controller;

import com.boldfaced7.board.domain.Member;
import com.boldfaced7.board.dto.MemberDto;
import com.boldfaced7.board.dto.request.SaveMemberRequest;
import com.boldfaced7.board.auth.SessionConst;
import com.boldfaced7.board.service.MemberService;
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

import java.util.List;

import static com.boldfaced7.board.TestUtil.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("API 컨트롤러 - 회원")
@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired MockMvc mvc;
    @Autowired Gson gson;
    @Autowired WebApplicationContext webApplicationContext;
    @MockBean  MemberService memberService;

    MockHttpSession session;

    @BeforeEach
    void setUp() {
        session = new MockHttpSession();
        session.setAttribute(SessionConst.AUTH_RESPONSE, createAuthResponse());

        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .alwaysDo(print()) // 모든 요청에 대해 print() 적용
                .build();
    }

    @DisplayName("[API][GET] 회원 리스트 조회 - 정상 호출")
    @Test
    void givenNothing_whenRequestingMembers_thenReturnsMembersJsonResponse() throws Exception {
        // Given
        MemberDto memberDto = createRequestMemberDto();
        given(memberService.getMembers()).willReturn(List.of(memberDto));

        // When & Then
        mvc.perform(get(memberUrl())
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.members[0].memberId").value(memberDto.getMemberId()))
                .andExpect(jsonPath("$.members[0].email").value(memberDto.getEmail()))
                .andExpect(jsonPath("$.members[0].nickname").value(memberDto.getNickname()));

        then(memberService).should().getMembers();
    }

    @DisplayName("[API][GET] 회원 단건 조회 - 정상 호출")
    @Test
    void givenNothing_whenRequestingMember_thenReturnsMemberJsonResponse() throws Exception {
        // Given
        MemberDto memberDto = createRequestMemberDto();
        given(memberService.getMember(MEMBER_ID)).willReturn(memberDto);

        // When & Then
        mvc.perform(get(memberUrl(MEMBER_ID))
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.memberId").value(memberDto.getMemberId()))
                .andExpect(jsonPath("$.email").value(memberDto.getEmail()))
                .andExpect(jsonPath("$.nickname").value(memberDto.getNickname()));

        then(memberService).should().getMember(MEMBER_ID);
    }

    @DisplayName("[API][POST] 새 회원 등록 - 정상 호출")
    @Test
    void givenNewMemberInfo_whenRequestingSaving_thenSavesNewMember() throws Exception {
        // Given
        SaveMemberRequest saveMemberRequest = createSaveMemberRequest();
        MemberDto memberDto = saveMemberRequest.toDto();
        given(memberService.saveMember(memberDto)).willReturn(MEMBER_ID);

        // When & Then
        mvc.perform(post("/api/signUp")
                        .session(session)
                        .content(gson.toJson(saveMemberRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(redirectedUrl(memberUrl(MEMBER_ID)));

        then(memberService).should().saveMember(memberDto);
    }

    @Test
    @DisplayName("[API][POST] 새 회원 등록 - 이메일 형식 불일치")
    void givenInvalidEmailFormat_whenRequestingSaving_thenReturnsBadRequest() throws Exception {
        // Given
        SaveMemberRequest saveMemberRequest = new SaveMemberRequest("notAnEmail", PASSWORD, NICKNAME);
        // When & Then
        mvc.perform(post("/api/signUp")
                        .content(new Gson().toJson(saveMemberRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("[API][POST] 새 회원 등록 - 비밀번호 길이 초과")
    void givenPasswordLengthExceeds_whenRequestingSaving_thenReturnsBadRequest() throws Exception {
        // Given
        String longPassword = "a".repeat(Member.MAX_PASSWORD_LENGTH + 1); // MAX_PASSWORD_LENGTH + 1
        SaveMemberRequest saveMemberRequest = new SaveMemberRequest(EMAIL, longPassword, NICKNAME);
        // When & Then
        mvc.perform(post("/api/signUp")
                        .content(new Gson().toJson(saveMemberRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("[API][POST] 새 회원 등록 - 닉네임 누락")
    void givenMissingNickname_whenRequestingSaving_thenReturnsBadRequest() throws Exception {
        // Given
        SaveMemberRequest saveMemberRequest = new SaveMemberRequest(EMAIL, PASSWORD, "");
        // When & Then
        mvc.perform(post("/api/signUp")
                        .content(new Gson().toJson(saveMemberRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @DisplayName("[API][PATCH] 회원 정보 수정 - 정상 호출")
    @Test
    void givenModifiedMemberInfo_whenRequestingUpdating_thenUpdatesMember() throws Exception {
        // Given
        SaveMemberRequest memberRequest = createSaveMemberRequest();
        willDoNothing().given(memberService).updateMember(any(MemberDto.class));

        // When & Then
        mvc.perform(patch(memberUrl(MEMBER_ID))
                        .session(session)
                        .content(gson.toJson(memberRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
        then(memberService).should().updateMember(any(MemberDto.class));
    }

    @DisplayName("[API][DELETE] 회원 삭제 - 정상 호출")
    @Test
    void givenMemberId_whenRequestingDeleting_thenDeletesMember() throws Exception {
        // Given
        willDoNothing().given(memberService).softDeleteMember(any());

        // When & Then
        mvc.perform(delete(memberUrl(MEMBER_ID))
                        .session(session))
                .andExpect(status().isOk());
        then(memberService).should().softDeleteMember(any());
    }
}