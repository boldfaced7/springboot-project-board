package com.boldfaced7.board.ticket.application;

import com.boldfaced7.noboilerplate.Context;
import com.boldfaced7.board.common.auth.AuthInfoHolder;
import com.boldfaced7.noboilerplate.Facade;
import com.boldfaced7.board.common.CustomPage;
import com.boldfaced7.board.member.application.MemberDto;
import com.boldfaced7.board.common.exception.exception.articleticket.ArticleTicketNotFoundException;
import com.boldfaced7.board.common.exception.exception.articleticket.ArticleTicketSoldOutException;
import com.boldfaced7.board.common.exception.exception.auth.ForbiddenException;
import com.boldfaced7.board.ticket.infrastructure.ArticleTicketRepository;
import com.boldfaced7.board.member.infrastructure.MemberRepository;
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

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

import static com.boldfaced7.noboilerplate.TestUtil.*;
import static com.boldfaced7.noboilerplate.Facade.articleTicketRepository;
import static com.boldfaced7.noboilerplate.Facade.memberRepository;
import static com.boldfaced7.noboilerplate.ServiceTestTemplate.doTest;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@DisplayName("ArticleTicketService 테스트")
@ExtendWith(MockitoExtension.class)
class ArticleTicketServiceTest {
    @InjectMocks
    ArticleTicketService articleTicketService;
    @Mock ArticleTicketRepository mockArticleTicketRepository;
    @Mock MemberRepository mockMemberRepository;
    Facade facade;

    @BeforeEach
    void setUp() {
        AuthInfoHolder.setAuthInfo(authResponse());
        facade = Facade.builder()
                .mockMemberRepository(mockMemberRepository)
                .mockArticleTicketRepository(mockArticleTicketRepository)
                .build();
    }
    @AfterEach
    void clear() {
        AuthInfoHolder.releaseAuthInfo();
    }

    @DisplayName("티켓 단건 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    void getIssuedTicketTest(Context<Facade, ArticleTicketDto> context, Long request) {
        doTest(() -> articleTicketService.getIssuedTicket(new ArticleTicketDto(request)), context, facade);
    }
    static Stream<Arguments> getIssuedTicketTest() {
        Context<Facade, ArticleTicketDto> valid = new Context<>("id를 입력하면, 티켓을 반환");
        valid.mocks(articleTicketRepository, a -> a.findById(anyLong()), Optional.of(articleTicket(1L)));
        valid.asserts(dto -> assertThat(dto.getArticleTicketId()).isEqualTo(1L));

        Context<Facade, ?> notFound = new Context<>("잘못된 id를 입력하면, 반환 없이 예외를 던짐");
        notFound.mocks(articleTicketRepository, a -> a.findById(anyLong()), Optional.empty());
        notFound.assertsThrowable(t -> assertThat(t).isInstanceOf(ArticleTicketNotFoundException.class));

        return Stream.of(Arguments.of(valid, 1L), Arguments.of(notFound, 2L));
    }

    @DisplayName("발급 티켓 목록 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    void getIssuedTicketsTest(Context<Facade, ArticleTicketDto> context, Pageable pageable, LocalDate date) {
        doTest(() -> articleTicketService.getIssuedTickets(pageable, date), context, facade);
    }
    static Stream<Arguments> getIssuedTicketsTest() {
        Context<Facade, CustomPage<?>> valid = new Context<>("티켓 리스트를 반환");
        valid.mocks(articleTicketRepository, a -> a.findAllByDate(any(), any(), any()), articleTickets());
        valid.asserts(dtos -> assertThat(dtos.getContent()).isNotEmpty());

        return Stream.of(Arguments.of(valid, pageable(), LocalDate.now()));
    }

    @DisplayName("회원의 티켓 목록 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    void testGetIssuedTicketsOfMemberTest(Context<Facade, ArticleTicketDto> context, MemberDto request) {
        doTest(() -> articleTicketService.getIssuedTickets(request), context, facade);
    }
    static Stream<Arguments> testGetIssuedTicketsOfMemberTest() {
        Context<Facade,CustomPage<?>> valid = new Context<>("회원 id를 입력하면, 티켓 리스트를 반환");
        valid.mocks(memberRepository, m -> m.findById(anyLong()), Optional.of(member()));
        valid.mocks(articleTicketRepository, a -> a.findAllByMember(any(), any()), articleTickets());
        valid.asserts(dtos -> assertThat(dtos.getContent()).isNotEmpty());

        Context<Facade, ?> notFound = new Context<>("잘못된 회원 id를 입력하면, 반환 없이 예외를 던짐");
        valid.mocks(memberRepository, m -> m.findById(anyLong()), Optional.of(member()));
        notFound.assertsThrowable(t -> assertThat(t).isInstanceOf(ForbiddenException.class));

        return Stream.of(Arguments.of(valid, memberDto(1L)), Arguments.of(notFound, memberDto(2L)));
    }

    @DisplayName("발급 티켓 개수 조회")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    void countIssuedTicketTest(Context<Facade, Integer> context) {
        doTest(() -> articleTicketService.countIssuedTicket(), context, facade);
    }
    static Stream<Arguments> countIssuedTicketTest() {
        Context<Facade, Integer> valid = new Context<>("발급된 티켓 개수를 반환");
        valid.mocks(articleTicketRepository, a -> a.countArticleTicketByDate(any(), any()), 1);
        valid.asserts(i -> assertThat(i).isEqualTo(1));

        return Stream.of(Arguments.of(valid));
    }

    @DisplayName("티켓 발급")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    void issueTicketTest(Context<Facade, Long> context) {
        doTest(() -> articleTicketService.issueTicket(), context, facade);
    }
    static Stream<Arguments> issueTicketTest() {
        Context<Facade, Long> success = new Context<>("선착순에 들어 티켓 발급에 성공");
        success.mocks(memberRepository, m -> m.findById(anyLong()), Optional.of(member()));
        success.mocks(articleTicketRepository, a -> a.save(any()), articleTicket(1L));
        success.mocks(articleTicketRepository, a -> a.findCriteria(any()), Optional.of(0L));
        success.asserts(l -> assertThat(l).isEqualTo(1L));

        Context<Facade, Long> fail = new Context<>("선착순에 들지 못해 티켓 발급에 실패");
        fail.mocks(memberRepository, m -> m.findById(anyLong()), Optional.of(member()));
        fail.mocks(articleTicketRepository, a -> a.save(any()), articleTicket(100L));
        fail.mocks(articleTicketRepository, a -> a.findCriteria(any()), Optional.of(0L));
        fail.mocks(articleTicketRepository, a -> a.delete(any()));
        fail.assertsThrowable(t -> assertThat(t).isInstanceOf(ArticleTicketSoldOutException.class));

        return Stream.of(Arguments.of(success), Arguments.of(fail));
    }
}