package com.boldfaced7.application.service;

import com.boldfaced7.Query;
import com.boldfaced7.application.port.in.ListArticleTicketsByEventCommand;
import com.boldfaced7.application.port.in.ListArticleTicketsByEventIdQuery;
import com.boldfaced7.application.port.in.ListResolvedArticleTicketsHelper;
import com.boldfaced7.application.port.out.*;
import com.boldfaced7.domain.ArticleTicket;
import com.boldfaced7.domain.ResolvedArticleTicket;
import com.boldfaced7.exception.articleticketevent.ArticleTicketEventNotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Query
@RequiredArgsConstructor
public class ListArticleTicketsByEventService implements ListArticleTicketsByEventIdQuery {

    private static final int PAGE_SIZE = 20;

    private final GetTicketEventInfoPort getTicketEventInfoPort;
    private final ListArticleTicketsByEventPort listArticleTicketsByEventPort;
    private final ListResolvedArticleTicketsHelper listResolvedArticleTicketsHelper;

    @Override
    public List<ResolvedArticleTicket> listArticleTicketsByEvent(ListArticleTicketsByEventCommand command) {
        ensureEventExists(command.ticketEventId());
        List<ArticleTicket> fetched = getArticleTickets(command);
        return listResolvedArticleTicketsHelper.listResolvedArticleTickets(fetched);
    }

    private void ensureEventExists(String eventId) {
        GetTicketEventInfoRequest request = new GetTicketEventInfoRequest(eventId);
        getTicketEventInfoPort.findTicketEvent(request)
                .orElseThrow(ArticleTicketEventNotFoundException::new);
    }
    
    private List<ArticleTicket> getArticleTickets(ListArticleTicketsByEventCommand command) {
        return listArticleTicketsByEventPort.listByEvent(
                new ArticleTicket.TicketEventId(command.ticketEventId()),
                command.pageNumber(),
                PAGE_SIZE
        );
    }
}
