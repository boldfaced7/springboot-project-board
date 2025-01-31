package com.boldfaced7.adapter.in.web;

import com.boldfaced7.WebAdapter;
import com.boldfaced7.application.port.in.DeleteArticleTicketCommand;
import com.boldfaced7.application.port.in.DeleteArticleTicketUseCase;
import com.boldfaced7.domain.ArticleTicket;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.time.LocalDateTime;

@WebAdapter
@RequiredArgsConstructor
public class DeleteArticleTicketController {

    private final DeleteArticleTicketUseCase deleteArticleTicketUseCase;

    @DeleteMapping("/tickets/{ticketId}")
    public ResponseEntity<DeleteArticleTicketResponse> deleteArticleTicket(
            @PathVariable String ticketId,
            @RequestHeader("X-Member-Id") String memberId
    ) {
        ArticleTicket deleted = delete(ticketId, memberId);
        DeleteArticleTicketResponse response = toResponse(deleted);
        return ResponseEntity.ok(response);
    }

    private ArticleTicket delete(String ticketId, String memberId) {
        DeleteArticleTicketCommand command
                = new DeleteArticleTicketCommand(ticketId, memberId);
        return deleteArticleTicketUseCase.deleteArticleTicket(command);
    }

    private DeleteArticleTicketResponse toResponse(ArticleTicket articleTicket) {
        return new DeleteArticleTicketResponse(
                articleTicket.getId(),
                articleTicket.getDeletedAt()
        );
    }

    public record DeleteArticleTicketResponse(
            String id,
            LocalDateTime deletedAt
    ) {}
}