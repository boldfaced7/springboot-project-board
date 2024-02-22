package com.boldfaced7.board.repository;

import com.boldfaced7.board.domain.Article;
import com.boldfaced7.board.domain.ArticleComment;
import com.boldfaced7.board.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long> {
    public ArticleComment save(ArticleComment articleComment);

    public void delete(ArticleComment articleComment);

    @Query("select ac from ArticleComment ac where ac.id = :id and ac.isActive = true")
    public Optional<ArticleComment> findById(@Param("id") Long id);

    public Optional<ArticleComment> findByIdAndIsActiveIsTrue(Long id);

    @Query("select ac from ArticleComment ac where ac.isActive = true")
    public List<ArticleComment> findAll();

    public List<ArticleComment> findAllByIsActiveIsTrue();

    @Query("select ac from ArticleComment ac where ac.article = :article and ac.isActive = true")
    public List<ArticleComment> findAllByArticle(@Param("article") Article article);

    public List<ArticleComment> findAllByArticleAndIsActiveIsTrue(Article article);

    @Query("select ac from ArticleComment ac where ac.member = :member and ac.isActive = true")
    public List<ArticleComment> findAllByMember(@Param("member") Member member);

    public List<ArticleComment> findAllByMemberAndIsActiveIsTrue(Member member);

}
