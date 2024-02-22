package com.boldfaced7.board.Controller;

import com.boldfaced7.board.dto.ArticleDto;
import com.boldfaced7.board.dto.MemberDto;
import com.boldfaced7.board.dto.response.ArticleListResponse;
import com.boldfaced7.board.service.ArticleService;
import com.boldfaced7.board.dto.request.ArticleRequest;
import com.boldfaced7.board.dto.response.ArticleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/articles")
    public ResponseEntity<ArticleListResponse> getArticles() {

        List<ArticleDto> articles = articleService.getArticles();
        ArticleListResponse response = new ArticleListResponse(articles);

        return ResponseEntity.ok()
                .body(response);
    }

    @GetMapping("/members/{memberId}/articles")
    public ResponseEntity<ArticleListResponse> getArticles(
            @PathVariable Long memberId) {

        MemberDto dto = new MemberDto(memberId);
        List<ArticleDto> articles = articleService.getArticles(dto);
        ArticleListResponse response = new ArticleListResponse(articles);

        return ResponseEntity.ok()
                .body(response);
    }

    @GetMapping("/articles/{articleId}")
    public ResponseEntity<ArticleResponse> getArticle(
            @PathVariable Long articleId) {

        ArticleDto dto = articleService.getArticle(articleId);
        ArticleResponse response = new ArticleResponse(dto);

        return ResponseEntity.ok()
                .body(response);
    }

    @PostMapping("/articles")
    private ResponseEntity<Void> postNewArticle(
            @RequestBody ArticleRequest articleRequest) {

        ArticleDto dto = articleRequest.toDto();
        Long articleId = articleService.saveArticle(dto);

        return ResponseEntity.created(URI.create(createUrl(articleId))).build();
    }

    @PatchMapping("/articles/{articleId}")
    public ResponseEntity<Void> updateArticle(
            @PathVariable Long articleId,
            @RequestBody ArticleRequest articleRequest) {

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
