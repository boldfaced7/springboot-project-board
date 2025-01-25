package com.boldfaced7.application.port.in;

import com.boldfaced7.domain.ArticleTicket;
import com.boldfaced7.domain.ResolvedArticleTicket;

import java.util.List;

public interface ListResolvedArticleTicketsHelper {
    List<ResolvedArticleTicket> listResolvedArticleTickets(List<ArticleTicket> articleTickets);
}
