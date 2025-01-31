package com.boldfaced7.application.port.in;

import com.boldfaced7.domain.Attachment;

public interface DeleteAttachmentUseCase {
    Attachment deleteAttachment(DeleteAttachmentCommand command);
}
