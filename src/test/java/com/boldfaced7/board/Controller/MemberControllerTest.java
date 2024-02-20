package com.boldfaced7.board.Controller;

import com.boldfaced7.board.dto.MemberDto;
import com.boldfaced7.board.dto.request.MemberRequest;
import com.boldfaced7.board.service.MemberService;
import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("API 컨트롤러 - 회원")
@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired MockMvc mvc;
    @Autowired Gson gson;
    @MockBean private MemberService memberService;

    final String urlTemplate = "/api/members";

    @DisplayName("[API][GET] 회원 리스트 조회 - 정상 호출")
    @Test
    void givenNothing_whenRequestingMembers_thenReturnsMembersJsonResponse() throws Exception {
        // Given
        MemberDto memberDto = createMemberDto();
        given(memberService.getMembers()).willReturn(List.of(memberDto));

        // When & Then
        mvc.perform(get(urlTemplate))
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
        Long memberId = 1L;
        MemberDto memberDto = createMemberDto();
        given(memberService.getMember(memberId)).willReturn(memberDto);

        // When & Then
        mvc.perform(get(urlTemplate + "/" + memberId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.memberId").value(memberDto.getMemberId()))
                .andExpect(jsonPath("$.email").value(memberDto.getEmail()))
                .andExpect(jsonPath("$.nickname").value(memberDto.getNickname()));

        then(memberService).should().getMember(memberId);
    }

    @DisplayName("[API][POST] 새 회원 등록 - 정상 호출")
    @Test
    void givenNewMemberInfo_whenRequestingSaving_thenSavesNewMember() throws Exception {
        // Given
        Long memberId = 1L;
        MemberRequest memberRequest = createMemberRequest();
        MemberDto memberDto = memberRequest.toDto();
        given(memberService.saveMember(memberDto)).willReturn(memberId);

        // When & Then
        mvc.perform(post(urlTemplate)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(memberRequest))
                )
                .andExpect(status().isCreated())
                .andExpect(redirectedUrl(urlTemplate + "/" + memberId));

        then(memberService).should().saveMember(memberDto);
    }

    @DisplayName("[API][PATCH] 회원 정보 수정 - 정상 호출")
    @Test
    void givenModifiedMemberInfo_whenRequestingUpdating_thenUpdatesMember() throws Exception {
        // Given
        Long memberId = 1L;
        MemberRequest memberRequest = createMemberRequest();
        MemberDto memberDto = memberRequest.toDto();
        willDoNothing().given(memberService).updateMember(memberId, memberDto);

        // When & Then
        mvc.perform(patch(urlTemplate + "/" + memberId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(memberRequest))
                )
                .andExpect(status().isOk());
        then(memberService).should().updateMember(memberId, memberDto);
    }

    @DisplayName("[API][DELETE] 회원 삭제 - 정상 호출")
    @Test
    void givenMemberId_whenRequestingDeleting_thenDeletesMember() throws Exception {
        // Given
        Long memberId = 1L;
        willDoNothing().given(memberService).softDeleteMember(memberId);

        // When & Then
        mvc.perform(delete(urlTemplate + "/" + memberId))
                .andExpect(status().isOk());
        then(memberService).should().softDeleteMember(memberId);
    }

    private MemberDto createMemberDto() {
        return MemberDto.builder()
                .memberId(1L)
                .email("boldfaced7@email.com")
                .nickname("nickname")
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
    }

    private MemberRequest createMemberRequest() {
        return MemberRequest.builder()
                .email("boldfaced7@email.com")
                .nickname("nickname")
                .password("password")
                .build();
    }

}