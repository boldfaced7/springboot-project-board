package com.boldfaced7.application.port.out;

import com.boldfaced7.domain.Attachment;

import java.util.Optional;

public interface FindAttachmentPort {
    Optional<Attachment> findById(Attachment.Id id);
}
