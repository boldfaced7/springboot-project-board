package com.boldfaced7.adapter.in.web;

import com.boldfaced7.WebAdapter;
import com.boldfaced7.application.port.in.GetArticleTicketCommand;
import com.boldfaced7.application.port.in.GetArticleTicketQuery;
import com.boldfaced7.domain.ResolvedArticleTicket;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;

@WebAdapter
@RequiredArgsConstructor
public class GetArticleTicketController {

    private final GetArticleTicketQuery getArticleTicketQuery;

    @GetMapping("/tickets/{ticketId}")
    public ResponseEntity<GetArticleTicketResponse> getArticleTicket(
            @PathVariable String ticketId
    ) {
        ResolvedArticleTicket resolved = getById(ticketId);
        GetArticleTicketResponse response = toResponse(resolved);
        return ResponseEntity.ok(response);
    }

    private ResolvedArticleTicket getById(String ticketId) {
        GetArticleTicketCommand command = new GetArticleTicketCommand(ticketId);
        return getArticleTicketQuery.getArticleTicket(command);
    }

    private GetArticleTicketResponse toResponse(ResolvedArticleTicket resolved) {
        return new GetArticleTicketResponse(
                resolved.getId(),
                resolved.getTicketEventId(),
                resolved.getMemberId(),
                resolved.getIssuedAt(),
                resolved.getExpiringAt()
        );
    }

    public record GetArticleTicketResponse(
            String id,
            String eventId,
            String memberId,
            LocalDateTime issuedAt,
            LocalDateTime validUntil
    ) {}
}
