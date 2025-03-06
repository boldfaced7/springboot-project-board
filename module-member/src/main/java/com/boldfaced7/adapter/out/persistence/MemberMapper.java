package com.boldfaced7.adapter.out.persistence;

import com.boldfaced7.IdParser;
import com.boldfaced7.domain.Member;

public class MemberMapper {

    public static Member mapToDomain(MemberJpaEntity jpaEntity) {
        return Member.generate(
                new Member.Id(jpaEntity.getId().toString()),
                new Member.Email(jpaEntity.getEmail()),
                new Member.Password(jpaEntity.getPassword()),
                new Member.Nickname(jpaEntity.getNickname()),
                jpaEntity.getCreatedAt(),
                jpaEntity.getUpdatedAt(),
                jpaEntity.getDeletedAt()
        );
    }

    public static MemberJpaEntity mapToJpaEntity(Member domainEntity) {
        return new MemberJpaEntity(
                IdParser.parseLong(domainEntity.getId()),
                domainEntity.getEmail(),
                domainEntity.getPassword(),
                domainEntity.getNickname(),
                domainEntity.getCreatedAt(),
                domainEntity.getUpdatedAt(),
                domainEntity.getDeletedAt()
        );
    }
}
