package com.boldfaced7.application.port.out;

import com.boldfaced7.domain.Attachment;

public interface DeleteAttachmentPort {
    Attachment delete(Attachment attachment);
}
