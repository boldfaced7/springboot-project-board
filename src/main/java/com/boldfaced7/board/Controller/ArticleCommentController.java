package com.boldfaced7.board.Controller;

import com.boldfaced7.board.dto.ArticleCommentDto;
import com.boldfaced7.board.dto.request.ArticleCommentRequest;
import com.boldfaced7.board.dto.response.ArticleCommentListResponse;
import com.boldfaced7.board.dto.response.ArticleCommentResponse;
import com.boldfaced7.board.service.ArticleCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class ArticleCommentController {

    private final ArticleCommentService articleCommentService;

    @GetMapping("/articleComments")
    public ResponseEntity<ArticleCommentListResponse> getArticleComments() {
        List<ArticleCommentResponse> articleComments = articleCommentService.getArticleComments()
                .stream()
                .map(ArticleCommentResponse::new)
                .toList();

        ArticleCommentListResponse response = ArticleCommentListResponse.builder()
                .articleComments(articleComments)
                .build();

        return ResponseEntity.ok()
                .body(response);
    }

    @GetMapping("/articleComments/{articleCommentsId}")
    public ResponseEntity<ArticleCommentResponse> getArticleComment(
            @PathVariable Long articleCommentsId) {
        ArticleCommentDto dto = articleCommentService.getArticleComment(articleCommentsId);
        ArticleCommentResponse response =  new ArticleCommentResponse(dto);

        return ResponseEntity.ok()
                .body(response);
    }

    @PatchMapping("/articleComments/{articleCommentsId}")
    public ResponseEntity<Void> updateArticleComment(
            @PathVariable Long articleCommentsId,
            @RequestBody ArticleCommentRequest articleCommentRequest) {
        articleCommentService.updateArticleComment(articleCommentsId, articleCommentRequest.toDto());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/articleComments/{articleCommentsId}")
    public ResponseEntity<Void> deleteArticleComment(
            @PathVariable Long articleCommentsId) {
        articleCommentService.softDeleteArticleComment(articleCommentsId);

        return ResponseEntity.ok().build();
    }

    /*
     * 게시글 연관 댓글 관련 API
     */

    @GetMapping("/articles/{articleId}/articleComments")
    public ResponseEntity<ArticleCommentListResponse> getArticleComments(
            @PathVariable Long articleId) {
        List<ArticleCommentResponse> articleComments = articleCommentService.getArticleComments(articleId)
                .stream()
                .map(ArticleCommentResponse::new)
                .toList();

        ArticleCommentListResponse response = ArticleCommentListResponse.builder()
                .articleComments(articleComments)
                .build();

        return ResponseEntity.ok()
                .body(response);
    }

    @GetMapping("/articles/{articleId}/articleComments/{articleCommentsId}")
    public ResponseEntity<ArticleCommentResponse> getArticleComment(
            @PathVariable Long articleId,
            @PathVariable Long articleCommentsId) {
        return getArticleComment(articleCommentsId);
    }

    @PostMapping("/articles/{articleId}/articleComments")
    public ResponseEntity<Void> postNewArticleComment(
            @PathVariable Long articleId,
            @RequestBody ArticleCommentRequest articleCommentRequest) {
        Long articleCommentId = articleCommentService.saveArticleComment(articleCommentRequest.toDto(articleId));

        return ResponseEntity.created(URI.create(createUri(articleId, articleCommentId))).build();
    }

    @PatchMapping("/articles/{articleId}/articleComments/{articleCommentsId}")
    public ResponseEntity<Void> updateArticleComment(
            @PathVariable Long articleId,
            @PathVariable Long articleCommentsId,
            @RequestBody ArticleCommentRequest articleCommentRequest) {
        return updateArticleComment(articleCommentsId, articleCommentRequest);
    }

    @DeleteMapping("/articles/{articleId}/articleComments/{articleCommentsId}")
    public ResponseEntity<Void> deleteArticleComment(
            @PathVariable Long articleId,
            @PathVariable Long articleCommentsId) {
        return deleteArticleComment(articleCommentsId);
    }

    private String createUri(Long articleId, Long articleCommentId) {
        return "/api/articles/" + articleId + "/articleComments/" + articleCommentId;
    }
}
