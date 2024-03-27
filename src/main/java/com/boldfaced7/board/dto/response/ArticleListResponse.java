package com.boldfaced7.board.dto.response;

import com.boldfaced7.board.dto.ArticleDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@NoArgsConstructor
public class ArticleListResponse {
    private Page<ArticleResponse> articles;

    public ArticleListResponse(Page<ArticleDto> dtos) {
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
