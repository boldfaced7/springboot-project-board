package com.boldfaced7.adapter.out.persistence;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface MemberJpaRepository extends Repository<MemberJpaEntity, Long> {
    MemberJpaEntity save(MemberJpaEntity memberJpaEntity);

    @Query("select m from MemberJpaEntity m" +
            " where m.deletedAt != null")
    List<MemberJpaEntity> findAll(Pageable pageable);


    @Query("select m from MemberJpaEntity m" +
            " where m.id = :id " +
            " and m.deletedAt != null")
    Optional<MemberJpaEntity> findById(Long id);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);
}
