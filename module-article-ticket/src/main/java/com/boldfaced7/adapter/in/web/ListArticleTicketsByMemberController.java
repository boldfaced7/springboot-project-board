package com.boldfaced7.adapter.in.web;

import com.boldfaced7.WebAdapter;
import com.boldfaced7.application.port.in.ListArticleTicketsByMemberCommand;
import com.boldfaced7.application.port.in.ListArticleTicketsByMemberIdQuery;
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
public class ListArticleTicketsByMemberController {

    private final ListArticleTicketsByMemberIdQuery listArticleTicketsByMemberIdQuery;

    @GetMapping("/members/{memberId}/tickets")
    public ResponseEntity<List<ListArticleTicketsByMemberResponse>> listTicketsByMember(
            @PathVariable String memberId,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page
    ) {
        List<ResolvedArticleTicket> tickets = listByMember(memberId, page);
        List<ListArticleTicketsByMemberResponse> responses = toResponses(tickets);
        return ResponseEntity.ok(responses);
    }

    private List<ResolvedArticleTicket> listByMember(String memberId, int page) {
        ListArticleTicketsByMemberCommand command
                = new ListArticleTicketsByMemberCommand(memberId, page);
        return listArticleTicketsByMemberIdQuery.listMemberArticleTickets(command);
    }

    private List<ListArticleTicketsByMemberResponse> toResponses(List<ResolvedArticleTicket> tickets) {
        return tickets.stream()
                .map(ticket -> new ListArticleTicketsByMemberResponse(
                        ticket.getId(),
                        ticket.getTicketEventId(),
                        ticket.getMemberId(),
                        ticket.getIssuedAt(),
                        ticket.getExpiringAt()
                ))
                .toList();
    }

    public record ListArticleTicketsByMemberResponse(
            String id,
            String eventId,
            String memberId,
            LocalDateTime issuedAt,
            LocalDateTime validUntil
    ) {}
}