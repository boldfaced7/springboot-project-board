package com.boldfaced7.application.port.out;

import com.boldfaced7.domain.ArticleTicket;

import java.util.Optional;

public interface FindArticleTicketPort {
    Optional<ArticleTicket> findById(ArticleTicket.Id id);
}
