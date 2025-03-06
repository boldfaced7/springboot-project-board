package com.boldfaced7.application.port.out;

import com.boldfaced7.domain.ArticleTicketEvent;

import java.util.List;

public interface ListAllArticleTicketEventsPort {
    List<ArticleTicketEvent> listAll(int pageNumber, int pageSize);
}
