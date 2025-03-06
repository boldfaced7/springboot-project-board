package com.boldfaced7.adapter.out.persistence;

import lombok.Builder;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Builder
public class MockedJpaRepository implements ArticleJpaRepository {
    private final ArticleJpaEntity bySave;
    private final ArticleJpaEntity byId;
    private final List<ArticleJpaEntity> byFindAll;
    private final List<ArticleJpaEntity> byMemberId;

    public MockedJpaRepository(
            ArticleJpaEntity bySave,
            ArticleJpaEntity byId,
            List<ArticleJpaEntity> byFindAll,
            List<ArticleJpaEntity> byMemberId
    ) {
        this.bySave = bySave;
        this.byId = byId;
        this.byFindAll = byFindAll;
        this.byMemberId = byMemberId;
    }


    @Override
    public ArticleJpaEntity save(ArticleJpaEntity articleJpaEntity) {
        return bySave;
    }

    @Override
    public void delete(ArticleJpaEntity articleJpaEntity) {
    }

    @Override
    public Optional<ArticleJpaEntity> findById(Long id) {
        return Optional.ofNullable(byId);
    }

    @Override
    public List<ArticleJpaEntity> findAll(Pageable pageable) {
        return byFindAll;
    }

    @Override
    public List<ArticleJpaEntity> findAllByMemberId(String memberId, Pageable pageable) {
        return byMemberId;
    }
}