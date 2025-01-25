package com.boldfaced7.domain;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ResolvedArticleTicket {

    private final ArticleTicket articleTicket;
    @Getter private final String ownerNickname;
    @Getter private final String displayName;
    @Getter private final LocalDateTime expiringAt;
    @Getter private final Long issueLimit;
    @Getter private final boolean used;

    public static ResolvedArticleTicket resolve(
            ArticleTicket articleTicket,
            String ownerNickname,
            String displayName,
            LocalDateTime expiringAt,
            Long issueLimit
    ) {
        return new ResolvedArticleTicket(
                articleTicket,
                ownerNickname,
                displayName,
                expiringAt,
                issueLimit
        );
    }

    public static List<ResolvedArticleTicket> resolveAll(
            List<ArticleTicket> articleTickets,
            List<String> ownerNicknames,
            List<String> displayNames,
            List<LocalDateTime> expiringAts,
            List<Long> issueLimits
    ) {
        List<ResolvedArticleTicket> resolvedArticleTickets = new ArrayList<>();

        for (int i = 0; i < articleTickets.size(); i++) {
            resolvedArticleTickets.add(
                    new ResolvedArticleTicket(
                            articleTickets.get(i),
                            ownerNicknames.get(i),
                            displayNames.get(i),
                            expiringAts.get(i),
                            issueLimits.get(i)
                    )
            );
        }
        return resolvedArticleTickets;
    }

    public static List<ResolvedArticleTicket> resolveAll(
            List<ArticleTicket> ArticleTickets,
            String ownerNickname,
            String displayName,
            LocalDateTime expiringAt,
            Long issueLimit
    ) {
        return ArticleTickets.stream()
                .map(ticket -> new ResolvedArticleTicket(
                        ticket,
                        ownerNickname,
                        displayName,
                        expiringAt,
                        issueLimit
                ))
                .toList();
    }

    private ResolvedArticleTicket(
            ArticleTicket articleTicket,
            String ownerNickname,
            String displayName,
            LocalDateTime expiringAt,
            Long issueLimit
    ) {
        this.articleTicket = articleTicket;
        this.ownerNickname = ownerNickname;
        this.displayName = displayName;
        this.expiringAt = expiringAt;
        this.issueLimit = issueLimit;
        this.used = Objects.nonNull(articleTicket.getUsedAt());
    }

    public String getId() {
        return articleTicket.getId();
    }

    public String getTicketEventId() {
        return articleTicket.getTicketEventId();
    }

    public String getMemberId() {
        return articleTicket.getMemberId();
    }

    public LocalDateTime getIssuedAt() {
        return articleTicket.getIssuedAt();
    }

    public LocalDateTime getUsedAt() {
        return articleTicket.getUsedAt();
    }

    public LocalDateTime getDeletedAt() {
        return articleTicket.getDeletedAt();
    }

}
