package com.boldfaced7.board.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
@Entity
public class Article extends BaseTimeEntity {

    public static final int MAX_TITLE_LENGTH = 100;
    public static final int MAX_CONTENT_LENGTH = 10000;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Long id;

    @Column(nullable = false, length = MAX_TITLE_LENGTH)
    private String title;

    @Column(nullable = false, length = MAX_CONTENT_LENGTH)
    private String content;

    private boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    protected Article() {}

    @Builder
    public Article(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.member = member;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void update(Article newArticle) {
        if (newArticle.getTitle() != null) updateTitle(newArticle.getTitle());
        if (newArticle.getContent() != null) updateContent(newArticle.getContent());
    }

    public void deactivate() {
        isActive = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return Objects.equals(id, article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
