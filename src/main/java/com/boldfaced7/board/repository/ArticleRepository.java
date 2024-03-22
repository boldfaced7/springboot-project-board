package com.boldfaced7.board.repository;

import com.boldfaced7.board.domain.Article;
import com.boldfaced7.board.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    public Article save(Article article);

    public void delete(Article article);

    @Query("select a from Article a where a.id = :id and a.isActive = true")
    public Optional<Article> findById(@Param("id") Long id);

    @Query("select a from Article a where a.isActive = true")
    public Page<Article> findAll(Pageable pageable);

    @Query("select a from Article a where a.member = :member and a.isActive = true")
    public Page<Article> findAllByMember(@Param("member") Member member, Pageable pageable);

}
