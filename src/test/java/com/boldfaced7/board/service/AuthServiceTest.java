package com.boldfaced7.board.service;

import com.boldfaced7.board.domain.Member;
import com.boldfaced7.board.dto.AuthDto;
import com.boldfaced7.board.error.ErrorCode;
import com.boldfaced7.board.error.exception.auth.InvalidAuthValueException;
import com.boldfaced7.board.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static com.boldfaced7.board.TestUtil.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("AuthService 테스트")
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @InjectMocks AuthService authService;
    @Mock MemberRepository memberRepository;
    @Mock BCryptPasswordEncoder encoder;

    @DisplayName("[조회] ID/PW를 입력하면, 로그인 처리")
    @Test
    void givenEmailAndPassword_whenLogin_thenReturnsMember() {
        //Given
        Member member = createMember();
        AuthDto authDto = createRequestAuthDto();
        given(memberRepository.findByEmail(authDto.getEmail())).willReturn(Optional.of(member));
        given(encoder.encode(authDto.getPassword())).willReturn(PASSWORD);

        // When
        AuthDto returnedAuthDto = authService.login(authDto);

        // Then
        assertThat(returnedAuthDto)
                .hasFieldOrPropertyWithValue("memberId", authDto.getMemberId())
                .hasFieldOrPropertyWithValue("email", authDto.getEmail())
                .hasFieldOrPropertyWithValue("nickname", authDto.getNickname());

        then(memberRepository).should().findByEmail(authDto.getEmail());
        then(encoder).should().encode(authDto.getPassword());
    }

    @DisplayName("[조회] 잘못된 ID/PW를 입력하면, 로그인 처리 없이 예외를 던짐")
    @Test
    void givenWrongEmailAndPassword_whenLogin_thenThrowsException() {
        //Given
        AuthDto authDto = createRequestAuthDto();
        given(memberRepository.findByEmail(authDto.getEmail())).willReturn(Optional.empty());
        given(encoder.encode(authDto.getPassword())).willReturn(PASSWORD);

        // When
        Throwable t = catchThrowable(() -> authService.login(authDto));

        // Then
        assertThat(t)
                .isInstanceOf(InvalidAuthValueException.class)
                .hasMessage(ErrorCode.INVALID_AUTH_VALUE.getMessage());

        then(memberRepository).should().findByEmail(authDto.getEmail());
        then(encoder).should().encode(authDto.getPassword());
    }

    @DisplayName("[조회] 탈퇴한 ID/PW를 입력하면, 로그인 처리 없이 예외를 던짐")
    @Test
    void givenInactiveEmailAndPassword_whenLogin_thenThrowsException() {
        //Given
        AuthDto authDto = createRequestAuthDto();
        given(memberRepository.findByEmail(authDto.getEmail())).willReturn(Optional.empty());
        given(encoder.encode(authDto.getPassword())).willReturn(PASSWORD);

        // When
        Throwable t = catchThrowable(() -> authService.login(authDto));

        // Then
        assertThat(t)
                .isInstanceOf(InvalidAuthValueException.class)
                .hasMessage(ErrorCode.INVALID_AUTH_VALUE.getMessage());

        then(memberRepository).should().findByEmail(authDto.getEmail());
        then(encoder).should().encode(authDto.getPassword());
    }

    /*
    @DisplayName("[] ")
    @Test
    void given_when_then() {
        //Given


        // When
        loginService.

        // Then

    }
    */
}