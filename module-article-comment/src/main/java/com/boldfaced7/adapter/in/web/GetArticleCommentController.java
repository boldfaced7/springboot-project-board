package com.boldfaced7.adapter.in.web;

import com.boldfaced7.WebAdapter;
import com.boldfaced7.application.port.in.GetArticleCommentCommand;
import com.boldfaced7.application.port.in.GetArticleCommentQuery;
import com.boldfaced7.domain.ResolvedArticleComment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;

@WebAdapter
@RequiredArgsConstructor
public class GetArticleCommentController {
    private final GetArticleCommentQuery getArticleCommentQuery;

    @GetMapping("/comments/{commentId}")
    public ResponseEntity<GetArticleCommentResponse> getArticleComment(
            @PathVariable String commentId
    ) {
        ResolvedArticleComment found = getComment(commentId);
        GetArticleCommentResponse response = toResponse(found);
        return ResponseEntity.ok(response);
    }

    private ResolvedArticleComment getComment(String commentId) {
        GetArticleCommentCommand command = new GetArticleCommentCommand(commentId);
        return getArticleCommentQuery.getArticleComment(command);
    }

    private GetArticleCommentResponse toResponse(ResolvedArticleComment resolvedArticleComment) {
        return new GetArticleCommentResponse(
                resolvedArticleComment.getId(),
                resolvedArticleComment.getArticleId(),
                resolvedArticleComment.getMemberId(),
                resolvedArticleComment.getContent(),
                resolvedArticleComment.getNickname(),
                resolvedArticleComment.getCreatedAt(),
                resolvedArticleComment.getUpdatedAt()
        );
    }

    public record GetArticleCommentResponse(
            String id,
            String articleId,
            String memberId,
            String content,
            String nickname,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {}
}
