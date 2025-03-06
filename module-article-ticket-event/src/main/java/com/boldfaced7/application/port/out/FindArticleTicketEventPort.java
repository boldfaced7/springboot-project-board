package com.boldfaced7.application.port.out;

import com.boldfaced7.domain.ArticleTicketEvent;

import java.util.Optional;

public interface FindArticleTicketEventPort {
    Optional<ArticleTicketEvent> findById(ArticleTicketEvent.Id id);
}
