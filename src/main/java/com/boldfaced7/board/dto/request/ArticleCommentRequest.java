package com.boldfaced7.board.dto.request;

import com.boldfaced7.board.dto.ArticleCommentDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ArticleCommentRequest {

    private String content;
    private String author;

    @Builder
    public ArticleCommentRequest(String content, String author) {
        this.content = content;
        this.author = author;
    }

    public ArticleCommentDto toDto() {
        return ArticleCommentDto.builder()
                .content(content)
                .author(author)
                .build();
    }

    public ArticleCommentDto toDto(Long articleId) {
        return ArticleCommentDto.builder()
                .content(content)
                .author(author)
                .articleId(articleId)
                .build();
    }
}
