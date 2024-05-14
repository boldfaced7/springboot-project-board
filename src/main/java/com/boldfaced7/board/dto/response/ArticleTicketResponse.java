package com.boldfaced7.board.dto.response;

import com.boldfaced7.board.dto.ArticleTicketDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleTicketResponse {
    private long articleTicketId;
    private long memberId;
    private boolean used;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public ArticleTicketResponse(ArticleTicketDto dto) {
        articleTicketId = dto.getArticleTicketId();
        memberId = dto.getMemberId();
        used = dto.isUsed();
        createdAt = dto.getCreatedAt();
        modifiedAt = dto.getModifiedAt();
    }
}
