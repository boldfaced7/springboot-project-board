package com.boldfaced7.board.service;

import com.boldfaced7.board.domain.Member;
import com.boldfaced7.board.dto.MemberDto;
import com.boldfaced7.board.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("MemberService 테스트")
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks private MemberService memberService;
    @Mock private MemberRepository memberRepository;

    private static final String NO_MEMBER = "탈퇴했거나 존재하지 않는 회원입니다 - memberId: ";

    @DisplayName("[조회] email 존재 여부를 확인")
    @Test
    void givenEmail_whenSearching_thenReturnsTrueOrFalse() {
        //Given
        Member member = createMember(1L);
        String email = member.getEmail();
        String wrongEmail = "wrong" + email;
        given(memberRepository.existsByEmail(email)).willReturn(true);
        given(memberRepository.existsByEmail(wrongEmail)).willReturn(false);

        // When
        boolean isOccupied1 = memberService.isOccupiedEmail(email);
        boolean isOccupied2 = memberService.isOccupiedEmail(wrongEmail);

        // Then
        assertThat(isOccupied1).isTrue();
        assertThat(isOccupied2).isFalse();

        then(memberRepository).should().existsByEmail(email);
        then(memberRepository).should().existsByEmail(wrongEmail);
    }

    @DisplayName("[조회] nickname 존재 여부를 확인")
    @Test
    void givenNickname_whenSearching_thenReturnsTrueOrFalse() {
        //Given
        Member member = createMember(1L);
        String nickname = member.getNickname();
        String wrongNickanem = "wrong" + nickname;
        given(memberRepository.existsByNickname(nickname)).willReturn(true);
        given(memberRepository.existsByNickname(wrongNickanem)).willReturn(false);

        // When
        boolean isOccupied1 = memberService.isOccupiedNickname(nickname);
        boolean isOccupied2 = memberService.isOccupiedNickname(wrongNickanem);

        // Then
        assertThat(isOccupied1).isTrue();
        assertThat(isOccupied2).isFalse();

        then(memberRepository).should().existsByNickname(nickname);
        then(memberRepository).should().existsByNickname(wrongNickanem);

    }

    @DisplayName("[조회] 전체 회원 목록을 반환")
    @Test
    void givenNothing_whenSearching_thenReturnsMembers() {
        //Given
        Long memberId = 1L;
        Member member = createMember(memberId);
        given(memberRepository.findAll()).willReturn(List.of(member));

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
        Long activeMemberId = 1L;
        Member activeMember = createMember(activeMemberId);
        given(memberRepository.findAll(true)).willReturn(List.of(activeMember));

        Long inactiveMemberId = 2L;
        Member inactiveMember = createMember(inactiveMemberId);
        inactiveMember.deactivate();
        given(memberRepository.findAll(false)).willReturn(List.of(inactiveMember));

        // When
        List<MemberDto> activeMemberDtos = memberService.getMembers(true);
        List<MemberDto> inactiveMemberDtos = memberService.getMembers(false);

        // Then
        assertThat(activeMemberDtos.get(0).getMemberId()).isEqualTo(activeMemberId);
        assertThat(inactiveMemberDtos.get(0).getMemberId()).isEqualTo(inactiveMemberId);

        then(memberRepository).should().findAll(true);
        then(memberRepository).should().findAll(false);
    }

    @DisplayName("[조회] id를 입력하면, 회원을 반환")
    @Test
    void givenMemberId_whenSearching_thenReturnsMember() {
        //Given
        Long memberId = 1L;
        Member member = createMember(memberId);
        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));

        // When
        MemberDto memberDto = memberService.getMember(memberId);

        // Then
        assertThat(memberDto.getMemberId()).isEqualTo(memberId);
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
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(NO_MEMBER + wrongMemberId);
        then(memberRepository).should().findById(wrongMemberId);
    }

    @DisplayName("[저장] 회원 정보를 입력하면, 회원을 저장")
    @Test
    void givenMemberInfo_whenSaving_thenSavesMember() {
        //Given
        Long memberId = 1L;
        Member member = createMember(memberId);
        MemberDto memberDto = new MemberDto(member);
        given(memberRepository.save(memberDto.toEntity())).willReturn(member);

        // When
        Long returnedMemberId = memberService.saveMember(memberDto);

        // Then
        assertThat(returnedMemberId).isEqualTo(memberId);
        then(memberRepository).should().save(memberDto.toEntity());
    }

    @DisplayName("[수정] id와 회원 수정 정보를 입력하면, 회원 정보를 수정")
    @Test
    void givenMemberIdAndModifiedMemberInfo_whenUpdatingMember_thenUpdatesMember() {
        //Given
        Long memberId = 1L;
        Member member = createMember(memberId);
        MemberDto memberDto = createMemberDto("newNickname");
        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));

        // When
        memberService.updateMember(memberId, memberDto);

        // Then
        assertThat(member)
                .hasFieldOrPropertyWithValue("nickname", memberDto.getNickname());
        then(memberRepository).should().findById(memberId);

    }

    @DisplayName("[수정] 잘못된 id를 입력하면, 수정 없이 예외를 던짐")
    @Test
    void givenWrongMemberIdAndModifiedMemberInfo_whenUpdatingMember_thenThrowsException() {
        //Given
        Long wrongMemberId = 1L;
        MemberDto memberDto = createMemberDto(1L);
        given(memberRepository.findById(wrongMemberId)).willReturn(Optional.empty());

        // When
        Throwable t = catchThrowable(() -> memberService.updateMember(wrongMemberId, memberDto));

        // Then
        assertThat(t)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(NO_MEMBER + wrongMemberId);
        then(memberRepository).should().findById(wrongMemberId);
    }

    @DisplayName("[삭제] id를 입력하면, 회원을 삭제(soft delete)")
    @Test
    void givenMemberId_whenDeleting_thenDeletesMemberSoftVer() {
        //Given
        Long memberId = 1L;
        Member member = createMember(memberId);
        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));

        // When
        memberService.softDeleteMember(memberId);

        // Then
        assertThat(member)
                .hasFieldOrPropertyWithValue("isActive", false);
        then(memberRepository).should().findById(memberId);
    }

    @DisplayName("[삭제] 잘못된 id를 입력하면, 삭제(soft delete) 없이 예외를 던짐")
    @Test
    void givenWrongId_whenDeleting_thenThrowsExceptionSoftVer() {
        //Given
        Long wrongMemberId = 1L;
        given(memberRepository.findById(wrongMemberId)).willReturn(Optional.empty());

        // When
        Throwable t = catchThrowable(() -> memberService.softDeleteMember(wrongMemberId));

        // Then
        assertThat(t)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(NO_MEMBER + wrongMemberId);
        then(memberRepository).should().findById(wrongMemberId);
    }

    @DisplayName("[삭제] id를 입력하면, 회원을 삭제(hard delete)")
    @Test
    void givenMemberId_whenDeleting_thenDeletesMemberHardVer() {
        //Given
        Long memberId = 1L;
        Member member = createMember(memberId);
        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));

        // When
        memberService.hardDeleteMember(memberId);

        // Then
        then(memberRepository).should().findById(memberId);
        then(memberRepository).should().delete(member);
    }

    @DisplayName("[삭제] 잘못된 id를 입력하면, 삭제(hard delete) 없이 예외를 던짐")
    @Test
    void givenWrongId_whenDeleting_thenThrowsExceptionHardVer() {
        //Given
        Long wrongMemberId = 1L;
        given(memberRepository.findById(wrongMemberId)).willReturn(Optional.empty());

        // When
        Throwable t = catchThrowable(() -> memberService.hardDeleteMember(wrongMemberId));

        // Then
        assertThat(t)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(NO_MEMBER + wrongMemberId);
        then(memberRepository).should().findById(wrongMemberId);
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
    private Member createMember(Long id) {
        Member member = Member.builder()
                .email("boldfaced7@email.com")
                .nickname("nickname")
                .password("password")
                .build();

        ReflectionTestUtils.setField(member, "id", id);
        return member;
    }


    private MemberDto createMemberDto(Long id) {
        return new MemberDto(createMember(id));
    }

    private MemberDto createMemberDto(String nickname) {
        return MemberDto.builder()
                .nickname(nickname)
                .build();
    }


}