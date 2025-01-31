package com.boldfaced7.adapter.in.web;

import com.boldfaced7.WebAdapter;
import com.boldfaced7.application.port.in.DeleteArticleTicketEventCommand;
import com.boldfaced7.application.port.in.DeleteArticleTicketEventUseCase;
import com.boldfaced7.domain.ArticleTicketEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.time.LocalDateTime;

@WebAdapter
@RequiredArgsConstructor
public class DeleteArticleTicketEventController {

    private final DeleteArticleTicketEventUseCase deleteArticleTicketEventUseCase;

    @DeleteMapping("/events/{eventId}")
    public ResponseEntity<DeleteArticleTicketEventResponse> deleteEvent(
            @PathVariable String eventId,
            @RequestHeader("X-Member-Id") String memberId
    ) {
        ArticleTicketEvent deleted = deleteById(eventId, memberId);
        DeleteArticleTicketEventResponse response = toResponse(deleted);
        return ResponseEntity.ok(response);
    }

    private ArticleTicketEvent deleteById(String eventId, String memberId) {
        DeleteArticleTicketEventCommand command
                = new DeleteArticleTicketEventCommand(eventId, memberId);
        return deleteArticleTicketEventUseCase.deleteEvent(command);
    }

    private DeleteArticleTicketEventResponse toResponse(ArticleTicketEvent event) {
        return new DeleteArticleTicketEventResponse(
                event.getId(),
                event.getDeletedAt()
        );
    }

    public record DeleteArticleTicketEventResponse(
            String id,
            LocalDateTime deletedAt
    ) {}
}
