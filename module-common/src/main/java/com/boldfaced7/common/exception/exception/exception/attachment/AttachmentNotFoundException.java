package com.boldfaced7.common.exception.exception.exception.attachment;

import com.boldfaced7.common.exception.exception.ErrorCode;
import com.boldfaced7.common.exception.exception.exception.entity.NotFoundException;

public class AttachmentNotFoundException extends NotFoundException {
    public AttachmentNotFoundException() {
        super(ErrorCode.ATTACHMENT_NOT_FOUND);
    }
}
