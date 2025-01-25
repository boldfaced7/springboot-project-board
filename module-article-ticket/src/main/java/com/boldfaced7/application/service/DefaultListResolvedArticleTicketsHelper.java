package com.boldfaced7.application.service;

import com.boldfaced7.application.port.in.ListResolvedArticleTicketsHelper;
import com.boldfaced7.application.port.out.*;
import com.boldfaced7.application.port.out.ListMembersInfoPort;
import com.boldfaced7.application.port.out.ListMembersInfoRequest;
import com.boldfaced7.domain.ArticleTicket;
import com.boldfaced7.domain.ResolvedArticleTicket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class DefaultListResolvedArticleTicketsHelper implements ListResolvedArticleTicketsHelper {

    private final ListMembersInfoPort listMembersInfoPort;
    private final ListTicketEventsInfoPort listTicketEventsInfoPort;

    @Override
    public List<ResolvedArticleTicket> listResolvedArticleTickets(List<ArticleTicket> articleTickets) {
        List<String> nicknames = getNicknames(articleTickets);
        ListTicketEventsInfoResponse eventsInfo = getEventsInfo(articleTickets);
        return resolveAll(articleTickets, nicknames, eventsInfo);
    }

    private List<String> getNicknames(List<ArticleTicket> articleTickets) {
        List<String> memberIds = extractFields(articleTickets, ArticleTicket::getMemberId);
        ListMembersInfoRequest request = new ListMembersInfoRequest(memberIds);
        return listMembersInfoPort.getMembers(request).nicknames();
    }

    private ListTicketEventsInfoResponse getEventsInfo(List<ArticleTicket> articleTickets) {
        List<String> eventIds = extractFields(articleTickets, ArticleTicket::getTicketEventId);
        ListTicketEventsInfoRequest request = new ListTicketEventsInfoRequest(eventIds);
        return listTicketEventsInfoPort.listTicketEvents(request);
    }

    private static <T> List<T> extractFields(List<ArticleTicket> fetched, Function<ArticleTicket, T> mapper) {
        return fetched.stream().map(mapper).toList();
    }

    private List<ResolvedArticleTicket> resolveAll(
            List<ArticleTicket> fetched,
            List<String> nicknames,
            ListTicketEventsInfoResponse eventsInfo
    ) {
        return ResolvedArticleTicket.resolveAll(
                fetched,
                nicknames,
                eventsInfo.displayNames(),
                eventsInfo.expiringAts(),
                eventsInfo.issueLimits()
        );
    }
}
