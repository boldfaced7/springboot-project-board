package com.boldfaced7.adapter.in.web;

import com.boldfaced7.WebAdapter;
import com.boldfaced7.application.port.in.ListArticleTicketsByEventCommand;
import com.boldfaced7.application.port.in.ListArticleTicketsByEventIdQuery;
import com.boldfaced7.domain.ResolvedArticleTicket;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@WebAdapter
@RequiredArgsConstructor
public class ListArticleTicketsByEventController {

    private final ListArticleTicketsByEventIdQuery listArticleTicketsByEventIdQuery;

    @GetMapping("events/{eventId}/tickets")
    public ResponseEntity<List<ListArticleTicketsByEventResponse>> listTicketsByEvent(
            @PathVariable String eventId,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page
    ) {
        List<ResolvedArticleTicket> tickets = listByEvent(eventId, page);
        List<ListArticleTicketsByEventResponse> responses = toResponses(tickets);
        return ResponseEntity.ok(responses);
    }

    private List<ResolvedArticleTicket> listByEvent(String eventId, int page) {
        ListArticleTicketsByEventCommand command
                = new ListArticleTicketsByEventCommand(eventId, page);
        return listArticleTicketsByEventIdQuery.listArticleTicketsByEvent(command);
    }

    private List<ListArticleTicketsByEventResponse> toResponses(List<ResolvedArticleTicket> tickets) {
        return tickets.stream()
                .map(ticket -> new ListArticleTicketsByEventResponse(
                        ticket.getId(),
                        ticket.getTicketEventId(),
                        ticket.getMemberId(),
                        ticket.getIssuedAt(),
                        ticket.getExpiringAt()
                ))
                .toList();
    }

    public record ListArticleTicketsByEventResponse(
            String id,
            String eventId,
            String memberId,
            LocalDateTime issuedAt,
            LocalDateTime validUntil
    ) {}
}

