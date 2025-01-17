package com.boldfaced7.api.adapter.in.web.response;

import com.boldfaced7.application.ArticleCommentDto;
import com.boldfaced7.common.CustomPage;
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
