package com.boldfaced7.board.service;

import com.boldfaced7.board.domain.Article;
import com.boldfaced7.board.domain.ArticleComment;
import com.boldfaced7.board.dto.ArticleDto;
import com.boldfaced7.board.repository.ArticleCommentRepository;
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
    private final ArticleCommentRepository articleCommentRepository;

    private static final String NO_ARTICLE_MESSAGE = "게시글이 없습니다 - articleId: ";


    @Transactional(readOnly = true)
    public List<ArticleDto> getArticles() {
        return articleRepository.findAll().stream()
                .map(ArticleDto::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public ArticleDto getArticle(Long articleId) {
        Article article = findArticleById(articleId);
        List<ArticleComment> articleComments = findArticleCommentsByArticle(article);

        return new ArticleDto(article, articleComments);
    }

    public Long saveArticle(ArticleDto dto) {
        Article article = articleRepository.save(dto.toEntity());
        return article.getId();
    }

    public void updateArticle(Long articleId, ArticleDto dto) {
        Article article = findArticleById(articleId);
        article.update(dto.toEntity());
    }

    public void softDeleteArticle(Long articleId) {
        Article article = findArticleById(articleId);
        article.deactivate();
    }

    public void hardDeleteArticle(Long articleId) {
        Article article = findArticleById(articleId);
        articleRepository.delete(article);
    }

    private Article findArticleById(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException(NO_ARTICLE_MESSAGE + articleId));
    }

    private List<ArticleComment> findArticleCommentsByArticle(Article article) {
        return articleCommentRepository.findAllByArticle(article);
    }
}
