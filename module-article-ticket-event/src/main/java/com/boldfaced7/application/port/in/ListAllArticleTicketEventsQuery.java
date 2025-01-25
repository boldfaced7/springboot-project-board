package com.boldfaced7.application.port.in;

import com.boldfaced7.domain.ArticleTicketEvent;

import java.util.List;

public interface ListAllArticleTicketEventsQuery {
    List<ArticleTicketEvent> listAllEvents(ListAllArticleTicketEventsCommand command);
}
