package com.boldfaced7.adapter.out.persistence;

import com.boldfaced7.IdParser;
import com.boldfaced7.PersistenceAdapter;
import com.boldfaced7.application.port.out.*;
import com.boldfaced7.domain.Attachment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.boldfaced7.domain.Attachment.*;

@PersistenceAdapter
@RequiredArgsConstructor
public class SpringDataAttachmentPersistenceAdapter implements
        DeleteAttachmentPort,
        DeleteAttachmentsPort,
        FindAttachmentPort,
        ListAllAttachmentsPort,
        ListAllAttachmentsByArticleIdPort,
        ListPagedAttachmentsByArticleIdPort,
        ListAttachmentsByIdsPort,
        ListAttachmentsByMemberIdPort,
        SaveAttachmentsPort,
        UpdateAttachmentsPort {

    private final AttachmentJpaRepository attachmentJpaRepository;

    @Override
    public Attachment delete(Attachment attachment) {
        return persist(attachment);
    }

    @Override
    public List<Attachment> deleteAttachments(List<Attachment> attachments) {
        return persistAll(attachments);
    }

    @Override
    public Optional<Attachment> findById(Id id) {
        Long parsedId = IdParser.parseLong(id.value());
        return attachmentJpaRepository.findById(parsedId)
                .map(AttachmentMapper::mapToDomain);
    }

    @Override
    public List<Attachment> listAllAttachments(int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        List<AttachmentJpaEntity> listed = attachmentJpaRepository.findAll(pageRequest);
        return AttachmentMapper.mapToDomainEntities(listed);
    }

    @Override
    public List<Attachment> listByArticleId(ArticleId articleId, int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        List<AttachmentJpaEntity> listed = attachmentJpaRepository
                .findAllByArticleId(articleId.value(), pageRequest);

        return AttachmentMapper.mapToDomainEntities(listed);
    }

    @Override
    public List<Attachment> listByArticleId(ArticleId articleId) {
        List<AttachmentJpaEntity> listed = attachmentJpaRepository
                .findAllByArticleId(articleId.value());

        return AttachmentMapper.mapToDomainEntities(listed);
    }


    @Override
    public List<Attachment> listAttachmentsByIds(List<Id> ids) {
        List<AttachmentJpaEntity> collected = ids.stream()
                .map(Id::value)
                .map(IdParser::parseLong)
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        attachmentJpaRepository::findAllByIdIn
                ));
        return AttachmentMapper.mapToDomainEntities(collected);
    }

    @Override
    public List<Attachment> listAttachmentsByMemberId(MemberId memberId, int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        List<AttachmentJpaEntity> listed = attachmentJpaRepository
                .findAllByMemberId(memberId.value(), pageRequest);

        return AttachmentMapper.mapToDomainEntities(listed);
    }

    @Override
    public List<Attachment> saveAttachments(List<Attachment> attachments) {
        return persistAll(attachments);
    }

    @Override
    public List<Attachment> updateAttachments(List<Attachment> attachments) {
        return persistAll(attachments);
    }

    public Attachment persist(Attachment attachment) {
        AttachmentJpaEntity jpaEntity = AttachmentMapper.mapToJpaEntity(attachment);
        AttachmentJpaEntity modified = attachmentJpaRepository.save(jpaEntity);
        return AttachmentMapper.mapToDomain(modified);
    }

    private List<Attachment> persistAll(List<Attachment> attachments) {
        List<AttachmentJpaEntity> jpaEntities = AttachmentMapper.mapToJpaEntities(attachments);
        List<AttachmentJpaEntity> persisted = attachmentJpaRepository.saveAll(jpaEntities);
        return AttachmentMapper.mapToDomainEntities(persisted);
    }
}
