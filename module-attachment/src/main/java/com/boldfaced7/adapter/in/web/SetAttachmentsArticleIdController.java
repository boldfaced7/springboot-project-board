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
public class SetAttachmentsArticleIdController {

    private final SetAttachmentsArticleIdUseCase setAttachmentsArticleIdUseCase;

    @PatchMapping("/articles/{articleId}/attachments")
    public ResponseEntity<List<SetAttachmentsArticleIdResponse>> setAttachmentsArticleId(
            @PathVariable String articleId,
            @RequestBody SetAttachmentsArticleIdRequest request,
            @RequestHeader("X-Member-Id") String memberId
    ) {
        List<Attachment> updated = setArticleId(memberId, articleId, request.attachmentIds());
        List<SetAttachmentsArticleIdResponse> responses = toResponses(updated);
        return ResponseEntity.ok(responses);
    }

    private List<Attachment> setArticleId(String memberId, String articleId, List<String> attachmentIds) {
        SetAttachmentsArticleIdCommand command = new SetAttachmentsArticleIdCommand(
                memberId,
                articleId,
                attachmentIds
        );
        return setAttachmentsArticleIdUseCase.setArticleId(command);
    }

    private List<SetAttachmentsArticleIdResponse> toResponses(List<Attachment> attachments) {
        return attachments.stream()
                .map(attachment -> new SetAttachmentsArticleIdResponse(
                        attachment.getId(),
                        attachment.getArticleId(),
                        attachment.getUpdatedAt()
                ))
                .toList();
    }

    public record SetAttachmentsArticleIdRequest(
            List<String> attachmentIds
    ) {}

    public record SetAttachmentsArticleIdResponse(
            String id,
            String articleId,
            LocalDateTime updatedAt
    ) {}
}