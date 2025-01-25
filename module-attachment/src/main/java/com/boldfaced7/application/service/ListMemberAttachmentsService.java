package com.boldfaced7.application.service;

import com.boldfaced7.Query;
import com.boldfaced7.application.port.in.ListMemberAttachmentsCommand;
import com.boldfaced7.application.port.in.ListMemberAttachmentsQuery;
import com.boldfaced7.application.port.in.ListResolvedAttachmentsHelper;
import com.boldfaced7.application.port.out.ListMemberAttachmentsPort;
import com.boldfaced7.domain.Attachment;
import com.boldfaced7.domain.ResolvedAttachment;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Query
@RequiredArgsConstructor
public class ListMemberAttachmentsService implements ListMemberAttachmentsQuery {

    private static final int PAGE_SIZE = 20;

    private final ListMemberAttachmentsPort listMemberAttachmentsPort;
    private final ListResolvedAttachmentsHelper listResolvedAttachmentsHelper;

    @Override
    public List<ResolvedAttachment> listMemberAttachments(ListMemberAttachmentsCommand command) {
        List<Attachment> attachments = listMemberAttachments(command.memberId(), command.pageNumber());
        return listResolvedAttachmentsHelper.listResolvedAttachments(attachments);
    }

    private List<Attachment> listMemberAttachments(String memberId, int pageNumber) {
        Attachment.MemberId target = new Attachment.MemberId(memberId);

        return listMemberAttachmentsPort
                .listMemberAttachments(target, pageNumber, PAGE_SIZE);
    }
}
