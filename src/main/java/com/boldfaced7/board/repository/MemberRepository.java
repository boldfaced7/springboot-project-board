package com.boldfaced7.board.repository;

import com.boldfaced7.board.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    public Member save(Member member);

    @Query("select m from Member m where m.active = true")
    public Page<Member> findAll(Pageable pageable);

    @Query("select m from Member m where m.active = :active")
    public Page<Member> findAll(@Param("active") boolean active, Pageable pageable);

    @Query("select m from Member m where m.id = :id")
    public Optional<Member> findById(@Param("id") Long id);

    @Query("select m from Member m where m.email = :email")
    public Optional<Member> findByEmail(@Param("email") String email);

    public void delete(Member member);

    public boolean existsByEmail(String email);

    public boolean existsByNickname(String nickname);
}
