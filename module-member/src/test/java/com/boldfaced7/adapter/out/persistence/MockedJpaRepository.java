package com.boldfaced7.adapter.out.persistence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Builder
@AllArgsConstructor
public class MockedJpaRepository implements MemberJpaRepository {
    private final Function<MemberJpaEntity, MemberJpaEntity> save;
    private final Function<Pageable, List<MemberJpaEntity>> findAll;
    private final Function<Long, Optional<MemberJpaEntity>> findById;
    private final Function<String, Boolean> existsByEmail;
    private final Function<String, Boolean> existsByNickname;

    @Override
    public MemberJpaEntity save(MemberJpaEntity memberJpa) {
        return save.apply(memberJpa);
    }

    @Override
    public List<MemberJpaEntity> findAll(Pageable pageable) {
        return findAll.apply(pageable);
    }

    @Override
    public Optional<MemberJpaEntity> findById(Long id) {
        return findById.apply(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return existsByEmail.apply(email);
    }

    @Override
    public boolean existsByNickname(String nickname) {
        return existsByNickname.apply(nickname);
    }
}