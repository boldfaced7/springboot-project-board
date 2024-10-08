package com.boldfaced7.board.comment.infrastructure;

import com.boldfaced7.board.article.domain.Article;
import com.boldfaced7.board.comment.domain.ArticleComment;
import com.boldfaced7.board.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long> {
    public ArticleComment save(ArticleComment articleComment);

    public void delete(ArticleComment articleComment);

    @Query("select ac from ArticleComment ac" +
            " join fetch ac.article" +
            " join fetch ac.member" +
            " where ac.id = :id and ac.active = true")
    public Optional<ArticleComment> findById(@Param("id") Long id);

    @Query("select ac from ArticleComment ac" +
            " join fetch ac.article" +
            " join fetch ac.member" +
            " where ac.active = true")
    public Page<ArticleComment> findAll(Pageable pageable);

    @Query("select ac from ArticleComment ac" +
            " join fetch ac.member" +
            " where ac.article = :article" +
                " and ac.active = true")
    public Page<ArticleComment> findAllByArticle(@Param("article") Article article, Pageable pageable);

    @Query("select ac from ArticleComment ac" +
            " join fetch ac.article" +
            " where ac.member = :member" +
                " and ac.active = true")
    public Page<ArticleComment> findAllByMember(@Param("member") Member member, Pageable pageable);
}
