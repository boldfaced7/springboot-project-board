package com.boldfaced7.board.dto.response;

import com.boldfaced7.board.dto.ArticleCommentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;


@Data
@NoArgsConstructor
public class ArticleCommentListResponse {
    private Page<ArticleCommentResponse> articleComments;

    public ArticleCommentListResponse(Page<ArticleCommentDto> dtos) {
        articleComments = dtos.map(ArticleCommentResponse::new);
    }
}
