package com.boldfaced7.member.application;

import com.boldfaced7.common.auth.AuthInfoHolder;
import com.boldfaced7.member.domain.Member;
import com.boldfaced7.common.CustomPage;
import com.boldfaced7.common.auth.presentation.response.AuthResponse;
import com.boldfaced7.common.exception.exception.auth.ForbiddenException;
import com.boldfaced7.common.exception.exception.member.MemberNotFoundException;
import com.boldfaced7.member.infrastructure.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public CustomPage<MemberDto> getMembers(Pageable pageable) {
        Page<Member> members = memberRepository.findAll(pageable);
        CustomPage<Member> converted = CustomPage.convert(members);

        return converted.map(MemberDto::new);
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
        Member member = findMemberById(memberDto.getMemberId());

        encodePassword(memberDto);
        member.update(memberDto.toEntity());
    }

    public void softDeleteMember(MemberDto memberDto) {
        authorizeMember(memberDto.getMemberId());
        Member member = findMemberById(memberDto.getMemberId());
        member.deactivate();
    }

    public void hardDeleteMember(MemberDto memberDto) {
        authorizeMember(memberDto.getMemberId());
        Member member = findMemberById(memberDto.getMemberId());
        memberRepository.delete(member);
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
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
