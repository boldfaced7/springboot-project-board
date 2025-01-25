package com.boldfaced7.exception.attachment;

import com.boldfaced7.exception.ErrorCode;
import com.boldfaced7.exception.entity.NotFoundException;

public class AttachmentNotFoundException extends NotFoundException {
    public AttachmentNotFoundException() {
        super(ErrorCode.ATTACHMENT_NOT_FOUND);
    }
}
