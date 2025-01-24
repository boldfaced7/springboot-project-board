package com.boldfaced7.application.port.in;

import com.boldfaced7.domain.Attachment;

import java.util.List;

public interface SetAttachmentsArticleIdUseCase {
    List<Attachment> setArticleId(SetAttachmentsArticleIdCommand command);
}
