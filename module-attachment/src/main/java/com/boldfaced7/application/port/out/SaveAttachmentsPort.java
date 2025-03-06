package com.boldfaced7.application.port.out;

import com.boldfaced7.domain.Attachment;

import java.util.List;

public interface SaveAttachmentsPort {
    List<Attachment> saveAttachments(List<Attachment> attachments);
}
