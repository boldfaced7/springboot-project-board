package com.boldfaced7.adapter.out.persistence;

import com.boldfaced7.PersistenceAdapter;
import com.boldfaced7.application.port.out.*;
import com.boldfaced7.domain.ArticleTicketEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static com.boldfaced7.adapter.out.persistence.ArticleTicketEventMapper.mapToDomain;
import static com.boldfaced7.adapter.out.persistence.ArticleTicketEventMapper.mapToJpaEntity;
import static com.boldfaced7.domain.ArticleTicketEvent.Id;

@PersistenceAdapter
@RequiredArgsConstructor
public class SpringDataArticleTicketEventEventPersistenceAdapter implements
        DeleteArticleTicketEventPort,
        FindArticleTicketEventPort,
        ListAllArticleTicketEventsPort,
        SaveArticleTicketEventPort,
        UpdateArticleTicketEventPort {

    private final ArticleTicketEventJpaRepository articleTicketEventJpaRepository;

    @Override
    public ArticleTicketEvent delete(ArticleTicketEvent articleTicketEvent) {
        return persist(articleTicketEvent);
    }

    @Override
    public Optional<ArticleTicketEvent> findById(Id id) {
        Long parsedId = Long.parseLong(id.value());
        return articleTicketEventJpaRepository.findById(parsedId)
                .map(ArticleTicketEventMapper::mapToDomain);
    }

    @Override
    public List<ArticleTicketEvent> listAll(int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return articleTicketEventJpaRepository.findAll(pageRequest)
                .stream()
                .map(ArticleTicketEventMapper::mapToDomain)
                .toList();
    }

    @Override
    public ArticleTicketEvent save(ArticleTicketEvent articleTicketEvent) {
        return persist(articleTicketEvent);
    }

    @Override
    public ArticleTicketEvent update(ArticleTicketEvent articleTicketEvent) {
        return persist(articleTicketEvent);
    }

    private ArticleTicketEvent persist(ArticleTicketEvent articleTicketEvent) {
        ArticleTicketEventJpaEntity source = mapToJpaEntity(articleTicketEvent);
        ArticleTicketEventJpaEntity persisted = articleTicketEventJpaRepository.save(source);
        return mapToDomain(persisted);
    }
}
