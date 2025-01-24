package com.boldfaced7.application.port.in;

import com.boldfaced7.domain.ResolvedAttachment;

import java.util.List;

public interface ListAllAttachmentsQuery {
    List<ResolvedAttachment> listAllAttachments(ListAllAttachmentsCommand command);
}
