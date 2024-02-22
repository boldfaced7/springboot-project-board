package com.boldfaced7.board.dto.response;

import com.boldfaced7.board.dto.ArticleCommentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
public class ArticleCommentListResponse {
    private List<ArticleCommentResponse> articleComments;

    public ArticleCommentListResponse(List<ArticleCommentDto> dtos) {
        articleComments = dtos.stream().map(ArticleCommentResponse::new).toList();
    }
}
