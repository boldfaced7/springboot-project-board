package com.boldfaced7.articleticket.application;

import com.boldfaced7.articleticket.domain.ArticleTicket;
import com.boldfaced7.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleTicketDto {
    private long articleTicketId;
    private long memberId;
    private boolean used;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public ArticleTicketDto(long articleTicketId) {
        this.articleTicketId = articleTicketId;
    }

    public ArticleTicketDto(long articleTicketId, long memberId) {
        this.articleTicketId = articleTicketId;
        this.memberId = memberId;
    }

    public ArticleTicketDto(ArticleTicket articleTicket) {
        articleTicketId = articleTicket.getId();
        memberId = articleTicket.getMember().getId();
        used = articleTicket.isUsed();
        createdAt = articleTicket.getCreatedAt();
        modifiedAt = articleTicket.getModifiedAt();
    }

    public ArticleTicketDto(ArticleTicket articleTicket, Member member) {
        articleTicketId = articleTicket.getId();
        memberId = member.getId();
        used = articleTicket.isUsed();
        createdAt = articleTicket.getCreatedAt();
        modifiedAt = articleTicket.getModifiedAt();
    }
}
