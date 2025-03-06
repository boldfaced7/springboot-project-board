package com.boldfaced7.application.port.out;

import com.boldfaced7.domain.Attachment;

import java.util.List;

public interface DeleteAttachmentsPort {
    List<Attachment> deleteAttachments(List<Attachment> attachments);
}
