package com.boldfaced7.board.dto.response;

import com.boldfaced7.board.dto.ArticleTicketDto;
import com.boldfaced7.board.dto.CustomPage;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ArticleTicketListResponse {
    private CustomPage<ArticleTicketResponse> articleTickets;

    public ArticleTicketListResponse(CustomPage<ArticleTicketDto> dtos) {
        articleTickets = dtos.map(this::toListResponse);
    }

    private ArticleTicketResponse toListResponse(ArticleTicketDto dto) {
        return ArticleTicketResponse.builder()
                .articleTicketId(dto.getArticleTicketId())
                .memberId(dto.getMemberId())
                .used(dto.isUsed())
                .createdAt(dto.getCreatedAt())
                .modifiedAt(dto.getModifiedAt())
                .build();
    }
}
