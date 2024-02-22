package com.boldfaced7.board.repository;

import com.boldfaced7.board.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    public Member save(Member member);

    @Query("select m from Member m")
    public List<Member> findAll();

    @Query("select m from Member m where m.isActive = :isActive")
    public List<Member> findAll(@Param("isActive") boolean isActive);

    @Query("select m from Member m where m.id = :id")
    public Optional<Member> findById(Long id);

    @Query("select m from Member m where m.email = :email")
    public Optional<Member> findByEmail(@Param("email") String email);

    public void delete(Member member);

    public boolean existsByEmail(String email);

    public boolean existsByNickname(String nickname);
}
