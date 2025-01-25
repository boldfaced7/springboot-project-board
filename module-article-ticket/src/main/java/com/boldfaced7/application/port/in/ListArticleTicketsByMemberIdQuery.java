package com.boldfaced7.application.port.in;

import com.boldfaced7.domain.ResolvedArticleTicket;

import java.util.List;

public interface ListArticleTicketsByMemberIdQuery {
    List<ResolvedArticleTicket> listMemberArticleTickets(ListArticleTicketsByMemberCommand command);
}
