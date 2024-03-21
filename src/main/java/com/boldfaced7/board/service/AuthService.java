package com.boldfaced7.board.service;

import com.boldfaced7.board.domain.Member;
import com.boldfaced7.board.dto.AuthDto;
import com.boldfaced7.board.error.exception.auth.InvalidAuthValueException;
import com.boldfaced7.board.repository.MemberRepository;
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
