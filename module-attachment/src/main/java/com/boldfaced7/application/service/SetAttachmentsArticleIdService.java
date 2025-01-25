package com.boldfaced7.application.service;

import com.boldfaced7.UseCase;
import com.boldfaced7.application.port.in.SetAttachmentsArticleIdCommand;
import com.boldfaced7.application.port.in.SetAttachmentsArticleIdUseCase;
import com.boldfaced7.application.port.out.ListAttachmentsByIdsPort;
import com.boldfaced7.application.port.out.UpdateAttachmentsPort;
import com.boldfaced7.domain.Attachment;
import com.boldfaced7.exception.auth.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.boldfaced7.domain.Attachment.*;

@UseCase
@Transactional
@RequiredArgsConstructor
public class SetAttachmentsArticleIdService implements SetAttachmentsArticleIdUseCase {

    private final ListAttachmentsByIdsPort listAttachmentsByIdsPort;
    private final UpdateAttachmentsPort updateAttachmentsPort;

    @Override
    public List<Attachment> setArticleId(SetAttachmentsArticleIdCommand command) {
        List<Attachment> founds = getAll(command.attachmentIds());
        throwIfUnauthorized(founds, command.memberId());
        return setAll(command.articleId(), founds);
    }

    private void throwIfUnauthorized(List<Attachment> attachments, String requiringMemberId) {
        attachments.stream()
                .map(Attachment::getMemberId)
                .filter(memberId -> !memberId.equals(requiringMemberId))
                .findAny()
                .ifPresent(memberId -> {throw new UnauthorizedException();});
    }

    private List<Attachment> getAll(List<String> attachmentIds) {
        List<Id> targetAttachmentIds = attachmentIds.stream()
                .map(Id::new)
                .toList();

        return listAttachmentsByIdsPort
                .listAttachmentsByIds(targetAttachmentIds);
    }
    
    private List<Attachment> setAll(String articleId, List<Attachment> attachments) {
        ArticleId targetArticleId = new ArticleId(articleId);

        return attachments.stream()
                .map(attachment -> attachment.registerArticleId(targetArticleId))
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        updateAttachmentsPort::updateAttachments
                ));
    }
}
