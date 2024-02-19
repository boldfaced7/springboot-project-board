package com.boldfaced7.board.Controller;

import com.boldfaced7.board.dto.ArticleDto;
import com.boldfaced7.board.dto.response.ArticleListResponse;
import com.boldfaced7.board.service.ArticleService;
import com.boldfaced7.board.dto.request.ArticleRequest;
import com.boldfaced7.board.dto.response.ArticleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequestMapping("/api/articles")
@RestController
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final String urlTemplate = "/api/articles";

    @GetMapping
    public ResponseEntity<ArticleListResponse> getArticles() {
        List<ArticleResponse> articles = articleService.getArticles()
                .stream()
                .map(ArticleResponse::new)
                .toList();

        ArticleListResponse articleListResponse = new ArticleListResponse(articles);

        return ResponseEntity.ok()
                .body(articleListResponse);
    }

    @GetMapping("/{articleId}")
    public ResponseEntity<ArticleResponse> getArticle(@PathVariable Long articleId) {
        ArticleDto dto = articleService.getArticle(articleId);
        ArticleResponse response = new ArticleResponse(dto);

        return ResponseEntity.ok()
                .body(response);
    }

    @PostMapping
    private ResponseEntity<Void> postNewArticle(@RequestBody ArticleRequest articleRequest) {
        Long articleId = articleService.saveArticle(articleRequest.toDto());

        return ResponseEntity.created(URI.create(urlTemplate + "/" + articleId)).build();
    }

    @PatchMapping("/{articleId}")
    public ResponseEntity<Void> updateArticle(@PathVariable Long articleId, @RequestBody ArticleRequest articleRequest) {
        articleService.updateArticle(articleId, articleRequest.toDto());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{articleId}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long articleId) {
        articleService.softDeleteArticle(articleId);

        return ResponseEntity.ok().build();
    }
}
