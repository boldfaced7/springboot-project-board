package com.boldfaced7.application.service;

import com.boldfaced7.UseCase;
import com.boldfaced7.application.port.in.DeleteArticleTicketEventCommand;
import com.boldfaced7.application.port.in.DeleteArticleTicketEventUseCase;
import com.boldfaced7.application.port.out.DeleteArticleTicketEventPort;
import com.boldfaced7.application.port.out.FindArticleTicketEventPort;
import com.boldfaced7.domain.ArticleTicketEvent;
import com.boldfaced7.exception.articleticketevent.ArticleTicketEventNotFoundException;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class DeleteArticleTicketEventService implements DeleteArticleTicketEventUseCase {

    private final FindArticleTicketEventPort findArticleTicketEventPort;
    private final DeleteArticleTicketEventPort deleteArticleTicketEventPort;

    @Override
    public ArticleTicketEvent deleteEvent(DeleteArticleTicketEventCommand command) {
        ArticleTicketEvent found = getArticleTicketEvent(command.articleTicketEventId());
        return deleteArticleTicket(found);
    }

    private ArticleTicketEvent getArticleTicketEvent(String articleTicketEventId) {
        ArticleTicketEvent.Id id = new ArticleTicketEvent.Id(articleTicketEventId);
        return findArticleTicketEventPort.findById(id)
                .orElseThrow(ArticleTicketEventNotFoundException::new);
    }

    private ArticleTicketEvent deleteArticleTicket(ArticleTicketEvent found) {
        ArticleTicketEvent deleted = found.delete();
        return deleteArticleTicketEventPort.delete(deleted);
    }
}
