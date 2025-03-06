package com.boldfaced7.adapter.out.persistence;

import com.boldfaced7.PersistenceAdapter;
import com.boldfaced7.application.port.out.*;
import com.boldfaced7.domain.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static com.boldfaced7.adapter.out.persistence.ArticleMapper.mapToDomain;
import static com.boldfaced7.adapter.out.persistence.ArticleMapper.mapToJpaEntity;

@PersistenceAdapter
@RequiredArgsConstructor
public class SpringDataArticlePersistenceAdapter implements
        FindArticlePort,
        ListPagedArticlesPort,
        ListArticlesByMemberPort,
        SaveArticlePort,
        UpdateArticlePort {

    private final ArticleJpaRepository articleJpaRepository;

    @Override
    public Optional<Article> findById(Article.Id id) {
        Long parsedId = Long.parseLong(id.value());
        return articleJpaRepository.findById(parsedId)
                .map(ArticleMapper::mapToDomain);
    }

    @Override
    public List<Article> listArticles(int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return articleJpaRepository.findAll(pageRequest)
                .stream()
                .map(ArticleMapper::mapToDomain)
                .toList();
    }

    @Override
    public List<Article> listByMember(Article.MemberId memberId, int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return articleJpaRepository.findAllByMemberId(memberId.value(), pageRequest)
                .stream()
                .map(ArticleMapper::mapToDomain)
                .toList();
    }

    @Override
    public Article save(Article article) {
        ArticleJpaEntity source = mapToJpaEntity(article);
        ArticleJpaEntity saved = articleJpaRepository.save(source);
        return mapToDomain(saved);
    }

    @Override
    public Article update(Article article) {
        ArticleJpaEntity source = mapToJpaEntity(article);
        ArticleJpaEntity updated = articleJpaRepository.save(source);
        return mapToDomain(updated);
    }
}
