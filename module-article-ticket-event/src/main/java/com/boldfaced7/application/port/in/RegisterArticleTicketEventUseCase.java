package com.boldfaced7.application.port.in;

import com.boldfaced7.domain.ArticleTicketEvent;

public interface RegisterArticleTicketEventUseCase {
    ArticleTicketEvent registerEvent(RegisterArticleTicketEventCommand command);
}
