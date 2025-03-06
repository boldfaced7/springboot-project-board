package com.boldfaced7.adapter.out.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleTicketJpaEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_ticket_id")
    private Long id;
    private String ticketEventId;
    private String memberId;

    private LocalDateTime issuedAt;
    private LocalDateTime usedAt;
    private LocalDateTime deletedAt;

    public ArticleTicketJpaEntity(
            String ticketEventId,
            String memberId,
            LocalDateTime issuedAt,
            LocalDateTime usedAt,
            LocalDateTime deletedAt
    ) {
        this.ticketEventId = ticketEventId;
        this.memberId = memberId;
        this.issuedAt = issuedAt;
        this.usedAt = usedAt;
        this.deletedAt = deletedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArticleTicketJpaEntity that = (ArticleTicketJpaEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
