package com.boldfaced7.adapter.out.persistence;

import com.boldfaced7.domain.Article;

public class ArticleMapper {

    public static Article mapToDomain(ArticleJpaEntity jpaEntity) {
        return Article.generate(
                new Article.Id(jpaEntity.getId().toString()),
                new Article.MemberId(jpaEntity.getMemberId()),
                new Article.Title(jpaEntity.getTitle()),
                new Article.Content(jpaEntity.getContent()),
                jpaEntity.getCreatedAt(),
                jpaEntity.getUpdatedAt(),
                jpaEntity.getDeletedAt()
        );
    }

    public static ArticleJpaEntity mapToJpaEntity(Article domainEntity) {
        return new ArticleJpaEntity(
                Long.parseLong(domainEntity.getId()),
                domainEntity.getMemberId(),
                domainEntity.getTitle(),
                domainEntity.getContent(),
                domainEntity.getCreatedAt(),
                domainEntity.getUpdatedAt(),
                domainEntity.getDeletedAt()
        );
    }
}
