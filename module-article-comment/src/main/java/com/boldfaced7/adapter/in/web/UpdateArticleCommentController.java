package com.boldfaced7.adapter.in.web;

import com.boldfaced7.WebAdapter;
import com.boldfaced7.application.port.in.UpdateArticleCommentCommand;
import com.boldfaced7.application.port.in.UpdateArticleCommentUseCase;
import com.boldfaced7.domain.ArticleComment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@WebAdapter
@RequiredArgsConstructor
public class UpdateArticleCommentController {
    private final UpdateArticleCommentUseCase updateArticleCommentUseCase;

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<UpdateArticleCommentResponse> updateArticleComment(
            @PathVariable String commentId,
            @RequestBody UpdateArticleCommentRequest request,
            @RequestHeader("X-Member-Id") String memberId
    ) {
        ArticleComment updated = updateComment(request, commentId, memberId);
        UpdateArticleCommentResponse response = toResponse(updated);
        return ResponseEntity.ok(response);
    }

    private static UpdateArticleCommentResponse toResponse(ArticleComment articleComment) {
        return new UpdateArticleCommentResponse(
                articleComment.getId(),
                articleComment.getArticleId(),
                articleComment.getMemberId(),
                articleComment.getContent(),
                articleComment.getUpdatedAt().toString()
        );
    }

    private ArticleComment updateComment(
            UpdateArticleCommentRequest request,
            String commentId,
            String memberId
            ) {
        UpdateArticleCommentCommand command = new UpdateArticleCommentCommand(
                commentId,
                memberId,
                request.content()
        );
        return updateArticleCommentUseCase.updateArticleComment(command);
    }

    public record UpdateArticleCommentRequest(
            String memberId,
            String content
    ) {}

    public record UpdateArticleCommentResponse(
            String id,
            String articleId,
            String memberId,
            String content,
            String updatedAt
    ) {}
}