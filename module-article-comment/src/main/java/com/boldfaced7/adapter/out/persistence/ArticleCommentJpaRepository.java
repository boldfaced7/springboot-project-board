package com.boldfaced7.adapter.out.persistence;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface ArticleCommentJpaRepository extends Repository<ArticleCommentJpaEntity, Long> {
    ArticleCommentJpaEntity save(ArticleCommentJpaEntity articleCommentJpaEntity);

    void delete(ArticleCommentJpaEntity articleCommentJpaEntity);

    @Query("select ac from ArticleCommentJpaEntity ac" +
            " where ac.id = :id" +
            " and ac.deletedAt != null")
    Optional<ArticleCommentJpaEntity> findById(Long id);

    @Query("select ac from ArticleCommentJpaEntity ac" +
            " where ac.deletedAt != null")
    List<ArticleCommentJpaEntity> findAll(Pageable pageable);

    @Query("select ac from ArticleCommentJpaEntity ac" +
            " where ac.articleId = :articleId" +
            " and ac.deletedAt != null")
    public List<ArticleCommentJpaEntity> findAllByArticleId(String articleId, Pageable pageable);


    @Query("select ac from ArticleCommentJpaEntity ac" +
            " where ac.memberId = :memberId" +
            " and ac.deletedAt != null")
    List<ArticleCommentJpaEntity> findAllByMemberId(String memberId, Pageable pageable);
}
