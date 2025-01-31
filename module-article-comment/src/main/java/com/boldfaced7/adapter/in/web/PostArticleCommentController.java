package com.boldfaced7.adapter.in.web;

import com.boldfaced7.WebAdapter;
import com.boldfaced7.application.port.in.PostArticleCommentCommand;
import com.boldfaced7.application.port.in.PostArticleCommentUseCase;
import com.boldfaced7.domain.ArticleComment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.time.LocalDateTime;

@WebAdapter
@RequiredArgsConstructor
public class PostArticleCommentController {

    private final PostArticleCommentUseCase postArticleCommentUseCase;

    @PostMapping("/comments")
    public ResponseEntity<PostArticleCommentResponse> postArticleComment(
            @RequestBody PostArticleCommentRequest request,
            @RequestHeader("X-Member-Id") String memberId
    ) {
        ArticleComment posted = postComment(request, memberId);
        PostArticleCommentResponse response = toResponse(posted);
        return ResponseEntity.ok(response);
    }

    private ArticleComment postComment(PostArticleCommentRequest request, String memberId) {
        PostArticleCommentCommand command = new PostArticleCommentCommand(
                request.articleId(),
                memberId,
                request.content()
        );
        return postArticleCommentUseCase.postArticleComment(command);
    }

    private PostArticleCommentResponse toResponse(ArticleComment articleComment) {
        return new PostArticleCommentResponse(
                articleComment.getId(),
                articleComment.getArticleId(),
                articleComment.getMemberId(),
                articleComment.getContent(),
                articleComment.getCreatedAt()
        );
    }

    public record PostArticleCommentRequest(
            String articleId,
            String content
    ) {}

    public record PostArticleCommentResponse(
            String id,
            String articleId,
            String memberId,
            String content,
            LocalDateTime createdAt
    ) {}
}