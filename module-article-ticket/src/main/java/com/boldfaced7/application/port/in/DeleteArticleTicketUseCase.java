package com.boldfaced7.application.port.in;

import com.boldfaced7.domain.ArticleTicket;

public interface DeleteArticleTicketUseCase {
    ArticleTicket deleteArticleTicket(DeleteArticleTicketCommand command);
}
