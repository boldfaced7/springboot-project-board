package com.boldfaced7.application.port.out;

import com.boldfaced7.domain.ArticleTicket;

import java.util.List;

public interface ListArticleTicketsByEventPort {
    List<ArticleTicket> listByEvent(ArticleTicket.TicketEventId TicketEventId, int pageNumber, int pageSize);
}
