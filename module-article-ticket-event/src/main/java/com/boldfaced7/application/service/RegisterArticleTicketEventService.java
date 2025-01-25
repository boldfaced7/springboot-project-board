package com.boldfaced7.application.service;

import com.boldfaced7.UseCase;
import com.boldfaced7.application.port.in.RegisterArticleTicketEventCommand;
import com.boldfaced7.application.port.in.RegisterArticleTicketEventUseCase;
import com.boldfaced7.application.port.out.SaveArticleTicketEventPort;
import com.boldfaced7.domain.ArticleTicketEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@Transactional
@RequiredArgsConstructor
public class RegisterArticleTicketEventService implements RegisterArticleTicketEventUseCase {

    private final SaveArticleTicketEventPort saveArticleTicketEventPort;
    @Override
    public ArticleTicketEvent registerEvent(RegisterArticleTicketEventCommand command) {
        ArticleTicketEvent generated = ArticleTicketEvent.generate(
                new ArticleTicketEvent.DisplayName(command.displayName()),
                new ArticleTicketEvent.ExpiringAt(command.expiringAt()),
                new ArticleTicketEvent.IssueLimit(command.issueLimit())
        );
        return saveArticleTicketEventPort.save(generated);
    }
}
