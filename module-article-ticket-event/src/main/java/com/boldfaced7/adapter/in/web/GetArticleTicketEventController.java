package com.boldfaced7.adapter.in.web;

import com.boldfaced7.WebAdapter;
import com.boldfaced7.application.port.in.GetArticleTicketEventCommand;
import com.boldfaced7.application.port.in.GetArticleTicketEventQuery;
import com.boldfaced7.domain.ArticleTicketEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;

@WebAdapter
@RequiredArgsConstructor
public class GetArticleTicketEventController {

    private final GetArticleTicketEventQuery getArticleTicketEventQuery;

    @GetMapping("/events/{eventId}")
    public ResponseEntity<GetArticleTicketEventResponse> getEvent(
            @PathVariable String eventId
    ) {
        ArticleTicketEvent event = getById(eventId);
        GetArticleTicketEventResponse response = toResponse(event);
        return ResponseEntity.ok(response);
    }

    private ArticleTicketEvent getById(String eventId) {
        GetArticleTicketEventCommand command = new GetArticleTicketEventCommand(eventId);
        return getArticleTicketEventQuery.getEvent(command);
    }

    private GetArticleTicketEventResponse toResponse(ArticleTicketEvent event) {
        return new GetArticleTicketEventResponse(
                event.getId(),
                event.getDisplayName(),
                event.getExpiringAt(),
                event.getIssueLimit(),
                event.getCreatedAt()
        );
    }

    public record GetArticleTicketEventResponse(
            String id,
            String displayName,
            LocalDateTime expiringAt,
            Long issueLimit,
            LocalDateTime createdAt
    ) {}
}