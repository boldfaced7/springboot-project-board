package com.boldfaced7.board.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleCommentListResponse {
    private List<ArticleCommentResponse> articleComments;
}
