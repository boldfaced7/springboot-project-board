package com.boldfaced7.board.dto.request;

import com.boldfaced7.board.dto.ArticleCommentDto;
import com.boldfaced7.board.dto.response.AuthResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleCommentRequest {
    private String content;
    private String author;

    public ArticleCommentDto toDto(Long articleId) {
        return ArticleCommentDto.builder()
                .articleId(articleId)
                .content(content)
                .author(author)
                .build();
    }

    public ArticleCommentDto toDtoForUpdating(Long targetId) {
        return ArticleCommentDto.builder()
                .articleCommentId(targetId)
                .content(content)
                .author(author)
                .build();
    }
}
