package com.boldfaced7.api.adapter.in.web;

import com.boldfaced7.article.application.ArticleDto;
import com.boldfaced7.article.application.ArticleService;
import com.boldfaced7.api.adapter.in.web.request.SaveArticleRequest;
import com.boldfaced7.api.adapter.in.web.request.UpdateArticleRequest;
import com.boldfaced7.api.adapter.in.web.response.ArticleListResponse;
import com.boldfaced7.api.adapter.in.web.response.ArticleResponse;
import com.boldfaced7.common.CustomPage;
import com.boldfaced7.member.application.MemberDto;
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
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/articles/{articleId}")
    public ResponseEntity<ArticleResponse> getArticle(
            @PathVariable Long articleId,
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable articleCommentPageable) {

        ArticleDto dto = articleService.getArticle(new ArticleDto(articleId, articleCommentPageable));
        ArticleResponse response = new ArticleResponse(dto);

        return ResponseEntity.ok()
                .body(response);
    }

    @GetMapping("/articles")
    public ResponseEntity<ArticleListResponse> getArticles(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        CustomPage<ArticleDto> articles = articleService.getArticles(pageable);
        ArticleListResponse response = new ArticleListResponse(articles);

        return ResponseEntity.ok()
                .body(response);
    }

    @GetMapping("/members/{memberId}/articles")
    public ResponseEntity<ArticleListResponse> getArticles(
            @PathVariable Long memberId,
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        MemberDto dto = new MemberDto(memberId, pageable);
        CustomPage<ArticleDto> articles = articleService.getArticles(dto);
        ArticleListResponse response = new ArticleListResponse(articles);

        return ResponseEntity.ok()
                .body(response);
    }

    @PostMapping("/articles")
    public ResponseEntity<Void> postNewArticle(
            @RequestBody @Validated SaveArticleRequest saveArticleRequest) {

        ArticleDto dto = saveArticleRequest.toDto();
        Long articleId = articleService.saveArticle(dto);

        return ResponseEntity.created(URI.create(createUrl(articleId))).build();
    }

    @PatchMapping("/articles/{articleId}")
    public ResponseEntity<Void> updateArticle(
            @PathVariable Long articleId,
            @RequestBody @Validated UpdateArticleRequest articleRequest) {

        ArticleDto dto = articleRequest.toDto(articleId);
        articleService.updateArticle(dto);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/articles/{articleId}")
    public ResponseEntity<Void> deleteArticle(
            @PathVariable Long articleId) {

        ArticleDto dto = ArticleDto.builder()
                .articleId(articleId)
                .build();

        articleService.softDeleteArticle(dto);

        return ResponseEntity.ok().build();
    }

    private String createUrl(Long articleId) {
        return "/api/articles/" + articleId;
    }

}
