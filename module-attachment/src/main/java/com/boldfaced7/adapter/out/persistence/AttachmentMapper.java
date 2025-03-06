package com.boldfaced7.adapter.out.persistence;

import com.boldfaced7.IdParser;
import com.boldfaced7.domain.Attachment;

import java.util.List;

public class AttachmentMapper {

    public static Attachment mapToDomain(AttachmentJpaEntity jpaEntity) {
        return Attachment.generate(
                new Attachment.Id(jpaEntity.getId().toString()),
                new Attachment.ArticleId(jpaEntity.getArticleId()),
                new Attachment.MemberId(jpaEntity.getMemberId()),
                new Attachment.UploadedName(jpaEntity.getUploadedName()),
                new Attachment.StoredName(jpaEntity.getStoredName()),
                jpaEntity.getCreatedAt(),
                jpaEntity.getUpdatedAt(),
                jpaEntity.getDeletedAt()
        );
    }

    public static AttachmentJpaEntity mapToJpaEntity(Attachment domainEntity) {
        return new AttachmentJpaEntity(
                IdParser.parseLong(domainEntity.getId()),
                domainEntity.getArticleId(),
                domainEntity.getMemberId(),
                domainEntity.getUploadedName(),
                domainEntity.getStoredName(),
                domainEntity.getCreatedAt(),
                domainEntity.getUpdatedAt(),
                domainEntity.getDeletedAt()
        );
    }

    public static List<Attachment> mapToDomainEntities(List<AttachmentJpaEntity> jpaEntities) {
        return jpaEntities.stream()
                .map(AttachmentMapper::mapToDomain)
                .toList();
    }

    public static List<AttachmentJpaEntity> mapToJpaEntities(List<Attachment> domainEntities) {
        return domainEntities.stream()
                .map(AttachmentMapper::mapToJpaEntity)
                .toList();
    }
}
