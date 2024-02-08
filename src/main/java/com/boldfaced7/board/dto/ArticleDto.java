package com.boldfaced7.board.dto;

import com.boldfaced7.board.domain.Article;
import com.boldfaced7.board.dto.response.ArticleResponse;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ArticleDto {

    private String title;
    private String content;
    private String author;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Builder
    public ArticleDto(String title, String content, String author,
                      LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public ArticleDto(Article article) {
        title = article.getTitle();
        content = article.getContent();
        author = article.getCreatedBy();
        createdAt = article.getCreatedAt();
        modifiedAt = article.getModifiedAt();
    }

    public Article toEntity() {
        return Article.builder()
                .title(title)
                .content(content)
                .build();
    }

    public ArticleResponse toResponse() {
        return ArticleResponse.builder()
                .title(title)
                .content(content)
                .author(author)
                .createdAt(createdAt)
                .modifiedAt(modifiedAt)
                .build();
    }
}
