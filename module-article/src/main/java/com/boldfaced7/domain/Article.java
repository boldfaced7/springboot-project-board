package com.boldfaced7.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Article {
    private final String id;
    private final String memberId;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final LocalDateTime deletedAt;

    public static Article generate(
            MemberId memberId,
            Title title,
            Content content
    ) {
        return new Article(
                null,
                memberId.value(),
                title.value(),
                content.value(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
        );
    }

    public static Article generate(
            Id id,
            MemberId memberId,
            Title title,
            Content content,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            LocalDateTime deletedAt
    ) {
        return new Article(
                id.value(),
                memberId.value(),
                title.value(),
                content.value(),
                createdAt,
                updatedAt,
                deletedAt
        );
    }

    public Article update(Title title, Content content) {
        return new Article(
                this.id,
                this.memberId,
                title.value(),
                content.value(),
                this.createdAt,
                LocalDateTime.now(),
                this.deletedAt
        );
    }

    public Article delete() {
        return new Article(
                this.id,
                this.memberId,
                this.title,
                this.content,
                this.createdAt,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public record Id(String value) {}
    public record MemberId(String value) {}
    public record Title(String value) {}
    public record Content(String value) {}
}
