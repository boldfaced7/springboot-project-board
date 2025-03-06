package com.boldfaced7.application.port.out;

import com.boldfaced7.domain.ArticleTicketEvent;

public interface SaveArticleTicketEventPort {
    ArticleTicketEvent save(ArticleTicketEvent articleTicketEvent);
}
