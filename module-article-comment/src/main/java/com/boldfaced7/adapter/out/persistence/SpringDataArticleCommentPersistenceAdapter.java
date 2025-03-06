package com.boldfaced7.adapter.out.persistence;

import com.boldfaced7.PersistenceAdapter;
import com.boldfaced7.application.port.out.*;
import com.boldfaced7.domain.ArticleComment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static com.boldfaced7.adapter.out.persistence.ArticleCommentMapper.mapToDomain;
import static com.boldfaced7.adapter.out.persistence.ArticleCommentMapper.mapToJpaEntity;

@PersistenceAdapter
@RequiredArgsConstructor
public class SpringDataArticleCommentPersistenceAdapter implements
        FindArticleCommentPort,
        ListArticleCommentsPort,
        ListAllArticleCommentsPort,
        ListMemberArticleCommentsPort,
        SaveArticleCommentPort,
        UpdateArticleCommentPort {

    private final ArticleCommentJpaRepository articleCommentJpaRepository;

    @Override
    public Optional<ArticleComment> findById(ArticleComment.Id id) {
        Long parsedId = Long.parseLong(id.value());
        return articleCommentJpaRepository.findById(parsedId)
                .map(ArticleCommentMapper::mapToDomain);
    }

    @Override
    public List<ArticleComment> listAll(int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return articleCommentJpaRepository.findAll(pageRequest)
                .stream()
                .map(ArticleCommentMapper::mapToDomain)
                .toList();
    }

    @Override
    public List<ArticleComment> listMemberArticleComments(ArticleComment.MemberId memberId, int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return articleCommentJpaRepository.findAllByMemberId(memberId.value(), pageRequest)
                .stream()
                .map(ArticleCommentMapper::mapToDomain)
                .toList();
    }

    @Override
    public ArticleComment save(ArticleComment articleComment) {
        ArticleCommentJpaEntity source = mapToJpaEntity(articleComment);
        ArticleCommentJpaEntity saved = articleCommentJpaRepository.save(source);
        return mapToDomain(saved);
    }

    @Override
    public ArticleComment update(ArticleComment articleComment) {
        ArticleCommentJpaEntity source = mapToJpaEntity(articleComment);
        ArticleCommentJpaEntity updated = articleCommentJpaRepository.save(source);
        return mapToDomain(updated);
    }

    @Override
    public List<ArticleComment> listArticleComments(ArticleComment.ArticleId articleId, int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return articleCommentJpaRepository
                .findAllByArticleId(articleId.value(), pageRequest)
                .stream()
                .map(ArticleCommentMapper::mapToDomain)
                .toList();
    }
}
