package com.boldfaced7.application.port.out;

import com.boldfaced7.domain.Attachment;

import java.util.List;

public interface ListAttachmentsByMemberIdPort {
    List<Attachment> listAttachmentsByMemberId(Attachment.MemberId memberId, int pageSize, int pageNumber);
}
