package com.boldfaced7.application.port.in;

import com.boldfaced7.domain.ResolvedArticleTicket;

import java.util.List;

public interface ListAllArticleTicketsQuery {
    List<ResolvedArticleTicket> listAllArticleTickets(ListAllArticleTicketsCommand command);
}
