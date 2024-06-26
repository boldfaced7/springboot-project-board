package com.boldfaced7.board.service;

import com.boldfaced7.board.auth.AuthInfoHolder;
import com.boldfaced7.board.domain.ArticleTicket;
import com.boldfaced7.board.domain.Member;
import com.boldfaced7.board.dto.ArticleTicketDto;
import com.boldfaced7.board.dto.CustomPage;
import com.boldfaced7.board.dto.MemberDto;
import com.boldfaced7.board.dto.response.AuthResponse;
import com.boldfaced7.board.error.exception.articlecomment.ArticleCommentNotFoundException;
import com.boldfaced7.board.error.exception.articleticket.ArticleTicketNotFoundException;
import com.boldfaced7.board.error.exception.articleticket.ArticleTicketSoldOutException;
import com.boldfaced7.board.error.exception.auth.ForbiddenException;
import com.boldfaced7.board.error.exception.member.MemberNotFoundException;
import com.boldfaced7.board.repository.ArticleTicketRepository;
import com.boldfaced7.board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleTicketService {
    private final MemberRepository memberRepository;
    private final ArticleTicketRepository articleTicketRepository;

    @Value("${ticket.total}")
    private int totalTicket;
    private final Map<LocalDate, Boolean> soldOutChecker = soldOutChecker();

    private Map<LocalDate, Boolean> soldOutChecker() {
        HashMap<LocalDate, Boolean> map = new HashMap<>();
        map.put(LocalDate.now(), false);
        return map;
    }

    @Transactional(readOnly = true)
    public ArticleTicketDto getIssuedTicket(ArticleTicketDto dto) {
        ArticleTicket articleTicket = articleTicketRepository.findById(dto.getArticleTicketId())
                .orElseThrow(ArticleTicketNotFoundException::new);

        return new ArticleTicketDto(articleTicket);
    }

    @Transactional(readOnly = true)
    public CustomPage<ArticleTicketDto> getIssuedTickets(Pageable pageable, LocalDate date) {
        LocalDateTime target = date.atStartOfDay();
        Page<ArticleTicket> articleTickets = articleTicketRepository
                .findAllByDate(pageable, target, target.plusDays(1));

        CustomPage<ArticleTicket> converted = CustomPage.convert(articleTickets);
        return converted.map(ArticleTicketDto::new);
    }

    @Transactional(readOnly = true)
    public CustomPage<ArticleTicketDto> getIssuedTickets(MemberDto memberDto) {
        authorizeMember(memberDto.getMemberId());
        Member member = findMemberById(memberDto.getMemberId());
        Page<ArticleTicket> articleTickets = articleTicketRepository
                .findAllByMember(memberDto.getPageable(), member);

        CustomPage<ArticleTicket> converted = CustomPage.convert(articleTickets);
        return converted.map(articleTicket -> new ArticleTicketDto(articleTicket, member));
    }

    @Transactional(readOnly = true)
    public int countIssuedTicket() {
        if (soldOutChecker.get(LocalDate.now())) {
            return totalTicket;
        }
        LocalDateTime today = LocalDate.now().atStartOfDay();
        return articleTicketRepository.countArticleTicketByDate(today, today.plusDays(1));
    }

    public long issueTicket() {
        ArticleTicket saved = createTicket();
        return confirmTicket(saved);
    }

    private ArticleTicket createTicket() {
        if (soldOutChecker.get(LocalDate.now())) {
            throw new ArticleTicketSoldOutException();
        }
        ArticleTicket articleTicket = ArticleTicket.builder()
                .member(findMemberByAuthInfo())
                .build();

        return articleTicketRepository.save(articleTicket);
    }

    private long confirmTicket(ArticleTicket saved) {
        Long criteria = articleTicketRepository
                .findCriteria(LocalDate.now().atStartOfDay()).orElse(0L);

        if (criteria + totalTicket < saved.getId()) {
            soldOutChecker.put(LocalDate.now(), true);
            articleTicketRepository.delete(saved);
            throw new ArticleTicketSoldOutException();
        }
        return saved.getId();
    }

    private Member findMemberByAuthInfo() {
        AuthResponse authInfo = AuthInfoHolder.getAuthInfo();
        return memberRepository.findById(authInfo.getMemberId())
                .orElseThrow(MemberNotFoundException::new);
    }
    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
    }
    private void authorizeMember(Long memberId) {
        AuthResponse authInfo = AuthInfoHolder.getAuthInfo();
        if (authInfo == null || !authInfo.getMemberId().equals(memberId)) {
            throw new ForbiddenException();
        }
    }
    @Scheduled(cron = "0 59 23 * * *")
    private void prepareTomorrowForFullChecker() {
        soldOutChecker.remove(LocalDate.now().minusDays(1));
        soldOutChecker.put(LocalDate.now().plusDays(1), false);
    }
}
