package com.boldfaced7.adapter.out.persistence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Builder
@AllArgsConstructor
public class MockedJpaRepository implements ArticleTicketEventJpaRepository {
    private final ArticleTicketEventJpaEntity save;
    private final ArticleTicketEventJpaEntity findById;
    private final List<ArticleTicketEventJpaEntity> findAll;

    @Override
    public ArticleTicketEventJpaEntity save(ArticleTicketEventJpaEntity articleTicketEventJpaEntity) {
        return save;
    }

    @Override
    public Optional<ArticleTicketEventJpaEntity> findById(Long id) {
        return Optional.ofNullable(findById);
    }

    @Override
    public List<ArticleTicketEventJpaEntity> findAll(Pageable pageable) {
        return findAll;
    }
}