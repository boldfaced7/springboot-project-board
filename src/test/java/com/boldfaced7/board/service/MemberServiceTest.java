package com.boldfaced7.board.service;

import com.boldfaced7.board.auth.AuthInfoHolder;
import com.boldfaced7.board.domain.Member;
import com.boldfaced7.board.dto.MemberDto;
import com.boldfaced7.board.error.ErrorCode;
import com.boldfaced7.board.error.exception.auth.ForbiddenException;
import com.boldfaced7.board.error.exception.member.MemberNotFoundException;
import com.boldfaced7.board.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static com.boldfaced7.board.TestUtil.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@DisplayName("MemberService 테스트")
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks private MemberService memberService;
    @Mock private MemberRepository memberRepository;
    @Mock private BCryptPasswordEncoder encoder;

    @BeforeEach
    void setUp() {
        AuthInfoHolder.setAuthInfo(createAuthResponse());
    }

    @AfterEach
    void clear() {
        AuthInfoHolder.releaseAuthInfo();
    }

    @DisplayName("[조회] email 존재 여부를 확인")
    @Test
    void givenEmail_whenSearching_thenReturnsTrueOrFalse() {
        //Given
        String wrongEmail = "wrong" + EMAIL;
        given(memberRepository.existsByEmail(EMAIL)).willReturn(true);
        given(memberRepository.existsByEmail(wrongEmail)).willReturn(false);

        // When
        boolean isOccupied1 = memberService.isOccupiedEmail(EMAIL);
        boolean isOccupied2 = memberService.isOccupiedEmail(wrongEmail);

        // Then
        assertThat(isOccupied1).isTrue();
        assertThat(isOccupied2).isFalse();

        then(memberRepository).should().existsByEmail(EMAIL);
        then(memberRepository).should().existsByEmail(wrongEmail);
    }

    @DisplayName("[조회] nickname 존재 여부를 확인")
    @Test
    void givenNickname_whenSearching_thenReturnsTrueOrFalse() {
        //Given
        String wrongNickname = "wrong" + NICKNAME;
        given(memberRepository.existsByNickname(NICKNAME)).willReturn(true);
        given(memberRepository.existsByNickname(wrongNickname)).willReturn(false);

        // When
        boolean isOccupied1 = memberService.isOccupiedNickname(NICKNAME);
        boolean isOccupied2 = memberService.isOccupiedNickname(wrongNickname);

        // Then
        assertThat(isOccupied1).isTrue();
        assertThat(isOccupied2).isFalse();

        then(memberRepository).should().existsByNickname(NICKNAME);
        then(memberRepository).should().existsByNickname(wrongNickname);

    }

    @DisplayName("[조회] 전체 회원 목록을 반환")
    @Test
    void givenNothing_whenSearching_thenReturnsMembers() {
        //Given
        given(memberRepository.findAll()).willReturn(List.of(createMember()));

        // When
        List<MemberDto> memberDtos = memberService.getMembers();

        // Then
        assertThat(memberDtos).isNotEmpty();
        then(memberRepository).should().findAll();
    }

    @DisplayName("[조회] 활성/비활성 회원 목록을 반환")
    @Test
    void givenTrueOrFalse_whenSearching_thenReturnsActiveOrInactiveMembers() {
        //Given
        Member activeMember = createMember();
        given(memberRepository.findAll(true)).willReturn(List.of(activeMember));

        Member inactiveMember = createMember();
        ReflectionTestUtils.setField(inactiveMember, "id", 2L);
        inactiveMember.deactivate();
        given(memberRepository.findAll(false)).willReturn(List.of(inactiveMember));

        // When
        List<MemberDto> activeMemberDtos = memberService.getMembers(true);
        List<MemberDto> inactiveMemberDtos = memberService.getMembers(false);

        // Then
        assertThat(activeMemberDtos.get(0).getMemberId()).isEqualTo(activeMember.getId());
        assertThat(inactiveMemberDtos.get(0).getMemberId()).isEqualTo(inactiveMember.getId());

        then(memberRepository).should().findAll(true);
        then(memberRepository).should().findAll(false);
    }

    @DisplayName("[조회] id를 입력하면, 회원을 반환")
    @Test
    void givenMemberId_whenSearching_thenReturnsMember() {
        //Given
        Member member = createMember();
        given(memberRepository.findById(MEMBER_ID)).willReturn(Optional.of(member));

        // When
        MemberDto memberDto = memberService.getMember(MEMBER_ID);

        // Then
        assertThat(memberDto.getMemberId()).isEqualTo(MEMBER_ID);
    }

    @DisplayName("[조회] 잘못된 id를 입력하면, 반환 없이 예외를 던짐")
    @Test
    void givenWrongMemberId_whenSearching_thenThrowsException() {
        //Given
        Long wrongMemberId = 1L;
        given(memberRepository.findById(wrongMemberId)).willReturn(Optional.empty());

        // When
        Throwable t = catchThrowable(() -> memberService.getMember(wrongMemberId));

        // Then
        assertThat(t)
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessage(ErrorCode.MEMBER_NOT_FOUND.getMessage());

        then(memberRepository).should().findById(wrongMemberId);
    }

    @DisplayName("[저장] 회원 정보를 입력하면, 회원을 저장")
    @Test
    void givenMemberInfo_whenSaving_thenSavesMember() {
        //Given
        Member member = createMember();
        MemberDto memberDto = createRequestMemberDto();

        given(memberRepository.save(memberDto.toEntity())).willReturn(member);
        given(encoder.encode(memberDto.getPassword())).willReturn(PASSWORD);

        // When
        Long returnedMemberId = memberService.saveMember(memberDto);

        // Then
        assertThat(returnedMemberId).isEqualTo(MEMBER_ID);
        then(memberRepository).should().save(memberDto.toEntity());
        then(encoder).should().encode(memberDto.getPassword());
    }

    @DisplayName("[수정] id와 회원 수정 정보를 입력하면, 회원 정보를 수정")
    @Test
    void givenMemberIdAndModifiedMemberInfo_whenUpdatingMember_thenUpdatesMember() {
        //Given
        Member member = createMember();
        MemberDto memberDto = createRequestMemberDto(MEMBER_ID);
        memberDto.setNickname("new nickname");

        given(memberRepository.findById(memberDto.getMemberId())).willReturn(Optional.of(member));

        // When
        memberService.updateMember(memberDto);

        // Then
        assertThat(member)
                .hasFieldOrPropertyWithValue("nickname", memberDto.getNickname());

        then(memberRepository).should().findById(MEMBER_ID);

    }

    @DisplayName("[수정] 잘못된 회원이 접근하면, 반환 없이 예외를 던짐")
    @Test
    void givenWrongMemberAndModifiedMemberInfo_whenUpdatingMember_thenThrowsException() {
        //Given
        Long wrongMemberId = 2L;
        AuthInfoHolder.setAuthInfo(createAuthResponse(wrongMemberId));

        MemberDto dto = createRequestMemberDto(MEMBER_ID);
        dto.setNickname("new nickname");

        // When
        Throwable t = catchThrowable(() -> memberService.updateMember(dto));

        // Then
        assertThat(wrongMemberId).isNotEqualTo(MEMBER_ID);
        assertThat(t)
                .isInstanceOf(ForbiddenException.class)
                .hasMessage(ErrorCode.FORBIDDEN.getMessage());
    }

    @DisplayName("[삭제] id를 입력하면, 회원을 삭제(soft delete)")
    @Test
    void givenMemberId_whenDeleting_thenDeletesMemberSoftVer() {
        //Given
        Member member = createMember();
        MemberDto dto = createRequestMemberDto(MEMBER_ID);
        given(memberRepository.findById(dto.getMemberId())).willReturn(Optional.of(member));

        // When
        memberService.softDeleteMember(dto);

        // Then
        assertThat(member)
                .hasFieldOrPropertyWithValue("isActive", false);

        then(memberRepository).should().findById(MEMBER_ID);
    }

    @DisplayName("[삭제] 잘못된 회원이 접근하면, 삭제 없이 예외를 던짐(soft delete)")
    @Test
    void givenWrongMemberAndModifiedMemberInfo_whenDeletingMember_thenThrowsExceptionSoftVer() {
        //Given
        Long wrongMemberId = 2L;
        AuthInfoHolder.setAuthInfo(createAuthResponse(wrongMemberId));

        MemberDto dto = createRequestMemberDto(MEMBER_ID);

        // When
        Throwable t = catchThrowable(() -> memberService.softDeleteMember(dto));

        // Then
        assertThat(wrongMemberId).isNotEqualTo(MEMBER_ID);
        assertThat(t)
                .isInstanceOf(ForbiddenException.class)
                .hasMessage(ErrorCode.FORBIDDEN.getMessage());
    }

    @DisplayName("[삭제] id를 입력하면, 회원을 삭제(hard delete)")
    @Test
    void givenMemberId_whenDeleting_thenDeletesMemberHardVer() {
        //Given
        Member member = createMember();
        MemberDto memberDto = createRequestMemberDto(MEMBER_ID);

        given(memberRepository.findById(memberDto.getMemberId())).willReturn(Optional.of(member));
        willDoNothing().given(memberRepository).delete(member);

        // When
        memberService.hardDeleteMember(memberDto);

        // Then
        then(memberRepository).should().findById(MEMBER_ID);
        then(memberRepository).should().delete(member);
    }

    @DisplayName("[삭제] 잘못된 회원이 접근하면, 반환 없이 예외를 던짐(hard delete)")
    @Test
    void givenWrongMemberAndModifiedMemberInfo_whenDeletingMember_thenThrowsExceptionHardVer() {
        //Given
        Long wrongMemberId = 2L;
        AuthInfoHolder.setAuthInfo(createAuthResponse(wrongMemberId));

        MemberDto dto = createRequestMemberDto(MEMBER_ID);

        // When
        Throwable t = catchThrowable(() -> memberService.hardDeleteMember(dto));

        // Then
        assertThat(wrongMemberId).isNotEqualTo(MEMBER_ID);
        assertThat(t)
                .isInstanceOf(ForbiddenException.class)
                .hasMessage(ErrorCode.FORBIDDEN.getMessage());
    }
    /*
    @DisplayName("[] ")
    @Test
    void given_when_then() {
        //Given


        // When
        memberService.

        // Then

    }
 */
}