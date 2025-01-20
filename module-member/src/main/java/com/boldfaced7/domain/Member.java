package com.boldfaced7.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Member {
    private final String id;
    private final String email;
    private final String password;
    private final String nickname;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final LocalDateTime deletedAt;

    public static Member generate(
            Email email,
            Password password,
            Nickname nickname
    ) {
        return new Member(
                null,
                email.value(),
                password.value(),
                nickname.value(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
        );
    }

    public static Member generate(
            Id id,
            Email email,
            Password password,
            Nickname nickname,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            LocalDateTime deletedAt

    ) {
        return new Member(
                id.value(),
                email.value(),
                password.value(),
                nickname.value(),
                createdAt,
                updatedAt,
                deletedAt
        );
    }

    public Member changeNickname(Nickname nickname) {
        return new Member(
                this.id,
                this.email,
                this.password,
                nickname.value(),
                this.createdAt,
                LocalDateTime.now(),
                this.deletedAt
        );
    }

    public Member changePassword(Password password) {
        return new Member(
                this.id,
                this.email,
                password.value(),
                this.nickname,
                this.createdAt,
                LocalDateTime.now(),
                this.deletedAt
        );
    }

    public Member delete() {
        return new Member(
                this.id,
                this.email,
                this.password,
                this.nickname,
                this.createdAt,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public record Id(String value) {}
    public record Email(String value) {}
    public record Password(String value) {}
    public record Nickname(String value) {}
}
