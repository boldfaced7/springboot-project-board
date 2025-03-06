package com.boldfaced7.adapter.out.persistence;

import com.boldfaced7.domain.ArticleComment;

public class ArticleCommentMapper {

    public static ArticleComment mapToDomain(ArticleCommentJpaEntity jpaEntity) {
        return ArticleComment.generate(
                new ArticleComment.Id(jpaEntity.getId().toString()),
                new ArticleComment.ArticleId(jpaEntity.getArticleId()),
                new ArticleComment.MemberId(jpaEntity.getMemberId()),
                new ArticleComment.Content(jpaEntity.getContent()),
                jpaEntity.getCreatedAt(),
                jpaEntity.getUpdatedAt(),
                jpaEntity.getDeletedAt()
        );
    }

    public static ArticleCommentJpaEntity mapToJpaEntity(ArticleComment domainEntity) {
        return new ArticleCommentJpaEntity(
                Long.parseLong(domainEntity.getId()),
                domainEntity.getArticleId(),
                domainEntity.getMemberId(),
                domainEntity.getContent(),
                domainEntity.getCreatedAt(),
                domainEntity.getUpdatedAt(),
                domainEntity.getDeletedAt()
        );
    }
}
