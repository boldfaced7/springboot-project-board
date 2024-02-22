package com.boldfaced7.board.dto.request;

import com.boldfaced7.board.dto.ArticleDto;
import com.boldfaced7.board.dto.response.AuthResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleRequest {
    private String title;
    private String content;
    private String author;

    public ArticleDto toDto() {
        return ArticleDto.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();
    }

    public ArticleDto toDto(Long targetId) {
        return ArticleDto.builder()
                .articleId(targetId)
                .title(title)
                .content(content)
                .author(author)
                .build();
    }
}
