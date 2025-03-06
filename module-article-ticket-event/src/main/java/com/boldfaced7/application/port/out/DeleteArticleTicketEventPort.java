package com.boldfaced7.application.port.out;

import com.boldfaced7.domain.ArticleTicketEvent;

public interface DeleteArticleTicketEventPort {
    ArticleTicketEvent delete(ArticleTicketEvent articleTicketEvent);
}
