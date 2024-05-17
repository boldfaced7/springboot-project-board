package com.boldfaced7.board.repository;

import com.boldfaced7.board.domain.ArticleTicket;
import com.boldfaced7.board.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ArticleTicketRepository extends Repository<ArticleTicket, Long> {
    ArticleTicket save(ArticleTicket articleTicket);
    void delete(ArticleTicket articleTicket);

    @Query("select a from ArticleTicket a" +
            " join fetch a.member" +
            " where a.id = :id")
    Optional<ArticleTicket> findById(@Param("id") Long id);

    @Query("select a from ArticleTicket a" +
            " where a.member = :member" +
            " and a.createdAt >= :from " +
            " and a.createdAt < :to" +
            " and a.used = false")
    List<ArticleTicket> findAvailable(@Param("member") Member member, @Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query("select a from ArticleTicket a" +
            " where a.member = :member")
    Page<ArticleTicket> findAllByMember(Pageable pageable, @Param("member") Member member);

    @Query("select a from ArticleTicket a" +
            " where a.createdAt >= :from" +
            " and a.createdAt < :to" +
            " order by a.createdAt")
    Page<ArticleTicket> findAllByDate(Pageable pageable, @Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query("select a.id from ArticleTicket a" +
            " where a.createdAt < :today" +
            " order by a.id DESC limit 1")
    Optional<Long> findCriteria(@Param("today") LocalDateTime today);

    @Query("select COUNT(a) from ArticleTicket a" +
            " where a.createdAt >= :from" +
            " and a.createdAt < :to" +
            " order by a.createdAt")
    int countArticleTicketByDate(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);
}
