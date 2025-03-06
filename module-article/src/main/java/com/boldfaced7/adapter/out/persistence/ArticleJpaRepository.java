package com.boldfaced7.adapter.out.persistence;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ArticleJpaRepository extends Repository<ArticleJpaEntity, Long> {
    ArticleJpaEntity save(ArticleJpaEntity articleJpaEntity);

    void delete(ArticleJpaEntity articleJpaEntity);

    @Query("select a from ArticleJpaEntity a" +
            " where a.id = :id" +
            " and a.deletedAt != null")
    Optional<ArticleJpaEntity> findById(@Param("id") Long id);

    @Query("select a from ArticleJpaEntity a" +
            " where a.deletedAt != null")
    List<ArticleJpaEntity> findAll(Pageable pageable);

    @Query("select a from ArticleJpaEntity a" +
            " where a.memberId = :memberId" +
            " and a.deletedAt != null")
    List<ArticleJpaEntity> findAllByMemberId(@Param("memberId") String memberId, Pageable pageable);
}
