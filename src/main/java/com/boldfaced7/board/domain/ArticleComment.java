package com.boldfaced7.board.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
@Entity
public class ArticleComment extends BaseEntity {

    public static final int MAX_CONTENT_LENGTH = 1000;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_comment_id")
    private Long id;

    @Column(nullable = false, length = MAX_CONTENT_LENGTH)
    private String content;

    private boolean isActive = true;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    protected ArticleComment() {}

    @Builder
    public ArticleComment(String content, Article article, Member member) {
        this.content = content;
        this.article = article;
        this.member = member;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void update(ArticleComment articleComment) {
        updateContent(articleComment.getContent());
    }

    public void deactivate() {
        isActive = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArticleComment that = (ArticleComment) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
