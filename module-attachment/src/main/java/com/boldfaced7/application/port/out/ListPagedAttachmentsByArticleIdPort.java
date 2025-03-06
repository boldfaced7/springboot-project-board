package com.boldfaced7.application.port.out;

import com.boldfaced7.domain.Attachment;

import java.util.List;

public interface ListPagedAttachmentsByArticleIdPort {
    List<Attachment> listByArticleId(Attachment.ArticleId articleId, int pageSize, int pageNumber);
}
