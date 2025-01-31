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
public class ListAttachmentsByArticleController {

    private final ListAttachmentsByArticleQuery listAttachmentsByArticleQuery;

    @GetMapping("/articles/{articleId}/attachments")
    public ResponseEntity<List<ListArticleAttachmentsResponse>> listArticleAttachments(
            @PathVariable String articleId,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page
    ) {
        List<ResolvedAttachment> attachments = listByArticle(articleId, page);
        List<ListArticleAttachmentsResponse> responses = toResponses(attachments);
        return ResponseEntity.ok(responses);
    }

    private List<ResolvedAttachment> listByArticle(String articleId, int page) {
        ListAttachmentsByArticleCommand command = new ListAttachmentsByArticleCommand(articleId, page);
        return listAttachmentsByArticleQuery.listArticleAttachments(command);
    }

    private List<ListArticleAttachmentsResponse> toResponses(List<ResolvedAttachment> attachments) {
        return attachments.stream()
                .map(attachment -> new ListArticleAttachmentsResponse(
                        attachment.getId(),
                        attachment.getStoredName(),
                        attachment.getAttachmentUrl(),
                        attachment.getCreatedAt()
                ))
                .toList();
    }

    public record ListArticleAttachmentsResponse(
            String id,
            String name,
            String url,
            LocalDateTime uploadedAt
    ) {}
}