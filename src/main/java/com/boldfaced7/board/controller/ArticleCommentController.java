package com.boldfaced7.board.controller;

import com.boldfaced7.board.dto.ArticleCommentDto;
import com.boldfaced7.board.dto.ArticleDto;
import com.boldfaced7.board.dto.MemberDto;
import com.boldfaced7.board.dto.request.SaveArticleCommentRequest;
import com.boldfaced7.board.dto.request.UpdateArticleCommentRequest;
import com.boldfaced7.board.dto.response.ArticleCommentListResponse;
import com.boldfaced7.board.dto.response.ArticleCommentResponse;
import com.boldfaced7.board.service.ArticleCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class ArticleCommentController {

    private final ArticleCommentService articleCommentService;

    @GetMapping({"/articleComments/{articleCommentId}",
            "articles/{articleId}/articleComments/{articleCommentId}"})
    public ResponseEntity<ArticleCommentResponse> getArticleComment(
            @PathVariable Long articleCommentId) {

        ArticleCommentDto dto = articleCommentService.getArticleComment(articleCommentId);
        ArticleCommentResponse response = new ArticleCommentResponse(dto);

        return ResponseEntity.ok()
                .body(response);
    }

    @GetMapping("/articleComments")
    public ResponseEntity<ArticleCommentListResponse> getArticleComments(Pageable pageable) {

        Page<ArticleCommentDto> articleComments = articleCommentService.getArticleComments(pageable);
        ArticleCommentListResponse response = new ArticleCommentListResponse(articleComments);

        return ResponseEntity.ok()
                .body(response);
    }

    @GetMapping("/articles/{articleId}/articleComments")
    public ResponseEntity<ArticleCommentListResponse> getArticleCommentsFromArticle(
            @PathVariable Long articleId, Pageable pageable) {

        ArticleDto dto = new ArticleDto(articleId, pageable);
        Page<ArticleCommentDto> articleComments = articleCommentService.getArticleComments(dto);
        ArticleCommentListResponse response = new ArticleCommentListResponse(articleComments);

        return ResponseEntity.ok()
                .body(response);
    }

    @GetMapping("/members/{memberId}/articleComments")
    public ResponseEntity<ArticleCommentListResponse> getArticleCommentsFromMember(
            @PathVariable Long memberId, Pageable pageable) {

        MemberDto dto = new MemberDto(memberId, pageable);
        Page<ArticleCommentDto> articleComments = articleCommentService.getArticleComments(dto);
        ArticleCommentListResponse response = new ArticleCommentListResponse(articleComments);

        return ResponseEntity.ok()
                .body(response);
    }

    @PostMapping("/articles/{articleId}/articleComments")
    public ResponseEntity<Void> postNewArticleComment(
            @PathVariable Long articleId,
            @RequestBody @Validated SaveArticleCommentRequest saveArticleCommentRequest) {

        ArticleCommentDto dto = saveArticleCommentRequest.toDto(articleId);
        Long articleCommentId = articleCommentService.saveArticleComment(dto);

        return ResponseEntity.created(URI.create(createUrl(articleId, articleCommentId))).build();
    }

    @PatchMapping({"/articleComments/{articleCommentId}",
                   "articles/{articleId}/articleComments/{articleCommentId}"})
    public ResponseEntity<Void> updateArticleComment(
            @PathVariable Long articleCommentId,
            @RequestBody @Validated UpdateArticleCommentRequest articleCommentRequest) {

        ArticleCommentDto dto = articleCommentRequest.toDto(articleCommentId);
        articleCommentService.updateArticleComment(dto);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping({"/articleComments/{articleCommentId}",
                    "articles/{articleId}/articleComments/{articleCommentId}"})
    public ResponseEntity<Void> deleteArticleComment(
            @PathVariable Long articleCommentId) {

        ArticleCommentDto dto = ArticleCommentDto.builder()
                .articleCommentId(articleCommentId)
                .build();

        articleCommentService.softDeleteArticleComment(dto);

        return ResponseEntity.ok().build();
    }

    private String createUrl(Long articleId, Long articleCommentId) {
        return "/api/articles/" + articleId + "/articleComments/" + articleCommentId;
    }
}

