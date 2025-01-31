package com.boldfaced7.adapter.in.web;

import com.boldfaced7.WebAdapter;
import com.boldfaced7.application.port.in.*;
import com.boldfaced7.domain.Attachment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@WebAdapter
@RequiredArgsConstructor
public class DeleteAttachmentsByArticleController {

    private final DeleteAttachmentsByArticleUseCase deleteAttachmentsByArticleUseCase;

    @DeleteMapping("/articles/{articleId}/attachments")
    public ResponseEntity<List<DeleteAttachmentsByArticleResponse>> deleteAttachments(
            @PathVariable String articleId,
            @RequestHeader("X-Member-Id") String memberId
    ) {
        List<Attachment> deleted = deleteAll(articleId, memberId);
        List<DeleteAttachmentsByArticleResponse> responses = toResponses(deleted);
        return ResponseEntity.ok(responses);
    }

    private List<Attachment> deleteAll(String articleId, String memberId) {
        DeleteAttachmentsByArticleCommand command
                = new DeleteAttachmentsByArticleCommand(articleId, memberId);
        return deleteAttachmentsByArticleUseCase.deleteAttachments(command);
    }

    private List<DeleteAttachmentsByArticleResponse> toResponses(List<Attachment> attachments) {
        return attachments.stream()
                .map(attachment -> new DeleteAttachmentsByArticleResponse(
                        attachment.getId(),
                        attachment.getDeletedAt()
                ))
                .toList();
    }

    public record DeleteAttachmentsByArticleResponse(
            String id,
            LocalDateTime deletedAt
    ) {}
}