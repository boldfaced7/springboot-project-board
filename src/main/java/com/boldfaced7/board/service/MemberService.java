package com.boldfaced7.board.service;

import com.boldfaced7.board.domain.Member;
import com.boldfaced7.board.dto.MemberDto;
import com.boldfaced7.board.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private static final String NO_MEMBER = "탈퇴했거나 존재하지 않는 회원입니다 - memberId: ";

    @Transactional(readOnly = true)
    public boolean isOccupiedEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public boolean isOccupiedNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    @Transactional(readOnly = true)
    public List<MemberDto> getMembers() {
        return memberRepository.findAll().stream()
                .map(MemberDto::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MemberDto> getMembers(boolean isActive) {
        return memberRepository.findAll(isActive).stream()
                .map(MemberDto::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public MemberDto getMember(Long memberId) {
        Member member = findMemberById(memberId);
        return new MemberDto(member);
    }

    public Long saveMember(MemberDto memberDto) {
        Member member = memberRepository.save(memberDto.toEntity());
        return member.getId();
    }

    public void updateMember(Long memberId, MemberDto memberDto) {
        Member member = findMemberById(memberId);
        member.update(memberDto.toEntity());
    }

    public void softDeleteMember(Long memberId) {
        Member member = findMemberById(memberId);
        member.deactivate();
    }

    public void hardDeleteMember(Long memberId) {
        Member member = findMemberById(memberId);
        memberRepository.delete(member);
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException(NO_MEMBER + memberId));
    }
}
