package com.boldfaced7.application.service;

import com.boldfaced7.UseCase;
import com.boldfaced7.application.port.in.DeleteAttachmentsCommand;
import com.boldfaced7.application.port.in.DeleteAttachmentsUseCase;
import com.boldfaced7.application.port.out.DeleteAttachmentsPort;
import com.boldfaced7.application.port.out.ListAttachmentsByIdsPort;
import com.boldfaced7.domain.Attachment;
import com.boldfaced7.exception.auth.UnauthorizedException;
import lombok.RequiredArgsConstructor;

import java.util.List;

@UseCase
@RequiredArgsConstructor
public class DeleteAttachmentsService implements DeleteAttachmentsUseCase {

    private final ListAttachmentsByIdsPort listAttachmentsByIdsPort;
    private final DeleteAttachmentsPort deleteAttachmentsPort;

    @Override
    public List<Attachment> deleteAttachments(DeleteAttachmentsCommand command) {
        List<Attachment> attachments = getAttachments(command);
        throwIfUnauthorized(attachments, command.memberId());
        return deleteAll(attachments);
    }

    private List<Attachment> getAttachments(DeleteAttachmentsCommand command) {
        List<Attachment.Id> ids = command.attachmentIds().stream()
                .map(Attachment.Id::new)
                .toList();
        return listAttachmentsByIdsPort.listAttachmentsByIds(ids);
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
