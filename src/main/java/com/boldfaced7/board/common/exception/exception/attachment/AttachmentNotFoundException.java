package com.boldfaced7.board.common.exception.exception.attachment;

import com.boldfaced7.board.common.exception.ErrorCode;
import com.boldfaced7.board.common.exception.exception.entity.NotFoundException;

public class AttachmentNotFoundException extends NotFoundException {
    public AttachmentNotFoundException() {
        super(ErrorCode.ATTACHMENT_NOT_FOUND);
    }
}
