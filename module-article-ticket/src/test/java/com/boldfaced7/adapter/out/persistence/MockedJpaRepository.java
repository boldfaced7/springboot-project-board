package com.boldfaced7.adapter.out.persistence;

import lombok.Builder;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Builder
public class MockedJpaRepository implements ArticleTicketJpaRepository {
    private final ArticleTicketJpaEntity save;
    private final ArticleTicketJpaEntity findById;
    private final List<ArticleTicketJpaEntity> findAll;
    private final List<ArticleTicketJpaEntity> findByEventId;
    private final List<ArticleTicketJpaEntity> findByMemberId;

    public MockedJpaRepository(
            ArticleTicketJpaEntity save,
            ArticleTicketJpaEntity findById,
            List<ArticleTicketJpaEntity> findAll,
            List<ArticleTicketJpaEntity> findByEventId,
            List<ArticleTicketJpaEntity> findByMemberId
    ) {
        this.save = save;
        this.findById = findById;
        this.findAll = findAll;
        this.findByEventId = findByEventId;
        this.findByMemberId = findByMemberId;
    }

    @Override
    public ArticleTicketJpaEntity save(ArticleTicketJpaEntity articleTicketJpaEntity) {
        return save;
    }

    @Override
    public Optional<ArticleTicketJpaEntity> findById(Long id) {
        return Optional.ofNullable(findById);
    }

    @Override
    public List<ArticleTicketJpaEntity> findAll(Pageable pageable) {
        return findAll;
    }

    @Override
    public List<ArticleTicketJpaEntity> findAllByTicketEventId(String ticketEventId, Pageable pageable) {
        return findByEventId;
    }

    @Override
    public List<ArticleTicketJpaEntity> findAllByMemberId(String memberId, Pageable pageable) {
        return findByMemberId;
    }
}