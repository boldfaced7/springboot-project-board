package com.boldfaced7.board.comment.presentation.response;

import com.boldfaced7.board.comment.application.ArticleCommentDto;
import com.boldfaced7.board.common.CustomPage;
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
