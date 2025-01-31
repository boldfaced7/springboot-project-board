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
public class ListAttachmentsByMemberController {

    private final ListAttachmentsByMemberQuery listAttachmentsByMemberQiery;

    @GetMapping("/members/{memberId}/attachments")
    public ResponseEntity<List<ListMemberAttachmentsResponse>> listMemberAttachments(
            @PathVariable String memberId,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page
    ) {
        List<ResolvedAttachment> attachments = listByMember(memberId, page);
        List<ListMemberAttachmentsResponse> responses = toResponses(attachments);
        return ResponseEntity.ok(responses);
    }

    private List<ResolvedAttachment> listByMember(String memberId, int page) {
        ListAttachmentsByMemberCommand command
                = new ListAttachmentsByMemberCommand(memberId, page);
        return listAttachmentsByMemberQiery.listMemberAttachments(command);
    }

    private List<ListMemberAttachmentsResponse> toResponses(List<ResolvedAttachment> attachments) {
        return attachments.stream()
                .map(attachment -> new ListMemberAttachmentsResponse(
                        attachment.getId(),
                        attachment.getStoredName(),
                        attachment.getAttachmentUrl(),
                        attachment.getCreatedAt()
                ))
                .toList();
    }

    public record ListMemberAttachmentsResponse(
            String id,
            String name,
            String url,
            LocalDateTime uploadedAt
    ) {}
}