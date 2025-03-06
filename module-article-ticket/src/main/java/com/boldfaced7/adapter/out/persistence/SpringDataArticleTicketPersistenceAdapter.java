package com.boldfaced7.adapter.out.persistence;

import com.boldfaced7.PersistenceAdapter;
import com.boldfaced7.application.port.out.*;
import com.boldfaced7.domain.ArticleTicket;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static com.boldfaced7.adapter.out.persistence.ArticleTicketMapper.mapToDomain;
import static com.boldfaced7.adapter.out.persistence.ArticleTicketMapper.mapToJpaEntity;
import static com.boldfaced7.domain.ArticleTicket.*;

@PersistenceAdapter
@RequiredArgsConstructor
public class SpringDataArticleTicketPersistenceAdapter implements
        DeleteArticleTicketPort,
        FindArticleTicketPort,
        ListAllArticleTicketsPort,
        ListArticleTicketsByEventPort,
        ListArticleTicketsByMemberPort,
        SaveArticleTicketPort,
        UpdateArticleTicketPort {

    private final ArticleTicketJpaRepository articleTicketJpaRepository;

    @Override
    public ArticleTicket delete(ArticleTicket articleTicket) {
        return persist(articleTicket);
    }

    @Override
    public Optional<ArticleTicket> findById(Id id) {
        Long parsedId = Long.parseLong(id.value());
        return articleTicketJpaRepository.findById(parsedId)
                .map(ArticleTicketMapper::mapToDomain);
    }

    @Override
    public List<ArticleTicket> listAll(int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return articleTicketJpaRepository.findAll(pageRequest)
                .stream()
                .map(ArticleTicketMapper::mapToDomain)
                .toList();
    }

    @Override
    public List<ArticleTicket> listByMember(MemberId memberId, int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return articleTicketJpaRepository
                .findAllByMemberId(memberId.value(), pageRequest)
                .stream()
                .map(ArticleTicketMapper::mapToDomain)
                .toList();
    }

    @Override
    public List<ArticleTicket> listByEvent(TicketEventId ticketEventId, int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return articleTicketJpaRepository
                .findAllByTicketEventId(ticketEventId.value(), pageRequest)
                .stream()
                .map(ArticleTicketMapper::mapToDomain)
                .toList();
    }

    @Override
    public ArticleTicket save(ArticleTicket articleTicket) {
        return persist(articleTicket);
    }

    @Override
    public ArticleTicket update(ArticleTicket articleTicket) {
        return persist(articleTicket);
    }

    private ArticleTicket persist(ArticleTicket articleTicket) {
        ArticleTicketJpaEntity source = mapToJpaEntity(articleTicket);
        ArticleTicketJpaEntity persisted = articleTicketJpaRepository.save(source);
        return mapToDomain(persisted);
    }
}
