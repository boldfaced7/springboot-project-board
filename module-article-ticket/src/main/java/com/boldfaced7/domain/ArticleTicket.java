package com.boldfaced7.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleTicket {
    private final String id;
    private final String ticketEventId;
    private final String memberId;
    private final LocalDateTime issuedAt;
    private final LocalDateTime usedAt;
    private final LocalDateTime deletedAt;

    public static ArticleTicket generate(
            TicketEventId ticketEventId,
            MemberId memberId
    ) {
        return new ArticleTicket(
                null,
                ticketEventId.value(),
                memberId.value(),
                LocalDateTime.now(),
                null,
                null
        );
    }

    public static ArticleTicket generate(
            Id id,
            TicketEventId ticketEventId,
            MemberId memberId,
            LocalDateTime issuedAt,
            LocalDateTime usedAt,
            LocalDateTime deletedAt

    ) {
        return new ArticleTicket(
                id.value(),
                ticketEventId.value(),
                memberId.value(),
                issuedAt,
                usedAt,
                deletedAt
        );
    }

    public ArticleTicket use() {
        return new ArticleTicket(
                this.id,
                this.ticketEventId,
                this.memberId,
                this.issuedAt,
                LocalDateTime.now(),
                this.deletedAt
        );
    }

    public ArticleTicket delete() {
        return new ArticleTicket(
                this.id,
                this.ticketEventId,
                this.memberId,
                this.issuedAt,
                this.usedAt,
                LocalDateTime.now()
        );
    }

    public record Id(String value) {}
    public record TicketEventId(String value) {}
    public record MemberId(String value) {}
}
