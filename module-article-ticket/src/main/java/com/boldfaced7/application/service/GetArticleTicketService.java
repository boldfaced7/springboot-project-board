package com.boldfaced7.application.service;

import com.boldfaced7.Query;
import com.boldfaced7.application.port.in.GetArticleTicketCommand;
import com.boldfaced7.application.port.in.GetArticleTicketQuery;
import com.boldfaced7.application.port.out.*;
import com.boldfaced7.application.port.out.GetMemberInfoPort;
import com.boldfaced7.application.port.out.GetMemberInfoRequest;
import com.boldfaced7.application.port.out.GetMemberInfoResponse;
import com.boldfaced7.domain.ArticleTicket;
import com.boldfaced7.domain.ResolvedArticleTicket;
import com.boldfaced7.exception.articleticket.ArticleTicketNotFoundException;
import com.boldfaced7.exception.articleticketevent.ArticleTicketEventNotFoundException;
import com.boldfaced7.exception.member.MemberNotFoundException;
import lombok.RequiredArgsConstructor;

@Query
@RequiredArgsConstructor
public class GetArticleTicketService implements GetArticleTicketQuery {

    private final FindArticleTicketPort findArticleTicketPort;
    private final GetMemberInfoPort getMemberInfoPort;
    private final GetTicketEventInfoPort getTicketEventInfoPort;

    @Override
    public ResolvedArticleTicket getArticleTicket(GetArticleTicketCommand command) {
        ArticleTicket articleTicket = getTicket(command.articleTicketId());
        String nickname = getNickname(articleTicket.getMemberId());
        GetTicketEventInfoResponse eventInfo = getEventInfo(articleTicket.getTicketEventId());

        return resolve(articleTicket, nickname, eventInfo);
    }

    private ArticleTicket getTicket(String ticketEventId) {
        ArticleTicket.Id id = new ArticleTicket.Id(ticketEventId);
        return findArticleTicketPort.findById(id)
                .orElseThrow(ArticleTicketNotFoundException::new);
    }

    private String getNickname(String memberId) {
        GetMemberInfoRequest request = new GetMemberInfoRequest(memberId);
        return getMemberInfoPort.getMember(request)
                .map(GetMemberInfoResponse::nickname)
                .orElseThrow(MemberNotFoundException::new);
    }

    private GetTicketEventInfoResponse getEventInfo(String ticketEventId) {
        GetTicketEventInfoRequest request = new GetTicketEventInfoRequest(ticketEventId);
        return getTicketEventInfoPort.findTicketEvent(request)
                .orElseThrow(ArticleTicketEventNotFoundException::new);
    }

    private ResolvedArticleTicket resolve(
            ArticleTicket articleTicket,
            String ownerNickname,
            GetTicketEventInfoResponse response
    ) {
        return ResolvedArticleTicket.resolve(
                articleTicket,
                ownerNickname,
                response.displayName(),
                response.expiringAt(),
                response.issueLimit()
        );
    }
}
