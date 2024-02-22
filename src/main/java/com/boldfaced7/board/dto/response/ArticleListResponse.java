package com.boldfaced7.board.dto.response;

import com.boldfaced7.board.dto.ArticleDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ArticleListResponse {
    private List<ArticleResponse> articles;

    public ArticleListResponse(List<ArticleDto> dtos) {
        articles = dtos.stream().map(ArticleResponse::new).toList();
    }
}
