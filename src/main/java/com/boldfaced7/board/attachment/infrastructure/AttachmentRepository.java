package com.boldfaced7.board.attachment.infrastructure;

import com.boldfaced7.board.article.domain.Article;
import com.boldfaced7.board.attachment.domain.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    Attachment save(Attachment attachment);

    void delete(Attachment attachment);

    @Query("select a from Attachment a" +
            " join fetch a.article" +
            " where a.id = :id and a.active = true")
    Optional<Attachment> findById(@Param("id") Long id);

    @Query("select a from Attachment a" +
            " join fetch a.article" +
            " where a.storedName = :storedName and a.active = true")
    Optional<Attachment> findByStoredName(@Param("storedName") String storedName);

    @Query("select a from Attachment a" +
            " where a.article = :article and a.active = true")
    List<Attachment> findAllByArticle(@Param("article") Article article);

    @Query("select a from Attachment a" +
            " join fetch a.article" +
            " where a.active = true")
    List<Attachment> findAll();

    @Modifying
    @Transactional
    @Query("update Attachment a set a.article = :article where a.storedName in :storedNames")
    int updateAttachments(@Param("article") Article article, @Param("storedNames") List<String> storedNames);

    @Modifying
    @Transactional
    @Query("update Attachment a set a.active = false where a.article = :article")
    int deactivateAttachments(@Param("article") Article article);

    @Modifying
    @Transactional
    @Query("delete Attachment a where a.article = :article")
    int deleteAttachments(@Param("article") Article article);
}