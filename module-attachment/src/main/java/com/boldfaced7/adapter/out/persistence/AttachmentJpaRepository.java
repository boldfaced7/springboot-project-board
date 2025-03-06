package com.boldfaced7.adapter.out.persistence;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AttachmentJpaRepository extends Repository<AttachmentJpaEntity, Long> {
    AttachmentJpaEntity save(AttachmentJpaEntity attachmentJpaEntity);

    @Query("select a from AttachmentJpaEntity a" +
            " where a.id = :id" +
            " and a.deletedAt != null")
    Optional<AttachmentJpaEntity> findById(Long id);

    @Query("select a from AttachmentJpaEntity a" +
            " where a.deletedAt != null")
    List<AttachmentJpaEntity> findAll(Pageable pageable);

    @Query("select a from AttachmentJpaEntity a" +
            " where a.id In :ids" +
            " and a.deletedAt != null")
    List<AttachmentJpaEntity> findAllByIdIn(List<Long> ids);

    @Query("select a from AttachmentJpaEntity a" +
            " where a.articleId = :articleId" +
            " and a.deletedAt != null")
    List<AttachmentJpaEntity> findAllByArticleId(String articleId, Pageable pageable);

    @Query("select a from AttachmentJpaEntity a" +
            " where a.articleId = :articleId" +
            " and a.deletedAt != null")
    List<AttachmentJpaEntity> findAllByArticleId(String articleId);

    @Query("select a from AttachmentJpaEntity a" +
            " where a.memberId = :memberId" +
            " and a.deletedAt != null")
    List<AttachmentJpaEntity> findAllByMemberId(String memberId, Pageable pageable);

    List<AttachmentJpaEntity> saveAll(List<AttachmentJpaEntity> attachmentJpaEntities);
}
