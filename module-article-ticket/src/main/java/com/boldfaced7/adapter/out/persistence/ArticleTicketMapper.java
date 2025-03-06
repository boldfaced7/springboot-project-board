package com.boldfaced7.adapter.out.persistence;

import com.boldfaced7.domain.ArticleTicket;

public class ArticleTicketMapper {

    public static ArticleTicket mapToDomain(ArticleTicketJpaEntity jpaEntity) {
        return ArticleTicket.generate(
                new ArticleTicket.Id(jpaEntity.getId().toString()),
                new ArticleTicket.TicketEventId(jpaEntity.getTicketEventId()),
                new ArticleTicket.MemberId(jpaEntity.getMemberId()),
                jpaEntity.getIssuedAt(),
                jpaEntity.getUsedAt(),
                jpaEntity.getDeletedAt()
        );
    }

    public static ArticleTicketJpaEntity mapToJpaEntity(ArticleTicket domainEntity) {
        return new ArticleTicketJpaEntity(
                Long.parseLong(domainEntity.getId()),
                domainEntity.getTicketEventId(),
                domainEntity.getMemberId(),
                domainEntity.getIssuedAt(),
                domainEntity.getUsedAt(),
                domainEntity.getDeletedAt()
        );
    }
}
