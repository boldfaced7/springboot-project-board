package com.boldfaced7.adapter.out.persistence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Builder
@AllArgsConstructor
public class MockedJpaRepository implements AttachmentJpaRepository {
    private final AttachmentJpaEntity bySave;
    private final AttachmentJpaEntity byId;
    private final List<AttachmentJpaEntity> byAll;
    private final List<AttachmentJpaEntity> byMemberId;
    private final List<AttachmentJpaEntity> byIdIn;
    private final List<AttachmentJpaEntity> byArticleId;
    private final List<AttachmentJpaEntity> bySaveAll;


    @Override
    public AttachmentJpaEntity save(AttachmentJpaEntity attachmentJpaEntity) {
        return bySave;
    }

    @Override
    public Optional<AttachmentJpaEntity> findById(Long id) {
        return Optional.ofNullable(byId);
    }

    @Override
    public List<AttachmentJpaEntity> findAll(Pageable pageable) {
        return byAll;
    }

    @Override
    public List<AttachmentJpaEntity> findAllByIdIn(List<Long> ids) {
        return byIdIn;
    }

    @Override
    public List<AttachmentJpaEntity> findAllByArticleId(String articleId, Pageable pageable) {
        return byArticleId;
    }

    @Override
    public List<AttachmentJpaEntity> findAllByArticleId(String articleId) {
        return byArticleId;
    }

    @Override
    public List<AttachmentJpaEntity> findAllByMemberId(String memberId, Pageable pageable) {
        return byMemberId;
    }

    @Override
    public List<AttachmentJpaEntity> saveAll(List<AttachmentJpaEntity> attachmentJpaEntities) {
        return bySaveAll;
    }
}