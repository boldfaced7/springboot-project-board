package com.boldfaced7.board.service;

import com.boldfaced7.board.auth.AuthInfoHolder;
import com.boldfaced7.board.domain.Member;
import com.boldfaced7.board.dto.MemberDto;
import com.boldfaced7.board.dto.response.AuthResponse;
import com.boldfaced7.board.error.exception.auth.ForbiddenException;
import com.boldfaced7.board.error.exception.member.MemberNotFoundException;
import com.boldfaced7.board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;

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
        encodePassword(memberDto);
        Member member = memberRepository.save(memberDto.toEntity());
        return member.getId();
    }

    public void updateMember(MemberDto memberDto) {
        authorizeMember(memberDto.getMemberId());
        Member member = findMemberByDto(memberDto);

        encodePassword(memberDto);
        member.update(memberDto.toEntity());
    }

    public void softDeleteMember(MemberDto memberDto) {
        authorizeMember(memberDto.getMemberId());
        Member member = findMemberByDto(memberDto);
        member.deactivate();
    }

    public void hardDeleteMember(MemberDto memberDto) {
        authorizeMember(memberDto.getMemberId());
        Member member = findMemberByDto(memberDto);
        memberRepository.delete(member);
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
    }

    private Member findMemberByDto(MemberDto dto) {
        return findMemberById(dto.getMemberId());
    }

    private void encodePassword(MemberDto memberDto) {
        if (memberDto.getPassword() == null) return;
        String encoded = encoder.encode(memberDto.getPassword());
        memberDto.setPassword(encoded);
    }

    private void authorizeMember(Long memberId) {
        AuthResponse authInfo = AuthInfoHolder.getAuthInfo();
        if (authInfo == null || !authInfo.getMemberId().equals(memberId)) {
            throw new ForbiddenException();
        }
    }
}
