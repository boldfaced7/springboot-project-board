package com.boldfaced7.board.dto.response;

import com.boldfaced7.board.dto.ArticleCommentDto;
import com.boldfaced7.board.dto.CustomPage;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ArticleCommentListResponse {
    private CustomPage<ArticleCommentResponse> articleComments;

    public ArticleCommentListResponse(CustomPage<ArticleCommentDto> dtos) {
        articleComments = dtos.map(ArticleCommentResponse::new);
    }
}
