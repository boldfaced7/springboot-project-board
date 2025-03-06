package com.boldfaced7.adapter.out.persistence;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface ArticleTicketJpaRepository extends Repository<ArticleTicketJpaEntity, Long> {
    ArticleTicketJpaEntity save(ArticleTicketJpaEntity articleTicketJpaEntity);

    @Query("select at from ArticleTicketJpaEntity at" +
            " where at.id = :id" +
            " and at.deletedAt != null")
    Optional<ArticleTicketJpaEntity> findById(Long id);

    @Query("select at from ArticleTicketJpaEntity at" +
            " where at.deletedAt != null")
    List<ArticleTicketJpaEntity> findAll(Pageable pageable);

    @Query("select at from ArticleTicketJpaEntity at" +
            " where at.ticketEventId = :ticketEventId" +
            " and at.deletedAt != null")
    public List<ArticleTicketJpaEntity> findAllByTicketEventId(String ticketEventId, Pageable pageable);


    @Query("select at from ArticleTicketJpaEntity at" +
            " where at.memberId = :memberId" +
            " and at.deletedAt != null")
    List<ArticleTicketJpaEntity> findAllByMemberId(String memberId, Pageable pageable);
}
