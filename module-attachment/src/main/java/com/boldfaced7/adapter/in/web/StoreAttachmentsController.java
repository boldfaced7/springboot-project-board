package com.boldfaced7.adapter.in.web;

import com.boldfaced7.WebAdapter;
import com.boldfaced7.application.port.in.*;
import com.boldfaced7.domain.Attachment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@WebAdapter
@RequiredArgsConstructor
public class StoreAttachmentsController {

    private final StoreAttachmentsUseCase storeAttachmentsUseCase;

    @PostMapping("/attachments")
    public ResponseEntity<List<StoreAttachmentsResponse>> storeAttachments(
            @RequestParam("files") List<MultipartFile> files,
            @RequestHeader("X-Member-Id") String memberId
    ) {
        List<Attachment> stored = storeAll(files, memberId);
        List<StoreAttachmentsResponse> responses = toResponses(stored);
        return ResponseEntity.ok(responses);
    }

    private List<Attachment> storeAll(List<MultipartFile> files, String memberId) {
        StoreAttachmentsCommand command = new StoreAttachmentsCommand(memberId, files);
        return storeAttachmentsUseCase.storeAttachments(command);
    }

    private List<StoreAttachmentsResponse> toResponses(List<Attachment> attachments) {
        return attachments.stream()
                .map(attachment -> new StoreAttachmentsResponse(
                        attachment.getId(),
                        attachment.getStoredName(),
                        attachment.getCreatedAt()
                ))
                .toList();
    }

    public record StoreAttachmentsResponse(
            String id,
            String name,
            LocalDateTime uploadedAt
    ) {}
}