package com.boldfaced7.application.service;

import com.boldfaced7.UseCase;
import com.boldfaced7.application.port.in.UseArticleTicketCommand;
import com.boldfaced7.application.port.in.UseArticleTicketUseCase;
import com.boldfaced7.application.port.out.FindArticleTicketPort;
import com.boldfaced7.application.port.out.UpdateArticleTicketPort;
import com.boldfaced7.domain.ArticleTicket;
import com.boldfaced7.exception.articleticket.ArticleTicketAlreadyUsedException;
import com.boldfaced7.exception.articleticket.ArticleTicketNotFoundException;
import com.boldfaced7.exception.auth.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@UseCase
@Transactional
@RequiredArgsConstructor
public class UseArticleTicketService implements UseArticleTicketUseCase {

    private final FindArticleTicketPort findArticleTicketPort;
    private final UpdateArticleTicketPort updateArticleTicketPort;

    @Override
    public ArticleTicket useArticleTicket(UseArticleTicketCommand command) {
        ArticleTicket found = getArticleTicket(command.articleTicketId());
        ensureAuthentication(command.memberId(), found.getMemberId());
        ensureTicketAvailable(found.getUsedAt());
        return useArticleTicket(found);
    }

    private ArticleTicket getArticleTicket(String ticketEventId) {
        ArticleTicket.Id id = new ArticleTicket.Id(ticketEventId);
        return findArticleTicketPort.findById(id)
                .orElseThrow(ArticleTicketNotFoundException::new);
    }

    private void ensureAuthentication(String fromCommand, String found) {
        if (!found.equals(fromCommand)) {
            throw new UnauthorizedException();
        }
    }

    private void ensureTicketAvailable(LocalDateTime usedAt) {
        if (Objects.nonNull(usedAt)) {
            throw new ArticleTicketAlreadyUsedException();
        }
    }

    private ArticleTicket useArticleTicket(ArticleTicket articleTicket) {
        ArticleTicket source = articleTicket.use();
        return updateArticleTicketPort.update(source);
    }
}
