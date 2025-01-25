package com.boldfaced7.application.service;

import com.boldfaced7.Query;
import com.boldfaced7.application.port.in.ListArticleTicketsByMemberCommand;
import com.boldfaced7.application.port.in.ListArticleTicketsByMemberIdQuery;
import com.boldfaced7.application.port.in.ListResolvedArticleTicketsHelper;
import com.boldfaced7.application.port.out.GetMemberInfoPort;
import com.boldfaced7.application.port.out.GetMemberInfoRequest;
import com.boldfaced7.application.port.out.ListArticleTicketsByMemberPort;
import com.boldfaced7.domain.ArticleTicket;
import com.boldfaced7.domain.ResolvedArticleTicket;
import com.boldfaced7.exception.member.MemberNotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Query
@RequiredArgsConstructor
public class ListArticleTicketsByMemberService implements ListArticleTicketsByMemberIdQuery {

    private static final int PAGE_SIZE = 20;

    private final GetMemberInfoPort getMemberInfoPort;
    private final ListArticleTicketsByMemberPort listArticleTicketsByMemberPort;
    private final ListResolvedArticleTicketsHelper listResolvedArticleTicketsHelper;

    @Override
    public List<ResolvedArticleTicket> listMemberArticleTickets(ListArticleTicketsByMemberCommand command) {
        ensureMemberExists(command.memberId());
        List<ArticleTicket> articleTickets
                = getTickets(command.memberId(), command.pageNumber());

        return listResolvedArticleTicketsHelper
                .listResolvedArticleTickets(articleTickets);
    }

    private void ensureMemberExists(String memberId) {
        GetMemberInfoRequest request = new GetMemberInfoRequest(memberId);
        getMemberInfoPort.getMember(request)
                .orElseThrow(MemberNotFoundException::new);
    }

    private List<ArticleTicket> getTickets(String memberId, int pageNumber) {
        ArticleTicket.MemberId target = new ArticleTicket.MemberId(memberId);

        return listArticleTicketsByMemberPort
                .listByMember(target, pageNumber, PAGE_SIZE);
    }
}
