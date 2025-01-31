package com.boldfaced7.application.service;

import com.boldfaced7.Query;
import com.boldfaced7.application.port.in.ListAttachmentsByMemberCommand;
import com.boldfaced7.application.port.in.ListAttachmentsByMemberQuery;
import com.boldfaced7.application.port.in.ListResolvedAttachmentsHelper;
import com.boldfaced7.application.port.out.ListAttachmentsByMemberIdPort;
import com.boldfaced7.domain.Attachment;
import com.boldfaced7.domain.ResolvedAttachment;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Query
@RequiredArgsConstructor
public class ListAttachmentsByMemberService implements ListAttachmentsByMemberQuery {

    private static final int PAGE_SIZE = 20;

    private final ListAttachmentsByMemberIdPort listAttachmentsByMemberIdPort;
    private final ListResolvedAttachmentsHelper listResolvedAttachmentsHelper;

    @Override
    public List<ResolvedAttachment> listMemberAttachments(ListAttachmentsByMemberCommand command) {
        List<Attachment> attachments = listMemberAttachments(command.memberId(), command.pageNumber());
        return listResolvedAttachmentsHelper.listResolvedAttachments(attachments);
    }

    private List<Attachment> listMemberAttachments(String memberId, int pageNumber) {
        Attachment.MemberId target = new Attachment.MemberId(memberId);

        return listAttachmentsByMemberIdPort
                .listAttachmentsByMemberId(target, pageNumber, PAGE_SIZE);
    }
}
