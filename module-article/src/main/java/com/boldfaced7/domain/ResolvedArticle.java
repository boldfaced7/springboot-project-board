package com.boldfaced7.domain;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ResolvedArticle {

    private final Article article;
    @Getter private final String email;
    @Getter private final String nickname;
    @Getter private final List<String> attachmentUrls;
    @Getter private final boolean updated;

    public static ResolvedArticle resolve(
            Article article,
            String email,
            String nickname,
            List<String> attachmentUrls
    ) {
        return new ResolvedArticle(
                article,
                email,
                nickname,
                attachmentUrls
        );
    }

    public static List<ResolvedArticle> resolveAll(
            List<Article> articles,
            List<String> nicknames
    ) {
        List<ResolvedArticle> resolvedArticles = new ArrayList<>();

        for (int i = 0; i < articles.size(); i++) {
            Article article = articles.get(i);
            String nickname = nicknames.get(i);
            resolvedArticles.add(new ResolvedArticle(article, nickname));
        }
        return resolvedArticles;
    }

    public static List<ResolvedArticle> resolveAll(
            List<Article> articles,
            String nickname
    ) {
        return articles.stream()
                .map(article -> new ResolvedArticle(article, nickname))
                .toList();
    }

    private ResolvedArticle(
            Article article,
            String email,
            String nickname,
            List<String> attachmentUrls
    ) {
        this.article = article;
        this.email = email;
        this.nickname = nickname;
        this.attachmentUrls = attachmentUrls;
        this.updated = !article.getUpdatedAt()
                .equals(article.getCreatedAt());
    }

    private ResolvedArticle(
            Article article,
            String nickname
    ) {
        this.article = article;
        this.email = "";
        this.nickname = nickname;
        this.attachmentUrls = List.of();
        this.updated = !article.getUpdatedAt()
                .equals(article.getCreatedAt());
    }

    public String getId() {
        return article.getId();
    }

    public String getMemberId() {
        return article.getMemberId();
    }

    public String getTitle() {
        return article.getTitle();
    }

    public String getContent() {
        return article.getContent();
    }

    public LocalDateTime getCreatedAt() {
        return article.getCreatedAt();
    }

    public LocalDateTime getUpdatedAt() {
        return article.getUpdatedAt();
    }

    public LocalDateTime getDeletedAt() {
        return article.getDeletedAt();
    }
}
