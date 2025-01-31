package com.boldfaced7.adapter.in.web;

import com.boldfaced7.WebAdapter;
import com.boldfaced7.application.port.in.ChangeDisplayNameCommand;
import com.boldfaced7.application.port.in.ChangeDisplayNameUseCase;
import com.boldfaced7.domain.ArticleTicketEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@WebAdapter
@RequiredArgsConstructor
public class ChangeDisplayNameController {

    private final ChangeDisplayNameUseCase changeDisplayNameUseCase;

    @PatchMapping("/events/{eventId}")
    public ResponseEntity<ChangeDisplayNameResponse> changeDisplayName(
            @PathVariable String eventId,
            @RequestBody ChangeDisplayNameRequest request,
            @RequestHeader("X-Member-Id") String memberId
    ) {
        ArticleTicketEvent updated = change(eventId, memberId, request.displayName());
        ChangeDisplayNameResponse response = toResponse(updated);
        return ResponseEntity.ok(response);
    }

    private ArticleTicketEvent change(String eventId, String memberId, String displayName) {
        ChangeDisplayNameCommand command
                = new ChangeDisplayNameCommand(eventId, displayName, memberId);
        return changeDisplayNameUseCase.changeDisplayName(command);
    }

    private ChangeDisplayNameResponse toResponse(ArticleTicketEvent event) {
        return new ChangeDisplayNameResponse(
                event.getId(),
                event.getDisplayName(),
                event.getUpdatedAt()
        );
    }

    public record ChangeDisplayNameRequest(String displayName) {}

    public record ChangeDisplayNameResponse(
            String id,
            String displayName,
            LocalDateTime updatedAt
    ) {}
}
