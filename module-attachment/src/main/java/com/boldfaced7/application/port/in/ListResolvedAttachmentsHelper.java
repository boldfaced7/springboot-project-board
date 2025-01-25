package com.boldfaced7.application.port.in;

import com.boldfaced7.domain.Attachment;
import com.boldfaced7.domain.ResolvedAttachment;

import java.util.List;

public interface ListResolvedAttachmentsHelper {
    List<ResolvedAttachment> listResolvedAttachments(List<Attachment> attachments);
}
