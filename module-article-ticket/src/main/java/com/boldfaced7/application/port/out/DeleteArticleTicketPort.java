package com.boldfaced7.application.port.out;

import com.boldfaced7.domain.ArticleTicket;

public interface DeleteArticleTicketPort {
    ArticleTicket delete(ArticleTicket articleTicket);
}
