package com.boldfaced7.application.port.out;

import com.boldfaced7.domain.ArticleTicket;

public interface SaveArticleTicketPort {
    ArticleTicket save(ArticleTicket articleTicket);
}
