package com.boldfaced7.adapter.out.persistence;

import com.boldfaced7.domain.ArticleTicketEvent;

public class ArticleTicketEventMapper {

    public static ArticleTicketEvent mapToDomain(ArticleTicketEventJpaEntity jpaEntity) {
        return ArticleTicketEvent.generate(
                new ArticleTicketEvent.Id(jpaEntity.getId().toString()),
                new ArticleTicketEvent.DisplayName(jpaEntity.getDisplayName()),
                new ArticleTicketEvent.ExpiringAt(jpaEntity.getExpiringAt()),
                new ArticleTicketEvent.IssueLimit(jpaEntity.getIssueLimit()),
                jpaEntity.getCreatedAt(),
                jpaEntity.getUpdatedAt(),
                jpaEntity.getDeletedAt()
        );
    }

    public static ArticleTicketEventJpaEntity mapToJpaEntity(ArticleTicketEvent domainEntity) {
        return new ArticleTicketEventJpaEntity(
                Long.parseLong(domainEntity.getId()),
                domainEntity.getDisplayName(),
                domainEntity.getExpiringAt(),
                domainEntity.getIssueLimit(),
                domainEntity.getCreatedAt(),
                domainEntity.getUpdatedAt(),
                domainEntity.getDeletedAt()
        );
    }
}
