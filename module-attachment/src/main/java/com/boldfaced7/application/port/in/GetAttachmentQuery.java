package com.boldfaced7.application.port.in;

import com.boldfaced7.domain.ResolvedAttachment;

public interface GetAttachmentQuery {
    ResolvedAttachment getAttachment(GetAttachmentCommand command);
}
