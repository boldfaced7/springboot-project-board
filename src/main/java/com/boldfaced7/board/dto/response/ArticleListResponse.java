package com.boldfaced7.board.dto.response;

import com.boldfaced7.board.dto.ArticleDto;
import com.boldfaced7.board.dto.CustomPage;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ArticleListResponse {
    private CustomPage<ArticleResponse> articles;

    public ArticleListResponse(CustomPage<ArticleDto> dtos) {
        articles = dtos.map(this::toListResponse);
    }

    private ArticleResponse toListResponse(ArticleDto dto) {
        return ArticleResponse.builder()
                .articleId(dto.getArticleId())
                .memberId(dto.getMemberId())
                .title(dto.getTitle())
                .content(dto.getContent())
                .author(dto.getAuthor())
                .createdAt(dto.getCreatedAt())
                .modifiedAt(dto.getModifiedAt())
                .build();
    }
}
