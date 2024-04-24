package com.boldfaced7.board.service;

import com.boldfaced7.board.Context;
import com.boldfaced7.board.auth.AuthInfoHolder;
import com.boldfaced7.board.dto.AuthDto;
import com.boldfaced7.board.error.exception.auth.InvalidAuthValueException;
import com.boldfaced7.board.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.stream.Stream;

import static com.boldfaced7.board.TestUtil.*;
import static com.boldfaced7.board.service.Facade.*;
import static com.boldfaced7.board.service.ServiceTestTemplate.doTest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@DisplayName("AuthService 테스트")
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @InjectMocks AuthService authService;
    @Mock MemberRepository mockMemberRepository;
    @Mock BCryptPasswordEncoder mockPasswordEncoder;
    Facade facade;

    @BeforeEach
    void setUp() {
        AuthInfoHolder.setAuthInfo(authResponse());
        facade = builder()
                .mockMemberRepository(mockMemberRepository)
                .mockPasswordEncoder(mockPasswordEncoder)
                .build();
    }

    @DisplayName("[조회] 로그인")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createLoginRequestTests")
    void loginTests(Context<Facade, AuthDto> context, AuthDto request) {
        doTest(() -> authService.login(request), context, facade);
    }
    static Stream<Arguments> createLoginRequestTests() {
        Context<Facade, AuthDto> valid = new Context<>("ID/PW를 입력하면, 로그인 처리");
        valid.mocks(memberRepository, m -> m.findByEmail(any()), Optional.of(member()));
        valid.mocks(passwordEncoder, p -> p.matches(any(), any()), true);
        valid.asserts(dto -> assertThat(dto).isNotNull());

        Context<Facade, AuthDto> wrongEmail = new Context<>("잘못된 ID를 입력하면, 로그인 처리 없이 예외를 던짐");
        wrongEmail.mocks(memberRepository, m -> m.findByEmail(any()), Optional.empty());
        wrongEmail.assertsThrowable(t -> assertThat(t).isInstanceOf(InvalidAuthValueException.class));

        Context<Facade, AuthDto> wrongPassword = new Context<>("잘못된 PW를 입력하면, 로그인 처리 없이 예외를 던짐");
        wrongPassword.mocks(memberRepository, m -> m.findByEmail(any()), Optional.of(member()));
        wrongPassword.mocks(passwordEncoder, p -> p.matches(any(), any()), false);
        wrongPassword.assertsThrowable(t -> assertThat(t).isInstanceOf(InvalidAuthValueException.class));

        Context<Facade, AuthDto> inactive = new Context<>("탈퇴한 ID/PW를 입력하면, 로그인 처리 없이 예외를 던짐");
        inactive.mocks(memberRepository, m -> m.findByEmail(any()), Optional.of(inactiveMember()));
        inactive.assertsThrowable(t -> assertThat(t).isInstanceOf(InvalidAuthValueException.class));

        return Stream.of(Arguments.of(valid, authDto()), Arguments.of(wrongEmail, authDto()),
                         Arguments.of(wrongPassword, authDto()), Arguments.of(inactive, authDto()));
    }
}