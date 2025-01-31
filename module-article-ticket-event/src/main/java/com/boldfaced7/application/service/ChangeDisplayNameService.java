package com.boldfaced7.application.service;

import com.boldfaced7.UseCase;
import com.boldfaced7.application.port.in.ChangeDisplayNameCommand;
import com.boldfaced7.application.port.in.ChangeDisplayNameUseCase;
import com.boldfaced7.application.port.out.FindArticleTicketEventPort;
import com.boldfaced7.application.port.out.UpdateArticleTicketEventPort;
import com.boldfaced7.domain.ArticleTicketEvent;
import com.boldfaced7.exception.articleticketevent.ArticleTicketEventNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import static com.boldfaced7.domain.ArticleTicketEvent.DisplayName;
import static com.boldfaced7.domain.ArticleTicketEvent.Id;

@UseCase
@Transactional
@RequiredArgsConstructor
public class ChangeDisplayNameService implements ChangeDisplayNameUseCase {

    private final FindArticleTicketEventPort findArticleTicketEventPort;
    private final UpdateArticleTicketEventPort updateArticleTicketEventPort;

    @Override
    public ArticleTicketEvent changeDisplayName(ChangeDisplayNameCommand command) {
        ArticleTicketEvent found = getEvent(command.articleTicketEventId());
        return useArticleTicket(found, command.displayName());
    }

    private ArticleTicketEvent getEvent(String ticketEventId) {
        Id id = new Id(ticketEventId);
        return findArticleTicketEventPort.findById(id)
                .orElseThrow(ArticleTicketEventNotFoundException::new);
    }

    private ArticleTicketEvent useArticleTicket(
            ArticleTicketEvent articleTicketEvent,
            String displayName
    ) {
        DisplayName target = new DisplayName(displayName);
        ArticleTicketEvent source = articleTicketEvent.updateDisplayName(target);
        return updateArticleTicketEventPort.update(source);
    }
}
