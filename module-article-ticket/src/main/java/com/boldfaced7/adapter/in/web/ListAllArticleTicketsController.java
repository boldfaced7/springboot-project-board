package com.boldfaced7.adapter.in.web;

import com.boldfaced7.WebAdapter;
import com.boldfaced7.application.port.in.ListAllArticleTicketsCommand;
import com.boldfaced7.application.port.in.ListAllArticleTicketsQuery;
import com.boldfaced7.domain.ResolvedArticleTicket;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@WebAdapter
@RequiredArgsConstructor
public class ListAllArticleTicketsController {

    private final ListAllArticleTicketsQuery listAllArticleTicketsQuery;

    @GetMapping("/api/tickets")
    public ResponseEntity<List<ListAllArticleTicketsResponse>> listAllTickets(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page
    ) {
        List<ResolvedArticleTicket> tickets = getAll(page);
        List<ListAllArticleTicketsResponse> responses = toResponses(tickets);
        return ResponseEntity.ok(responses);
    }

    private List<ResolvedArticleTicket> getAll(int page) {
        ListAllArticleTicketsCommand command = new ListAllArticleTicketsCommand(page);
        return listAllArticleTicketsQuery.listAllArticleTickets(command);
    }

    private List<ListAllArticleTicketsResponse> toResponses(List<ResolvedArticleTicket> tickets) {
        return tickets.stream()
                .map(ticket -> new ListAllArticleTicketsResponse(
                        ticket.getId(),
                        ticket.getTicketEventId(),
                        ticket.getMemberId(),
                        ticket.getIssuedAt(),
                        ticket.getExpiringAt()
                ))
                .toList();
    }

    public record ListAllArticleTicketsResponse(
            String id,
            String eventId,
            String memberId,
            LocalDateTime issuedAt,
            LocalDateTime validUntil
    ) {}
}