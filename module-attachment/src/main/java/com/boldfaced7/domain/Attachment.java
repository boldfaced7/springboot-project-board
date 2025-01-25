package com.boldfaced7.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Attachment {
    private final String id;
    private final String articleId;
    private final String memberId;
    private final String uploadedName;
    private final String storedName;

    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final LocalDateTime deletedAt;

    public static Attachment generate(
            MemberId memberId,
            UploadedName uploadedName,
            StoredName storedName
    ) {
        return new Attachment(
                null,
                null,
                memberId.value(),
                uploadedName.value(),
                storedName.value(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
        );
    }

    public static Attachment generate(
            Id id,
            ArticleId articleId,
            MemberId memberId,
            UploadedName uploadedName,
            StoredName storedName,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            LocalDateTime deletedAt
    ) {
        return new Attachment(
                id.value(),
                articleId.value(),
                memberId.value(),
                uploadedName.value(),
                storedName.value(),
                createdAt,
                updatedAt,
                deletedAt
        );
    }

    public Attachment registerArticleId(
            ArticleId articleId
    ) {
        return new Attachment(
                this.id,
                articleId.value(),
                this.memberId,
                this.uploadedName,
                this.storedName,
                this.createdAt,
                LocalDateTime.now(),
                null
        );
    }

    public Attachment delete() {
        return new Attachment(
                this.id,
                this.articleId,
                this.memberId,
                this.uploadedName,
                this.storedName,
                this.createdAt,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public record Id(String value) {}
    public record ArticleId(String value) {}
    public record MemberId(String value) {}
    public record UploadedName(String value) {}
    public record StoredName(String value) {}
}
