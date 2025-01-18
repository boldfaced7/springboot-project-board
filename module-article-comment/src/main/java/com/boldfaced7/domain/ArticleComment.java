package com.boldfaced7.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleComment {
    private final String id;
    private final String articleId;
    private final String memberId;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final LocalDateTime deletedAt;

    public static ArticleComment generate(
            ArticleId articleId,
            MemberId memberId,
            Content content
    ) {
        return new ArticleComment(
                null,
                articleId.value(),
                memberId.value(),
                content.value(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
        );
    }

    public static ArticleComment generate(
            Id id,
            ArticleId articleId,
            MemberId memberId,
            Content content,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            LocalDateTime deletedAt

    ) {
        return new ArticleComment(
                id.value(),
                articleId.value(),
                memberId.value(),
                content.value(),
                createdAt,
                updatedAt,
                deletedAt
        );
    }

    public ArticleComment update(Content content) {
        return new ArticleComment(
                this.id,
                this.articleId,
                this.memberId,
                content.value(),
                this.createdAt,
                LocalDateTime.now(),
                this.deletedAt
        );
    }

    public ArticleComment delete() {
        return new ArticleComment(
                this.id,
                this.articleId,
                this.memberId,
                this.content,
                this.createdAt,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public record Id(String value) {}
    public record ArticleId(String value) {}
    public record MemberId(String value) {}
    public record Content(String value) {}
}
