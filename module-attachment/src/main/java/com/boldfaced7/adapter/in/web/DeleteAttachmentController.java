package com.boldfaced7.adapter.in.web;

import com.boldfaced7.WebAdapter;
import com.boldfaced7.application.port.in.DeleteAttachmentCommand;
import com.boldfaced7.application.port.in.DeleteAttachmentUseCase;
import com.boldfaced7.domain.Attachment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.time.LocalDateTime;

@WebAdapter
@RequiredArgsConstructor
public class DeleteAttachmentController {

    private final DeleteAttachmentUseCase deleteAttachmentUseCase;

    @DeleteMapping("/attachments/{attachmentId}")
    public ResponseEntity<DeleteAttachmentResponse> deleteAttachments(
            @PathVariable String attachmentId,
            @RequestHeader("X-Member-Id") String memberId
    ) {
        Attachment deleted = delete(attachmentId, memberId);
        DeleteAttachmentResponse response = toResponse(deleted);
        return ResponseEntity.ok(response);
    }

    private Attachment delete(String attachmentId, String memberId) {
        DeleteAttachmentCommand command
                = new DeleteAttachmentCommand(attachmentId, memberId);
        return deleteAttachmentUseCase.deleteAttachment(command);
    }

    private DeleteAttachmentResponse toResponse(Attachment attachment) {
        return new DeleteAttachmentResponse(
                attachment.getId(),
                attachment.getDeletedAt()
        );
    }

    public record DeleteAttachmentResponse(
            String id,
            LocalDateTime deletedAt
    ) {}
}