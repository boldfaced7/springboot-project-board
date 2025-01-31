package com.boldfaced7.application.service;

import com.boldfaced7.UseCase;
import com.boldfaced7.application.port.in.DeleteAttachmentsByArticleCommand;
import com.boldfaced7.application.port.in.DeleteAttachmentsByArticleUseCase;
import com.boldfaced7.application.port.out.DeleteAttachmentsPort;
import com.boldfaced7.application.port.out.ListAllAttachmentsByArticleIdPort;
import com.boldfaced7.domain.Attachment;
import com.boldfaced7.exception.auth.UnauthorizedException;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.boldfaced7.domain.Attachment.ArticleId;

@UseCase
@RequiredArgsConstructor
public class DeleteAttachmentsByArticleService implements DeleteAttachmentsByArticleUseCase {

    private final ListAllAttachmentsByArticleIdPort listAllAttachmentsByArticleIdPort;
    private final DeleteAttachmentsPort deleteAttachmentsPort;

    @Override
    public List<Attachment> deleteAttachments(DeleteAttachmentsByArticleCommand command) {
        List<Attachment> attachments = getAttachments(command.articleId());
        throwIfUnauthorized(attachments, command.memberId());
        return deleteAll(attachments);
    }

    private List<Attachment> getAttachments(String articleId) {
        ArticleId target = new ArticleId(articleId);
        return listAllAttachmentsByArticleIdPort.listByArticleId(target);
    }

    private void throwIfUnauthorized(List<Attachment> attachments, String requiringMemberId) {
        attachments.stream()
                .map(Attachment::getMemberId)
                .filter(memberId -> !memberId.equals(requiringMemberId))
                .findAny()
                .ifPresent(memberId -> {throw new UnauthorizedException();});
    }

    private List<Attachment> deleteAll(List<Attachment> attachments) {
        List<Attachment> modified = attachments.stream()
                .map(Attachment::delete)
                .toList();

        return deleteAttachmentsPort.deleteAttachments(modified);
    }
}
