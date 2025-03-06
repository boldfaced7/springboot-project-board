package com.boldfaced7.application.port.out;

import com.boldfaced7.domain.Attachment;

import java.util.List;

public interface ListAttachmentsByIdsPort {
    List<Attachment> listAttachmentsByIds(List<Attachment.Id> ids);
}
