package com.boldfaced7.board.service;

import com.boldfaced7.board.Assertion;
import com.boldfaced7.board.Context;
import com.boldfaced7.board.auth.AuthInfoHolder;
import com.boldfaced7.board.domain.Member;
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

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static com.boldfaced7.board.RepoMethod.*;
import static com.boldfaced7.board.TestUtil.*;

@DisplayName("AuthService 테스트")
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @InjectMocks AuthService authService;
    @Mock MemberRepository memberRepository;
    @Mock BCryptPasswordEncoder encoder;
    ServiceTestTemplate testTemplate;

    final static String WRONG_EMAIL = "wrongEmail";
    final static String WRONG_PASSWORD = "wrongPassword";
    final static String INACTIVE = "inactive";

    @BeforeEach
    void setUp() {
        AuthInfoHolder.setAuthInfo(createAuthResponse());
        DependencyHolder dependencyHolder = DependencyHolder.builder().encoder(encoder)
                .memberRepository(memberRepository).build();

        testTemplate = new ServiceTestTemplate(dependencyHolder);
    }
    @DisplayName("[조회] 로그인")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createLoginRequestTests")
    void LoginTest(String ignoredMessage, List<Context<DependencyHolder>> contexts,AuthDto request, List<Assertion<AuthDto>> assertions) {
        testTemplate.performRequest(contexts, authService::login, request, assertions);
    }
    static Stream<Arguments> createLoginRequestTests() {
        AuthDto requestDto = createRequestAuthDto();

        Member pwFailMember =createMember();
        pwFailMember.updatePassword("New");
        Member inactiveMember = createMember();
        inactiveMember.deactivate();

        Context<DependencyHolder> success = new Context<>(matches, PASSWORD, PASSWORD, true, encoderFunc);
        Context<DependencyHolder> fail = new Context<>(matches, PASSWORD, "New", false, encoderFunc);

        Map<String, List<Context<DependencyHolder>>> contexts = Map.of(
                VALID, List.of(new Context<>(findMemberByEmail, requestDto.getEmail(), Optional.of(createMember()), memberRepoFunc), success),
                WRONG_EMAIL, List.of(new Context<>(findMemberByEmail, requestDto.getEmail(), Optional.empty(), memberRepoFunc)),
                WRONG_PASSWORD, List.of(new Context<>(findMemberByEmail, requestDto.getEmail(), Optional.of(pwFailMember), memberRepoFunc), fail),
                INACTIVE, List.of(new Context<>(findMemberByEmail, requestDto.getEmail(), Optional.of(inactiveMember), memberRepoFunc))
        );
        Map<String, List<Assertion<AuthDto>>> assertions = Map.of(
                VALID, List.of(new Assertion<>()),
                WRONG_EMAIL, List.of(new Assertion<>(InvalidAuthValueException.class)),
                WRONG_PASSWORD, List.of(new Assertion<>(InvalidAuthValueException.class)),
                INACTIVE, List.of(new Assertion<>(InvalidAuthValueException.class))
        );
        return Stream.of(
                Arguments.of("ID/PW를 입력하면, 로그인 처리", contexts.get(VALID), requestDto, assertions.get(VALID)),
                Arguments.of("잘못된 ID를 입력하면, 로그인 처리 없이 예외를 던짐", contexts.get(WRONG_EMAIL), requestDto, assertions.get(WRONG_EMAIL)),
                Arguments.of("잘못된 PW를 입력하면, 로그인 처리 없이 예외를 던짐", contexts.get(WRONG_PASSWORD), requestDto, assertions.get(WRONG_PASSWORD)),
                Arguments.of("탈퇴한 ID/PW를 입력하면, 로그인 처리 없이 예외를 던짐", contexts.get(INACTIVE), requestDto, assertions.get(INACTIVE))
        );
    }
}