package com.boldfaced7.board.ticket.domain;

import com.boldfaced7.board.common.BaseTimeEntity;
import com.boldfaced7.board.member.domain.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Entity
public class ArticleTicket extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_ticket_id")
    private Long id;

    private boolean used = false;
    private boolean confirmed = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    protected ArticleTicket() {}

    @Builder
    public ArticleTicket(Member member) {
        this.member = member;
    }

    public boolean isAvailable() {
        return LocalDate.now().isEqual(getCreatedAt().toLocalDate());
    }
    public void useTicket() {
        used = true;
    }
    public void confirmTicket() {
        confirmed = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArticleTicket that = (ArticleTicket) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
