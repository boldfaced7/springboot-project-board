package com.boldfaced7.board.service;

import com.boldfaced7.board.Assertion;
import com.boldfaced7.board.Context;
import com.boldfaced7.board.auth.AuthInfoHolder;
import com.boldfaced7.board.domain.Member;
import com.boldfaced7.board.dto.MemberDto;
import com.boldfaced7.board.error.exception.auth.ForbiddenException;
import com.boldfaced7.board.error.exception.member.MemberNotFoundException;
import com.boldfaced7.board.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
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

import static com.boldfaced7.board.TestUtil.*;
import static com.boldfaced7.board.RepoMethod.*;

@DisplayName("MemberService 테스트")
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks private MemberService memberService;
    @Mock private MemberRepository memberRepository;
    @Mock private BCryptPasswordEncoder encoder;
    ServiceTestTemplate testTemplate;

    static final String ACTIVE = "active";
    static final String INACTIVE = "inactive";
    static final String FOUND = "found";

    @BeforeEach
    void setUp() {
        AuthInfoHolder.setAuthInfo(createAuthResponse());
        DependencyHolder dependencyHolder = DependencyHolder.builder().encoder(encoder)
                .memberRepository(memberRepository).build();

        testTemplate = new ServiceTestTemplate(dependencyHolder);
    }

    @AfterEach
    void clear() {
        AuthInfoHolder.releaseAuthInfo();
    }

    @DisplayName("email 중복 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createGetEmailAndNicknameExistenceRequestTests")
    void getEmailExistenceTest(String ignoredMessage, List<Context<DependencyHolder>> contexts, String request, List<Assertion<Boolean>> assertions) {
        testTemplate.performRequest(contexts, memberService::isOccupiedEmail, request, assertions);
    }
    static Stream<Arguments> createGetEmailAndNicknameExistenceRequestTests() {
        Map<String, List<Context<DependencyHolder>>> contexts = Map.of(
                FOUND, List.of(new Context<>(existsMemberByEmail, EMAIL, true, memberRepoFunc)),
                NOT_FOUND, List.of(new Context<>(existsMemberByEmail, EMAIL, false, memberRepoFunc))
        );
        Map<String, List<Assertion<Boolean>>> assertions = Map.of(
                FOUND, List.of(new Assertion<>(true)),
                NOT_FOUND, List.of(new Assertion<>(false))
        );
        return Stream.of(
                Arguments.of("email 중복 O", contexts.get(FOUND), EMAIL, assertions.get(FOUND)),
                Arguments.of("email 중복 X", contexts.get(NOT_FOUND), EMAIL, assertions.get(NOT_FOUND))
        );
    }

    @DisplayName("nickname 중복 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createGetNicknameExistenceRequestTests")
    void getNicknameExistenceTest(String ignoredMessage, List<Context<DependencyHolder>> contexts, String request, List<Assertion<Boolean>> assertions) {
        testTemplate.performRequest(contexts, memberService::isOccupiedNickname, request, assertions);
    }
    static Stream<Arguments> createGetNicknameExistenceRequestTests() {
        Map<String, List<Context<DependencyHolder>>> contexts = Map.of(
                FOUND, List.of(new Context<>(existsMemberByNickname, NICKNAME, true, memberRepoFunc)),
                NOT_FOUND, List.of(new Context<>(existsMemberByNickname, NICKNAME, false, memberRepoFunc))
        );
        Map<String, List<Assertion<Boolean>>> assertions = Map.of(
                FOUND, List.of(new Assertion<>(true)),
                NOT_FOUND, List.of(new Assertion<>(false))
        );
        return Stream.of(
                Arguments.of("nickname 중복 O", contexts.get(FOUND), NICKNAME, assertions.get(FOUND)),
                Arguments.of("nickname 중복 X", contexts.get(NOT_FOUND), NICKNAME, assertions.get(NOT_FOUND))
        );    }

    @DisplayName("회원 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createGetMemberRequestTests")
    void getMemberTest(String ignoredMessage, List<Context<DependencyHolder>> contexts, Long request, List<Assertion<MemberDto>> assertions) {
        testTemplate.performRequest(contexts, memberService::getMember, request, assertions);
    }
    static Stream<Arguments> createGetMemberRequestTests() {
        Member member = createMember();

        Map<String, List<Context<DependencyHolder>>> contexts = Map.of(
                VALID, List.of(new Context<>(findMemberById, MEMBER_ID, Optional.of(member), memberRepoFunc)),
                NOT_FOUND, List.of(new Context<>(findMemberById, MEMBER_ID, Optional.empty(), memberRepoFunc))
        );
        Map<String, List<Assertion<MemberDto>>> assertions = Map.of(
                VALID, List.of(new Assertion<>(new MemberDto(member))),
                NOT_FOUND, List.of(new Assertion<>(MemberNotFoundException.class))
        );
        return Stream.of(
                Arguments.of("id를 입력하면, 회원 정보를 반환", contexts.get(VALID), MEMBER_ID, assertions.get(VALID)),
                Arguments.of("잘못된 id를 입력하면, 반환 없이 예외를 던짐", contexts.get(NOT_FOUND), MEMBER_ID, assertions.get(NOT_FOUND))
        );
    }

    @DisplayName("회원 목록 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createGetMembersRequestTests")
    void getMembersTest(String ignoredMessage, List<Context<DependencyHolder>> contexts, List<Assertion<List<MemberDto>>> assertions) {
        testTemplate.performRequest(contexts, memberService::getMembers, assertions);
    }
    static Stream<Arguments> createGetMembersRequestTests() {
        Map<String, List<Context<DependencyHolder>>> contexts = Map.of(
                VALID, List.of(new Context<>(findMembers, List.of(createMember()), memberRepoFunc))
        );
        Map<String, List<Assertion<List<MemberDto>>>> assertions = Map.of(VALID, List.of(new Assertion<>()));
        return Stream.of(Arguments.of("회원 목록을 반환", contexts.get(VALID), assertions.get(VALID)));
    }

    @DisplayName("활성/비활성 회원 목록 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createGetctiveOrInactiveMembersRequestTests")
    void getActiveOrInactiveMembersTest(String ignoredMessage, List<Context<DependencyHolder>> contexts, boolean request, List<Assertion<List<MemberDto>>> assertions) {
        testTemplate.performRequest(contexts, memberService::getMembers, request, assertions);
    }
    static Stream<Arguments> createGetctiveOrInactiveMembersRequestTests() {
        Member activeMember = createMember();
        Member inactiveMember = createMember();
        inactiveMember.deactivate();

        Map<String, List<Context<DependencyHolder>>> contexts = Map.of(
                ACTIVE, List.of(new Context<>(findMembersByIsActive, true, List.of(activeMember), memberRepoFunc)),
                INACTIVE, List.of(new Context<>(findMembersByIsActive, false, List.of(inactiveMember), memberRepoFunc))
        );
        Map<String, List<Assertion<List<MemberDto>>>> assertions = Map.of(
                ACTIVE, List.of(new Assertion<>()),
                INACTIVE, List.of(new Assertion<>())
        );
        return Stream.of(
                Arguments.of("활성 회원 목록을 반환", contexts.get(ACTIVE), true, assertions.get(ACTIVE)),
                Arguments.of("비활성 회원 목록을 반환", contexts.get(INACTIVE), false, assertions.get(INACTIVE))
        );
    }

    @DisplayName("회원 저장")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createPostMemberRequestTests")
    void PostMemberTests(String ignoredMessage, List<Context<DependencyHolder>> contexts, MemberDto request, List<Assertion<Long>> assertions) {
        testTemplate.performRequest(contexts, memberService::saveMember, request, assertions);
    }
    static Stream<Arguments> createPostMemberRequestTests() {
        MemberDto requestDto = MemberDto.builder().email(EMAIL).password(PASSWORD).nickname(NICKNAME).build();

        Map<String, List<Context<DependencyHolder>>> contexts = Map.of(
                VALID, List.of(
                        new Context<>(encode, PASSWORD, PASSWORD, encoderFunc),
                        new Context<>(saveMember, requestDto.toEntity(), createMember(), memberRepoFunc)
                )
        );
        Map<String, List<Assertion<Long>>> assertions = Map.of(
                VALID, List.of(new Assertion<>(MEMBER_ID))
        );
        return Stream.of(
                Arguments.of("회원 작성 정보를 입력하면, 회원을 저장", contexts.get(VALID), requestDto, assertions.get(VALID))
        );
    }

    @DisplayName("회원 수정")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("UpdateMemberRequestTests")
    void UpdateMemberTests(String ignoredMessage, List<Context<DependencyHolder>> contexts, MemberDto request, List<Assertion<Member>> assertions, Member target) {
        testTemplate.performRequest(contexts, memberService::updateMember, request, assertions, target);
    }
    static Stream<Arguments> UpdateMemberRequestTests() {
        Member member = createMember();

        MemberDto requestDto = MemberDto.builder().memberId(MEMBER_ID).password("New").nickname("New").build();
        MemberDto forbiddenRequestDto = MemberDto.builder().memberId(MEMBER_ID+1).password("New").nickname("New").build();

        Map<String, List<Context<DependencyHolder>>> contexts = Map.of(
                VALID, List.of(
                        new Context<>(findMemberById, MEMBER_ID, Optional.of(member), memberRepoFunc),
                        new Context<>(encode, "New", "New", encoderFunc)
                ),
                NOT_FOUND, List.of(new Context<>(findMemberById, MEMBER_ID, Optional.empty(), memberRepoFunc)),
                FORBIDDEN, List.of()
        );
        Map<String, List<Assertion<Member>>> assertions = Map.of(
                VALID, List.of(new Assertion<>(Map.of("password", requestDto.getPassword(),"nickname", requestDto.getNickname()))),
                NOT_FOUND, List.of(new Assertion<>(MemberNotFoundException.class)),
                FORBIDDEN, List.of(new Assertion<>(ForbiddenException.class))
        );
        return Stream.of(
                Arguments.of("id와 회원 수정 정보를 입력하면, 회원을 수정", contexts.get(VALID), requestDto, assertions.get(VALID), member),
                Arguments.of("잘못된 id를 입력하면, 수정 없이 예외를 던짐", contexts.get(NOT_FOUND), requestDto, assertions.get(NOT_FOUND), null),
                Arguments.of("잘못된 회원이 접근하면, 수정 없이 예외를 던짐", contexts.get(FORBIDDEN), forbiddenRequestDto, assertions.get(FORBIDDEN), null)
        );
    }

    @DisplayName("회원 비활성화")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createInactiveMemberRequestTests")
    void deactivateMemberTest(String ignoredMessage, List<Context<DependencyHolder>> contexts, MemberDto request, List<Assertion<Member>> assertions, Member target) {
        testTemplate.performRequest(contexts, memberService::softDeleteMember, request, assertions, target);
    }
    static Stream<Arguments> createInactiveMemberRequestTests() {
        Member member = createMember();

        MemberDto requestDto = MemberDto.builder().memberId(MEMBER_ID).build();
        MemberDto forbiddenRequestDto = MemberDto.builder().memberId(MEMBER_ID+1).build();

        Map<String, List<Context<DependencyHolder>>> contexts = Map.of(
                VALID, List.of(new Context<>(findMemberById, MEMBER_ID, Optional.of(member), memberRepoFunc)),
                NOT_FOUND, List.of(new Context<>(findMemberById, MEMBER_ID, Optional.empty(), memberRepoFunc)),
                FORBIDDEN, List.of()
        );
        Map<String, List<Assertion<Member>>> assertions = Map.of(
                VALID, List.of(new Assertion<>(Map.of("isActive", false))),
                NOT_FOUND, List.of(new Assertion<>(MemberNotFoundException.class)),
                FORBIDDEN, List.of(new Assertion<>(ForbiddenException.class))
        );
        return Stream.of(
                Arguments.of("id를 입력하면, 회원을 비활성화", contexts.get(VALID), requestDto, assertions.get(VALID), member),
                Arguments.of("잘못된 id를 입력하면, 비활성화 없이 예외를 던짐", contexts.get(NOT_FOUND), requestDto, assertions.get(NOT_FOUND), null),
                Arguments.of("잘못된 회원이 접근하면, 비활성화 없이 예외를 던짐", contexts.get(FORBIDDEN), forbiddenRequestDto, assertions.get(FORBIDDEN), null)
        );
    }

    @DisplayName("회원 삭제")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createDeleteMemberRequestTests")
    void deleteMemberTest(String ignoredMessage, List<Context<DependencyHolder>> contexts, MemberDto request, List<Assertion<Member>> assertions) {
        testTemplate.performRequest(contexts, memberService::hardDeleteMember, request, assertions, null);
    }
    static Stream<Arguments> createDeleteMemberRequestTests() {
        Member member = createMember();

        MemberDto requestDto = MemberDto.builder().memberId(MEMBER_ID).build();
        MemberDto forbiddenRequestDto = MemberDto.builder().memberId(MEMBER_ID+1).build();

        Map<String, List<Context<DependencyHolder>>> contexts = Map.of(
                VALID, List.of(new Context<>(findMemberById, MEMBER_ID, Optional.of(member), memberRepoFunc)),
                NOT_FOUND, List.of(new Context<>(findMemberById, MEMBER_ID, Optional.empty(), memberRepoFunc)),
                FORBIDDEN, List.of()
        );
        Map<String, List<Assertion<Member>>> assertions = Map.of(
                VALID, List.of(),
                NOT_FOUND, List.of(new Assertion<>(MemberNotFoundException.class)),
                FORBIDDEN, List.of(new Assertion<>(ForbiddenException.class))
        );
        return Stream.of(
                Arguments.of("id를 입력하면, 회원을 삭제", contexts.get(VALID), requestDto, assertions.get(VALID)),
                Arguments.of("잘못된 id를 입력하면, 삭제 없이 예외를 던짐", contexts.get(NOT_FOUND), requestDto, assertions.get(NOT_FOUND)),
                Arguments.of("잘못된 회원이 접근하면, 삭제 없이 예외를 던짐", contexts.get(FORBIDDEN), forbiddenRequestDto, assertions.get(FORBIDDEN))
        );
    }
}