package com.boldfaced7.board.member.application;

import com.boldfaced7.noboilerplate.Context;
import com.boldfaced7.board.common.auth.AuthInfoHolder;
import com.boldfaced7.board.member.domain.Member;
import com.boldfaced7.board.common.CustomPage;
import com.boldfaced7.board.common.exception.exception.auth.ForbiddenException;
import com.boldfaced7.board.common.exception.exception.member.MemberNotFoundException;
import com.boldfaced7.board.member.infrastructure.MemberRepository;
import com.boldfaced7.noboilerplate.Facade;
import org.assertj.core.api.Assertions;
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
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.stream.Stream;

import static com.boldfaced7.noboilerplate.TestUtil.*;
import static com.boldfaced7.noboilerplate.Facade.*;
import static com.boldfaced7.noboilerplate.ServiceTestTemplate.doTest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@DisplayName("MemberService 테스트")
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks private MemberService memberService;
    @Mock private MemberRepository mockMemberRepository;
    @Mock private PasswordEncoder mockPasswordEncoder;
    Facade facade;

    @BeforeEach
    void setUp() {
        AuthInfoHolder.setAuthInfo(authResponse());
        facade = builder()
                .mockMemberRepository(mockMemberRepository)
                .mockPasswordEncoder(mockPasswordEncoder)
                .build();
    }

    @AfterEach
    void clear() {
        AuthInfoHolder.releaseAuthInfo();
    }

    @DisplayName("email 중복 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    void getEmailExistenceTest(Context<Facade, Boolean> context, String request) {
        doTest(() -> memberService.isOccupiedEmail(request), context, facade);
    }
    static Stream<Arguments> getEmailExistenceTest() {
        Context<Facade, Boolean> found = new Context<>("E-mail 중복 O");
        found.mocks(memberRepository, m -> m.existsByEmail(any()), true);
        found.asserts(b -> Assertions.assertThat(b).isEqualTo(true));

        Context<Facade, Boolean> notFound = new Context<>("E-mail 중복 X");
        notFound.mocks(memberRepository, m -> m.existsByEmail(any()), false);
        notFound.asserts(b -> Assertions.assertThat(b).isEqualTo(false));

        return Stream.of(Arguments.of(found, EMAIL), Arguments.of(notFound, EMAIL));
    }

    @DisplayName("Nickname 중복 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    void getNicknameExistenceTest(Context<Facade, Boolean> context, String request) {
        doTest(() -> memberService.isOccupiedNickname(request), context, facade);
    }
    static Stream<Arguments> getNicknameExistenceTest() {
        Context<Facade, Boolean> found = new Context<>("Nickname 중복 O");
        found.mocks(memberRepository, m -> m.existsByNickname(any()), true);
        found.asserts(b -> Assertions.assertThat(b).isEqualTo(true));

        Context<Facade, Boolean> notFound = new Context<>("Nickname 중복 X");
        notFound.mocks(memberRepository, m -> m.existsByNickname(any()), false);
        notFound.asserts(b -> Assertions.assertThat(b).isEqualTo(false));

        return Stream.of(Arguments.of(found, NICKNAME), Arguments.of(notFound, NICKNAME));
    }

    @DisplayName("회원 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    void getMemberTest(Context<Facade, MemberDto> context, Long request) {
        doTest(() -> memberService.getMember(request), context, facade);
    }
    static Stream<Arguments> getMemberTest() {
        Context<Facade,MemberDto> valid = new Context<>("회원 id를 입력하면, 회원 정보를 반환");
        valid.mocks(memberRepository, m -> m.findById(anyLong()), Optional.of(member()));
        valid.asserts(dto -> assertThat(dto.getMemberId()).isNotNull());

        Context<Facade, ?> notFound = new Context<>("잘못된 회원 id를 입력하면, 반환 없이 예외를 던짐");
        valid.mocks(memberRepository, m -> m.findById(anyLong()), Optional.empty());
        notFound.assertsThrowable(t -> assertThat(t).isInstanceOf(MemberNotFoundException.class));

        return Stream.of(Arguments.of(valid, 1L), Arguments.of(notFound, 2L));
    }

    @DisplayName("회원 목록 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    void getMembersTest(Context<Facade,CustomPage<?>> context, Pageable request) {
        doTest(() -> memberService.getMembers(request), context, facade);
    }
    static Stream<Arguments> getMembersTest() {
        Context<Facade,CustomPage<?>> valid = new Context<>("회원 목록을 반환");
        valid.mocks(memberRepository, m -> m.findAll(any(Pageable.class)), members());
        valid.asserts(dtos -> assertThat(dtos.getContent()).isNotEmpty());

        return Stream.of(Arguments.of(valid, pageable()));
    }

    @DisplayName("회원 저장")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    void postMemberTests(Context<Facade, Long> context, MemberDto request) {
        doTest(() -> memberService.saveMember(request), context, facade);
    }
    static Stream<Arguments> postMemberTests() {
        Context<Facade, Long> valid = new Context<>("회원 정보를 입력하면, 회원을 저장");
        valid.mocks(passwordEncoder, p -> p.encode(any()), PASSWORD);
        valid.mocks(memberRepository, m -> m.save(any()), member());
        valid.asserts(id -> assertThat(id).isEqualTo(1L));

        return Stream.of(Arguments.of(valid, memberDto()));
    }

    @DisplayName("회원 수정")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    void updateMemberTests(Context<Facade, ?> context, MemberDto request) {
        doTest(() -> memberService.updateMember(request), context, facade);
    }
    static Stream<Arguments> updateMemberTests() {
        Member target = member();

        Context<Facade, ?> valid = new Context<>("id와 회원 수정 정보를 입력하면, 회원 정보를 수정");
        valid.mocks(memberRepository, m -> m.findById(anyLong()), Optional.of(target));
        valid.mocks(passwordEncoder, p -> p.encode(any()), NEW);
        valid.asserts(() -> assertThat(target).hasFieldOrPropertyWithValue("password", NEW)
                                              .hasFieldOrPropertyWithValue("nickname", NEW));

        Context<Facade, ?> notFound = new Context<>("잘못된 id를 입력하면, 수정 없이 예외를 던짐");
        notFound.mocks(memberRepository, m -> m.findById(anyLong()), Optional.empty());
        notFound.assertsThrowable(t -> assertThat(t).isInstanceOf(MemberNotFoundException.class));

        Context<Facade, ?> forbidden = new Context<>("잘못된 회원이 접근하면, 수정 없이 예외를 던짐");
        forbidden.assertsThrowable(t -> assertThat(t).isInstanceOf(ForbiddenException.class));
        MemberDto forbiddenRequest = MemberDto.builder().memberId(2L).password(NEW).nickname(NEW).build();

        return Stream.of(Arguments.of(valid, memberDto(NEW, NEW)), Arguments.of(notFound, memberDto(NEW, NEW)),
                Arguments.of(forbidden, forbiddenRequest));
    }

    @DisplayName("회원 비활성화")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    void deactivateMemberTests(Context<Facade, ?> context, MemberDto request) {
        doTest(() -> memberService.softDeleteMember(request), context, facade);
    }
    static Stream<Arguments> deactivateMemberTests() {
        Member target = member();

        Context<Facade, ?> valid = new Context<>("id를 입력하면, 회원을 비활성화");
        valid.mocks(memberRepository, a -> a.findById(anyLong()), Optional.of(target));
        valid.asserts(() -> assertThat(target).hasFieldOrPropertyWithValue("isActive", false));

        Context<Facade, ?> notFound = new Context<>("잘못된 id를 입력하면, 비활성화 없이 예외를 던짐");
        notFound.mocks(memberRepository, a -> a.findById(anyLong()), Optional.empty());
        notFound.assertsThrowable(t -> assertThat(t).isInstanceOf(MemberNotFoundException.class));

        Context<Facade, ?> forbidden = new Context<>("잘못된 회원이 접근하면, 비활성화 없이 예외를 던짐");
        forbidden.assertsThrowable(t -> assertThat(t).isInstanceOf(ForbiddenException.class));
        MemberDto forbiddenRequest = MemberDto.builder().memberId(2L).build();

        return Stream.of(Arguments.of(valid, memberDto()), Arguments.of(notFound, memberDto()),
                Arguments.of(forbidden, forbiddenRequest));
    }

    @DisplayName("게시글 삭제")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    void deleteMemberTests(Context<Facade, ?> context, MemberDto request) {
        doTest(() -> memberService.hardDeleteMember(request), context, facade);
    }
    static Stream<Arguments> deleteMemberTests() {
        Context<Facade, ?> valid = new Context<>("id를 입력하면, 회원을 삭제");
        valid.mocks(memberRepository, a -> a.findById(anyLong()), Optional.of(member()));
        valid.mocks(memberRepository, a -> a.delete(any()));

        Context<Facade, ?> notFound = new Context<>("잘못된 id를 입력하면, 삭제 없이 예외를 던짐");
        notFound.mocks(memberRepository, a -> a.findById(anyLong()), Optional.empty());
        notFound.assertsThrowable(t -> assertThat(t).isInstanceOf(MemberNotFoundException.class));

        Context<Facade, ?> forbidden = new Context<>("잘못된 회원이 접근하면, 삭제 없이 예외를 던짐");
        forbidden.assertsThrowable(t -> assertThat(t).isInstanceOf(ForbiddenException.class));
        MemberDto forbiddenRequest = MemberDto.builder().memberId(2L).build();

        return Stream.of(Arguments.of(valid, memberDto()), Arguments.of(notFound, memberDto()),
                Arguments.of(forbidden, forbiddenRequest));
    }
}