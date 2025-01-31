package com.boldfaced7.application.service;

import com.boldfaced7.UseCase;
import com.boldfaced7.application.port.in.DeleteAttachmentCommand;
import com.boldfaced7.application.port.in.DeleteAttachmentUseCase;
import com.boldfaced7.application.port.out.DeleteAttachmentPort;
import com.boldfaced7.application.port.out.FindAttachmentPort;
import com.boldfaced7.domain.Attachment;
import com.boldfaced7.exception.attachment.AttachmentNotFoundException;
import com.boldfaced7.exception.auth.UnauthorizedException;
import lombok.RequiredArgsConstructor;

import static com.boldfaced7.domain.Attachment.Id;

@UseCase
@RequiredArgsConstructor
public class DeleteAttachmentService implements DeleteAttachmentUseCase {

    private final FindAttachmentPort findAttachmentPort;
    private final DeleteAttachmentPort deleteAttachmentPort;

    @Override
    public Attachment deleteAttachment(DeleteAttachmentCommand command) {
        Attachment attachment = getAttachment(command.attachmentId());
        throwIfUnauthorized(attachment, command.memberId());
        Attachment deleted = attachment.delete();
        return deleteAttachmentPort.delete(deleted);
    }

    private Attachment getAttachment(String attachmentId) {
        Id id = new Id(attachmentId);
        return findAttachmentPort.findById(id)
                .orElseThrow(AttachmentNotFoundException::new);
    }

    private void throwIfUnauthorized(Attachment attachment, String requiringMemberId) {
        if (!attachment.getMemberId().equals(requiringMemberId)) {
            throw new UnauthorizedException();
        }
    }
}