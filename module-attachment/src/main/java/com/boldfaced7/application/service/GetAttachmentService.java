package com.boldfaced7.application.service;

import com.boldfaced7.Query;
import com.boldfaced7.application.port.in.GetAttachmentCommand;
import com.boldfaced7.application.port.in.GetAttachmentQuery;
import com.boldfaced7.application.port.out.*;
import com.boldfaced7.domain.Attachment;
import com.boldfaced7.domain.ResolvedAttachment;
import com.boldfaced7.exception.attachment.AttachmentNotFoundException;
import lombok.RequiredArgsConstructor;

@Query
@RequiredArgsConstructor
public class GetAttachmentService implements GetAttachmentQuery {

    private final FindAttachmentPort findAttachmentPort;
    private final GetAttachmentUrlPort getAttachmentUrlPort;

    @Override
    public ResolvedAttachment getAttachment(GetAttachmentCommand command) {
        Attachment attachment = getAttachment(command.attachmentId());
        String url = getUrl(attachment);

        return ResolvedAttachment.resolve(attachment, url);
    }

    private Attachment getAttachment(String attachmentId) {
        Attachment.Id id = new Attachment.Id(attachmentId);

        return findAttachmentPort.findById(id)
                .orElseThrow(AttachmentNotFoundException::new);
    }

    private String getUrl(Attachment attachment) {
        GetAttachmentUrlRequest request = new GetAttachmentUrlRequest(attachment.getStoredName());
        GetAttachmentUrlResponse response = getAttachmentUrlPort.getAttachmentUrl(request);
        if (!response.valid()) {
            throw new AttachmentNotFoundException();
        }
        return response.attachmentUrl();
    }
}
