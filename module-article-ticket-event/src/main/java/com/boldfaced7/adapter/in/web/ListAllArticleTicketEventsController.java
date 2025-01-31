package com.boldfaced7.adapter.in.web;

import com.boldfaced7.WebAdapter;
import com.boldfaced7.application.port.in.ListAllArticleTicketEventsCommand;
import com.boldfaced7.application.port.in.ListAllArticleTicketEventsQuery;
import com.boldfaced7.domain.ArticleTicketEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@WebAdapter
@RequiredArgsConstructor
public class ListAllArticleTicketEventsController {

    private final ListAllArticleTicketEventsQuery listAllArticleTicketEventsQuery;

    @GetMapping("/events")
    public ResponseEntity<List<ListAllArticleTicketEventsResponse>> listAllEvents(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page
    ) {
        List<ArticleTicketEvent> events = listAll(page);
        List<ListAllArticleTicketEventsResponse> responses = toResponses(events);
        return ResponseEntity.ok(responses);
    }

    private List<ArticleTicketEvent> listAll(int page) {
        ListAllArticleTicketEventsCommand command = new ListAllArticleTicketEventsCommand(page);
        return listAllArticleTicketEventsQuery.listAllEvents(command);
    }

    private List<ListAllArticleTicketEventsResponse> toResponses(List<ArticleTicketEvent> events) {
        return events.stream()
                .map(event -> new ListAllArticleTicketEventsResponse(
                        event.getId(),
                        event.getDisplayName(),
                        event.getExpiringAt(),
                        event.getIssueLimit(),
                        event.getCreatedAt()
                ))
                .toList();
    }

    public record ListAllArticleTicketEventsResponse(
            String id,
            String displayName,
            LocalDateTime expiringAt,
            Long issueLimit,
            LocalDateTime createdAt
    ) {}
}