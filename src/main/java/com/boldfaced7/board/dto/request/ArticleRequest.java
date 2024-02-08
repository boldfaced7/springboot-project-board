package com.boldfaced7.board.dto.request;

import com.boldfaced7.board.dto.ArticleDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ArticleRequest {
    private String title;
    private String content;
    private String author;

    @Builder
    public ArticleRequest(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public ArticleDto toDto() {
        return ArticleDto.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();
    }
}
