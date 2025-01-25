package com.boldfaced7.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleTicketEvent {
    private final String id;
    private final String displayName;
    private final LocalDateTime expiringAt;
    private final Long issueLimit;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final LocalDateTime deletedAt;


    public static ArticleTicketEvent generate(
            DisplayName displayName,
            ExpiringAt expiringAt,
            IssueLimit issueLimit
    ) {
        return new ArticleTicketEvent(
                null,
                displayName.value(),
                expiringAt.value(),
                issueLimit.value(),
                LocalDateTime.now(),
                null,
                null
        );
    }

    public static ArticleTicketEvent generate(
            Id id,
            DisplayName displayName,
            ExpiringAt expiringAt,
            IssueLimit issueLimit,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            LocalDateTime deletedAt

    ) {
        return new ArticleTicketEvent(
                id.value(),
                displayName.value(),
                expiringAt.value(),
                issueLimit.value(),
                createdAt,
                updatedAt,
                deletedAt
        );
    }

    public ArticleTicketEvent updateDisplayName(DisplayName displayName) {
        return new ArticleTicketEvent(
                this.id,
                displayName.value(),
                this.expiringAt,
                this.issueLimit,
                this.createdAt,
                LocalDateTime.now(),
                this.deletedAt
        );
    }

    public ArticleTicketEvent delete() {
        return new ArticleTicketEvent(
                this.id,
                this.displayName,
                this.expiringAt,
                this.issueLimit,
                this.createdAt,
                this.updatedAt,
                LocalDateTime.now()
        );
    }

    public record Id(String value) {}
    public record DisplayName(String value) {}
    public record ExpiringAt(LocalDateTime value) {}
    public record IssueLimit(Long value) {}

}
