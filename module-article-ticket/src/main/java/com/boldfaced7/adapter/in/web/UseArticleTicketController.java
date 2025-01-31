package com.boldfaced7.adapter.in.web;

import com.boldfaced7.WebAdapter;
import com.boldfaced7.application.port.in.UseArticleTicketCommand;
import com.boldfaced7.application.port.in.UseArticleTicketUseCase;
import com.boldfaced7.domain.ArticleTicket;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.time.LocalDateTime;

@WebAdapter
@RequiredArgsConstructor
public class UseArticleTicketController {

    private final UseArticleTicketUseCase useArticleTicketUseCase;

    @PatchMapping("/tickets/{ticketId}")
    public ResponseEntity<UseArticleTicketResponse> useTicket(
            @PathVariable String ticketId,
            @RequestHeader("X-Member-Id") String memberId
    ) {
        ArticleTicket used = use(ticketId, memberId);
        UseArticleTicketResponse response = toResponse(used);
        return ResponseEntity.ok(response);
    }

    private ArticleTicket use(String ticketId, String memberId) {
        UseArticleTicketCommand command
                = new UseArticleTicketCommand(ticketId, memberId);
        return useArticleTicketUseCase.useArticleTicket(command);
    }

    private UseArticleTicketResponse toResponse(ArticleTicket articleTicket) {
        return new UseArticleTicketResponse(
                articleTicket.getId(),
                articleTicket.getTicketEventId(),
                articleTicket.getMemberId(),
                articleTicket.getUsedAt()
        );
    }

    public record UseArticleTicketResponse(
            String id,
            String eventId,
            String memberId,
            LocalDateTime usedAt
    ) {}
}