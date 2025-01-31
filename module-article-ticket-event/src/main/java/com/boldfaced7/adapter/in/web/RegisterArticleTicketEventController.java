package com.boldfaced7.adapter.in.web;

import com.boldfaced7.WebAdapter;
import com.boldfaced7.application.port.in.RegisterArticleTicketEventCommand;
import com.boldfaced7.application.port.in.RegisterArticleTicketEventUseCase;
import com.boldfaced7.domain.ArticleTicketEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.time.LocalDateTime;

@WebAdapter
@RequiredArgsConstructor
public class RegisterArticleTicketEventController {

    private final RegisterArticleTicketEventUseCase registerArticleTicketEventUseCase;

    @PostMapping("/events")
    public ResponseEntity<RegisterArticleTicketEventResponse> registerEvent(
            @RequestBody RegisterArticleTicketEventRequest request,
            @RequestHeader("X-Member-Id") String memberId
    ) {
        ArticleTicketEvent registered = register(request, memberId);
        RegisterArticleTicketEventResponse response = toResponse(registered);
        return ResponseEntity.ok(response);
    }

    private ArticleTicketEvent register(RegisterArticleTicketEventRequest request, String memberId) {
        RegisterArticleTicketEventCommand command = new RegisterArticleTicketEventCommand(
                request.displayName(),
                request.expiringAt(),
                request.issueLimit(),
                memberId
        );
        return registerArticleTicketEventUseCase.registerEvent(command);
    }

    private RegisterArticleTicketEventResponse toResponse(ArticleTicketEvent event) {
        return new RegisterArticleTicketEventResponse(
                event.getId(),
                event.getDisplayName(),
                event.getExpiringAt(),
                event.getIssueLimit(),
                event.getCreatedAt()
        );
    }

    public record RegisterArticleTicketEventRequest(
            String displayName,
            LocalDateTime expiringAt,
            Long issueLimit
    ) {}

    public record RegisterArticleTicketEventResponse(
            String id,
            String displayName,
            LocalDateTime expiringAt,
            Long issueLimit,
            LocalDateTime createdAt
    ) {}
}
