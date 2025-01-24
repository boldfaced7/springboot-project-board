package com.boldfaced7.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class ResolvedAttachment {
    private final Attachment attachment;
    @Getter private final String attachmentUrl;

    public static ResolvedAttachment resolve(
            Attachment attachment,
            String attachmentUrl
    ) {
        return new ResolvedAttachment(
                attachment,
                attachmentUrl
        );
    }

    public static List<ResolvedAttachment> resolveAll(
            List<Attachment> attachments,
            List<String> urls
    ) {
        List<ResolvedAttachment> resolvedAttachments = new ArrayList<>();

        for (int i = 0; i < attachments.size(); i++) {
            Attachment attachment = attachments.get(i);
            String url = urls.get(i);
            ResolvedAttachment resolved = new ResolvedAttachment(attachment, url);
            resolvedAttachments.add(resolved);
        }
        return resolvedAttachments;
    }

    public String getId() {
        return attachment.getId();
    }

    public String getArticleId() {
        return attachment.getArticleId();
    }

    public String getMemberId() {
        return attachment.getMemberId();
    }

    public String getUploadedName() {
        return attachment.getUploadedName();
    }

    public String getStoredName() {
        return attachment.getStoredName();
    }

    public LocalDateTime getCreatedAt() {
        return attachment.getCreatedAt();
    }

    public LocalDateTime getUpdatedAt() {
        return attachment.getUpdatedAt();
    }

    public LocalDateTime getDeletedAt() {
        return attachment.getDeletedAt();
    }
}
