package com.boldfaced7.board.error.exception.attachment;

import com.boldfaced7.board.error.ErrorCode;
import com.boldfaced7.board.error.exception.entity.NotFoundException;

public class AttachmentNotFoundException extends NotFoundException {
    public AttachmentNotFoundException() {
        super(ErrorCode.ATTACHMENT_NOT_FOUND);
    }
}
