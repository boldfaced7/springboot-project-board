package com.boldfaced7.board.service;

import com.boldfaced7.board.domain.Article;
import com.boldfaced7.board.domain.ArticleComment;
import com.boldfaced7.board.dto.ArticleCommentDto;
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
public class ArticleCommentService {

    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;

    private static final String NO_ARTICLE_MESSAGE = "게시글이 없습니다 - articleId: ";
    private static final String NO_ARTICLE_COMMENT_MESSAGE = "댓글이 없습니다 - articleCommentId: ";

    @Transactional(readOnly = true)
    public List<ArticleCommentDto> getArticleComments() {
        return articleCommentRepository.findAll().stream()
                .map(ArticleCommentDto::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ArticleCommentDto> getArticleComments(Long articleId) {
        Article article = findArticleById(articleId);

        return articleCommentRepository.findAllByArticle(article).stream()
                .map(ArticleCommentDto::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public ArticleCommentDto getArticleComment(Long articleCommentId) {
        ArticleComment articleComment = findArticleCommentById(articleCommentId);
        return new ArticleCommentDto(articleComment);
    }

    public Long saveArticleComment(ArticleCommentDto dto) {
        Article article = findArticleById(dto.getArticleId());
        return articleCommentRepository.save(dto.toEntity(article)).getId();
    }

    public void updateArticleComment(Long articleCommentId, ArticleCommentDto dto) {
        ArticleComment articleComment = findArticleCommentById(articleCommentId);
        articleComment.update(dto.toEntity());
    }

    public void softDeleteArticleComment(Long articleCommentId) {
        ArticleComment articleComment = findArticleCommentById(articleCommentId);
        articleComment.deactivate();
    }

    public void hardDeleteArticleComment(Long articleCommentId) {
        ArticleComment articleComment = findArticleCommentById(articleCommentId);
        articleCommentRepository.delete(articleComment);
    }

    private Article findArticleById(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException(NO_ARTICLE_MESSAGE + articleId));
    }

    private ArticleComment findArticleCommentById(Long articleCommentId) {
        return articleCommentRepository.findById(articleCommentId)
                .orElseThrow(() -> new EntityNotFoundException(NO_ARTICLE_COMMENT_MESSAGE + articleCommentId));
    }
}
