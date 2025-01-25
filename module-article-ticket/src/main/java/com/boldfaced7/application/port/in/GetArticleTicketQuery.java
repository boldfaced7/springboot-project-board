package com.boldfaced7.application.port.in;

import com.boldfaced7.domain.ResolvedArticleTicket;

public interface GetArticleTicketQuery {
    ResolvedArticleTicket getArticleTicket(GetArticleTicketCommand command);
}
