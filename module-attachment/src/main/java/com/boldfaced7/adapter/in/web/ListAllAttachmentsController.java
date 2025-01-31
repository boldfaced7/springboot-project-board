package com.boldfaced7.adapter.in.web;

import com.boldfaced7.WebAdapter;
import com.boldfaced7.application.port.in.*;
import com.boldfaced7.domain.ResolvedAttachment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@WebAdapter
@RequiredArgsConstructor
public class ListAllAttachmentsController {

    private final ListAllAttachmentsQuery listAllAttachmentsQuery;

    @GetMapping("/attachments")
    public ResponseEntity<List<ListAllAttachmentsResponse>> listAllAttachments(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page
    ) {
        List<ResolvedAttachment> attachments = listAll(page);
        List<ListAllAttachmentsResponse> responses = toResponses(attachments);
        return ResponseEntity.ok(responses);
    }

    private List<ResolvedAttachment> listAll(int page) {
        ListAllAttachmentsCommand command = new ListAllAttachmentsCommand(page);
        return listAllAttachmentsQuery.listAllAttachments(command);
    }

    private List<ListAllAttachmentsResponse> toResponses(List<ResolvedAttachment> attachments) {
        return attachments.stream()
                .map(attachment -> new ListAllAttachmentsResponse(
                        attachment.getId(),
                        attachment.getStoredName(),
                        attachment.getAttachmentUrl(),
                        attachment.getCreatedAt()
                ))
                .toList();
    }

    public record ListAllAttachmentsResponse(
            String id,
            String name,
            String url,
            LocalDateTime uploadedAt
    ) {}
}
