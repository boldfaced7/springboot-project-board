package com.boldfaced7.board.comment.presentation;

import com.boldfaced7.board.comment.application.ArticleCommentDto;
import com.boldfaced7.board.article.application.ArticleDto;
import com.boldfaced7.board.common.CustomPage;
import com.boldfaced7.board.member.application.MemberDto;
import com.boldfaced7.board.comment.presentation.request.SaveArticleCommentRequest;
import com.boldfaced7.board.comment.presentation.request.UpdateArticleCommentRequest;
import com.boldfaced7.board.comment.presentation.response.ArticleCommentListResponse;
import com.boldfaced7.board.comment.presentation.response.ArticleCommentResponse;
import com.boldfaced7.board.comment.application.ArticleCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

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
    public ResponseEntity<ArticleCommentListResponse> getArticleComments(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        CustomPage<ArticleCommentDto> articleComments = articleCommentService.getArticleComments(pageable);
        ArticleCommentListResponse response = new ArticleCommentListResponse(articleComments);

        return ResponseEntity.ok()
                .body(response);
    }

    @GetMapping("/articles/{articleId}/articleComments")
    public ResponseEntity<ArticleCommentListResponse> getArticleCommentsFromArticle(
            @PathVariable Long articleId,
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        ArticleDto dto = new ArticleDto(articleId, pageable);
        CustomPage<ArticleCommentDto> articleComments = articleCommentService.getArticleComments(dto);
        ArticleCommentListResponse response = new ArticleCommentListResponse(articleComments);

        return ResponseEntity.ok()
                .body(response);
    }

    @GetMapping("/members/{memberId}/articleComments")
    public ResponseEntity<ArticleCommentListResponse> getArticleCommentsFromMember(
            @PathVariable Long memberId,
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        MemberDto dto = new MemberDto(memberId, pageable);
        CustomPage<ArticleCommentDto> articleComments = articleCommentService.getArticleComments(dto);
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

