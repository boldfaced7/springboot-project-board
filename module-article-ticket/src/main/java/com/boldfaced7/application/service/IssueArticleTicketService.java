package com.boldfaced7.application.service;

import com.boldfaced7.UseCase;
import com.boldfaced7.application.port.in.IssueArticleTicketCommand;
import com.boldfaced7.application.port.in.IssueArticleTicketUseCase;
import com.boldfaced7.application.port.out.*;
import com.boldfaced7.application.port.out.GetMemberInfoPort;
import com.boldfaced7.application.port.out.GetMemberInfoRequest;
import com.boldfaced7.domain.ArticleTicket;
import com.boldfaced7.exception.articleticketevent.ArticleTicketEventNotFoundException;
import com.boldfaced7.exception.member.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@Transactional
@RequiredArgsConstructor
public class IssueArticleTicketService implements IssueArticleTicketUseCase {

    private final GetMemberInfoPort getMemberInfoPort;
    private final GetTicketEventInfoPort getTicketEventInfoPort;
    private final SaveArticleTicketPort saveArticleTicketPort;
    @Override
    public ArticleTicket issueTicket(IssueArticleTicketCommand command) {
        ensureMemberExists(command.memberId());
        ensureEventExists(command.ticketEventId());
        return saveArticleTicket(command);
    }

    private void ensureMemberExists(String memberId) {
        GetMemberInfoRequest request = new GetMemberInfoRequest(memberId);
        getMemberInfoPort.getMember(request)
                .orElseThrow(MemberNotFoundException::new);
    }

    private void ensureEventExists(String ticketEventId) {
        GetTicketEventInfoRequest request = new GetTicketEventInfoRequest(ticketEventId);
        getTicketEventInfoPort.findTicketEvent(request)
                .orElseThrow(ArticleTicketEventNotFoundException::new);
    }

    private ArticleTicket saveArticleTicket(IssueArticleTicketCommand command) {
        ArticleTicket generated = ArticleTicket.generate(
                new ArticleTicket.TicketEventId(command.ticketEventId()),
                new ArticleTicket.MemberId(command.memberId())
        );
        return saveArticleTicketPort.save(generated);
    }
}
