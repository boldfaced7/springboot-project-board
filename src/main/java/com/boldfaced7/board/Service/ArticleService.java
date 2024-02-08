package com.boldfaced7.board.Service;

import com.boldfaced7.board.domain.Article;
import com.boldfaced7.board.dto.ArticleDto;
import com.boldfaced7.board.repository.ArticleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true)
    public List<ArticleDto> getArticles() {
        return articleRepository.findAll().stream()
                .map(ArticleDto::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public ArticleDto getArticle(Long articleId) {
        return articleRepository.findById(articleId)
                .map(ArticleDto::new)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - articleId: " + articleId));
    }

    public Long saveArticle(ArticleDto dto) {
        Article article = articleRepository.save(dto.toEntity());
        return article.getId();
    }

    public void updateArticle(Long articleId, ArticleDto dto) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - articleId: " + articleId));

        article.update(dto.toEntity());
    }

    public void softDeleteArticle(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - articleId: " + articleId));

        article.deactivate();
    }

    public void hardDeleteArticle(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - articleId: " + articleId));

        articleRepository.delete(article);
    }
}
