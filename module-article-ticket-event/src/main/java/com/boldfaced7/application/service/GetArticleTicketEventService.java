package com.boldfaced7.application.service;

import com.boldfaced7.Query;
import com.boldfaced7.application.port.in.GetArticleTicketEventCommand;
import com.boldfaced7.application.port.in.GetArticleTicketEventQuery;
import com.boldfaced7.application.port.out.FindArticleTicketEventPort;
import com.boldfaced7.domain.ArticleTicketEvent;
import com.boldfaced7.exception.articleticketevent.ArticleTicketEventNotFoundException;
import lombok.RequiredArgsConstructor;

@Query
@RequiredArgsConstructor
public class GetArticleTicketEventService implements GetArticleTicketEventQuery {

    private final FindArticleTicketEventPort findArticleTicketEventPort;

    @Override
    public ArticleTicketEvent getEvent(GetArticleTicketEventCommand command) {
        ArticleTicketEvent.Id id
                = new ArticleTicketEvent.Id(command.articleTicketEventId());
        return findArticleTicketEventPort.findById(id)
                .orElseThrow(ArticleTicketEventNotFoundException::new);
    }
}
