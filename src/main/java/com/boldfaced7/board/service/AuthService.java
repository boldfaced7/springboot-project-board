package com.boldfaced7.board.service;

import com.boldfaced7.board.domain.Member;
import com.boldfaced7.board.dto.AuthDto;
import com.boldfaced7.board.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder;

    public static final String NOT_FOUND = "ID/PW를 확인해주세요.";

    public AuthDto login(AuthDto authDto) {
        String encodedPassword = encoder.encode(authDto.getPassword());
        authDto.setPassword(encodedPassword);

        return memberRepository.findByEmail(authDto.getEmail())
                .filter(Member::isActive)
                .filter(m -> m.isCorrectPassword(authDto.getPassword()))
                .map(AuthDto::new)
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND));
    }
}
