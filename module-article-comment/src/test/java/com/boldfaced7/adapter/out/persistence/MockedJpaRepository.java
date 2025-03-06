package com.boldfaced7.adapter.out.persistence;

import lombok.Builder;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Builder
public class MockedJpaRepository implements ArticleCommentJpaRepository {
    private final ArticleCommentJpaEntity saved;
    private final ArticleCommentJpaEntity byId;
    private final List<ArticleCommentJpaEntity> all;
    private final List<ArticleCommentJpaEntity> byArticleId;
    private final List<ArticleCommentJpaEntity> byMemberId;

    public MockedJpaRepository(
            ArticleCommentJpaEntity saved,
            ArticleCommentJpaEntity byId,
            List<ArticleCommentJpaEntity> all,
            List<ArticleCommentJpaEntity> byArticleId,
            List<ArticleCommentJpaEntity> byMemberId
    ) {
        this.saved = saved;
        this.byId = byId;
        this.all = all;
        this.byArticleId = byArticleId;
        this.byMemberId = byMemberId;
    }

    @Override
    public ArticleCommentJpaEntity save(ArticleCommentJpaEntity articleCommentJpaEntity) {
        return saved;
    }

    @Override
    public void delete(ArticleCommentJpaEntity articleCommentJpaEntity) {
    }

    @Override
    public Optional<ArticleCommentJpaEntity> findById(Long id) {
        return Optional.ofNullable(byId);
    }

    @Override
    public List<ArticleCommentJpaEntity> findAll(Pageable pageable) {
        return all;
    }

    @Override
    public List<ArticleCommentJpaEntity> findAllByArticleId(String articleId, Pageable pageable) {
        return byArticleId;
    }

    @Override
    public List<ArticleCommentJpaEntity> findAllByMemberId(String memberId, Pageable pageable) {
        return byMemberId;
    }
}