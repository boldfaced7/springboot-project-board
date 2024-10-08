package com.boldfaced7.board.common.auth.application;

import com.boldfaced7.board.common.auth.application.AuthDto;
import com.boldfaced7.board.member.domain.Member;
import com.boldfaced7.board.common.exception.exception.auth.InvalidAuthValueException;
import com.boldfaced7.board.member.infrastructure.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;

    public AuthDto login(AuthDto authDto) {
        return memberRepository.findByEmail(authDto.getEmail())
                .filter(Member::isActive)
                .filter(m -> encoder.matches(authDto.getPassword(), m.getPassword()))
                .map(AuthDto::new)
                .orElseThrow(InvalidAuthValueException::new);
    }
}
