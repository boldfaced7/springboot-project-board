package com.boldfaced7.application.port.in;

import com.boldfaced7.domain.ArticleTicket;

public interface UseArticleTicketUseCase {
    ArticleTicket useArticleTicket(UseArticleTicketCommand command);
}
