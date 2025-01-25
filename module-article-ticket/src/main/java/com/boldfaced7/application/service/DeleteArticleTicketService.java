package com.boldfaced7.application.service;

import com.boldfaced7.UseCase;
import com.boldfaced7.application.port.in.DeleteArticleTicketCommand;
import com.boldfaced7.application.port.in.DeleteArticleTicketUseCase;
import com.boldfaced7.application.port.out.DeleteArticleTicketPort;
import com.boldfaced7.application.port.out.FindArticleTicketPort;
import com.boldfaced7.domain.ArticleTicket;
import com.boldfaced7.exception.articleticket.ArticleTicketNotFoundException;
import com.boldfaced7.exception.auth.UnauthorizedException;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class DeleteArticleTicketService implements DeleteArticleTicketUseCase {

    private final FindArticleTicketPort findArticleTicketPort;
    private final DeleteArticleTicketPort deleteArticleTicketPort;

    @Override
    public ArticleTicket deleteArticleTicket(DeleteArticleTicketCommand command) {
        ArticleTicket found = getArticleTicket(command.targetArticleTicketId());
        ensureAuthentication(command.requiringMemberId(), found.getMemberId());
        return deleteArticleTicket(found);
    }

    private ArticleTicket getArticleTicket(String articleTicketId) {
        ArticleTicket.Id id = new ArticleTicket.Id(articleTicketId);
        return findArticleTicketPort.findById(id)
                .orElseThrow(ArticleTicketNotFoundException::new);
    }

    private void ensureAuthentication(String fromCommand, String found) {
        if (!found.equals(fromCommand)) {
            throw new UnauthorizedException();
        }
    }

    private ArticleTicket deleteArticleTicket(ArticleTicket found) {
        ArticleTicket deleted = found.delete();
        return deleteArticleTicketPort.delete(deleted);
    }
}
