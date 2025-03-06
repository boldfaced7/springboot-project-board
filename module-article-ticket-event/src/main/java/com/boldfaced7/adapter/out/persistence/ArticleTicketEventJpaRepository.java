package com.boldfaced7.adapter.out.persistence;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface ArticleTicketEventJpaRepository extends Repository<ArticleTicketEventJpaEntity, Long> {
    ArticleTicketEventJpaEntity save(ArticleTicketEventJpaEntity articleTicketEventJpaEntity);

    @Query("select ate from ArticleTicketEventJpaEntity ate" +
            " where ate.id = :id" +
            " and ate.deletedAt != null")
    Optional<ArticleTicketEventJpaEntity> findById(Long id);

    @Query("select ate from ArticleTicketEventJpaEntity ate" +
            " where ate.deletedAt != null")
    List<ArticleTicketEventJpaEntity> findAll(Pageable pageable);
}
