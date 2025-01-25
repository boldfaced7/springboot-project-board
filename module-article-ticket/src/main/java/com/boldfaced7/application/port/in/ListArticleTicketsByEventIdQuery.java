package com.boldfaced7.application.port.in;

import com.boldfaced7.domain.ResolvedArticleTicket;

import java.util.List;

public interface ListArticleTicketsByEventIdQuery {
    List<ResolvedArticleTicket> listArticleTicketsByEvent(ListArticleTicketsByEventCommand command);
}
