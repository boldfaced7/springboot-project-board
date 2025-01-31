package com.boldfaced7.adapter.in.web;

import com.boldfaced7.WebAdapter;
import com.boldfaced7.application.port.in.*;
import com.boldfaced7.domain.ResolvedAttachment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@WebAdapter
@RequiredArgsConstructor
public class GetAttachmentController {

    private final GetAttachmentQuery getAttachmentQuery;

    @GetMapping("/attachments/{attachmentId}")
    public ResponseEntity<GetAttachmentResponse> getAttachment(
            @PathVariable String attachmentId
    ) {
        ResolvedAttachment resolved = getById(attachmentId);
        GetAttachmentResponse response = toResponse(resolved);
        return ResponseEntity.ok(response);
    }

    private ResolvedAttachment getById(String attachmentId) {
        GetAttachmentCommand command = new GetAttachmentCommand(attachmentId);
        return getAttachmentQuery.getAttachment(command);
    }

    private GetAttachmentResponse toResponse(ResolvedAttachment resolvedAttachment) {
        return new GetAttachmentResponse(
                resolvedAttachment.getId(),
                resolvedAttachment.getStoredName(),
                resolvedAttachment.getAttachmentUrl(),
                resolvedAttachment.getCreatedAt()
        );
    }

    public record GetAttachmentResponse(
            String id,
            String name,
            String url,
            LocalDateTime uploadedAt
    ) {}
}