package com.boldfaced7.application.service;

import com.boldfaced7.Query;
import com.boldfaced7.application.port.in.ListAllAttachmentsCommand;
import com.boldfaced7.application.port.in.ListAllAttachmentsQuery;
import com.boldfaced7.application.port.in.ListResolvedAttachmentsHelper;
import com.boldfaced7.application.port.out.ListAllAttachmentsPort;
import com.boldfaced7.domain.Attachment;
import com.boldfaced7.domain.ResolvedAttachment;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Query
@RequiredArgsConstructor
public class ListAllAttachmentsService implements ListAllAttachmentsQuery {

    private static final int PAGE_SIZE = 20;

    private final ListAllAttachmentsPort listAllAttachmentsPort;
    private final ListResolvedAttachmentsHelper listResolvedAttachmentsHelper;

    @Override
    public List<ResolvedAttachment> listAllAttachments(ListAllAttachmentsCommand command) {
        List<Attachment> attachments = listAllAttachmentsPort
                .listAllAttachments(command.pageNumber(), PAGE_SIZE);

        return listResolvedAttachmentsHelper
                .listResolvedAttachments(attachments);
    }
}
