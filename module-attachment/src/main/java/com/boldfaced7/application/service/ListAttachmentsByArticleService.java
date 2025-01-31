package com.boldfaced7.application.service;

import com.boldfaced7.Query;
import com.boldfaced7.application.port.in.ListAttachmentsByArticleCommand;
import com.boldfaced7.application.port.in.ListAttachmentsByArticleQuery;
import com.boldfaced7.application.port.in.ListResolvedAttachmentsHelper;
import com.boldfaced7.application.port.out.ListPagedAttachmentsByArticleIdPort;
import com.boldfaced7.domain.Attachment;
import com.boldfaced7.domain.ResolvedAttachment;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Query
@RequiredArgsConstructor
public class ListAttachmentsByArticleService implements ListAttachmentsByArticleQuery {

    private static final int PAGE_SIZE = 20;

    private final ListPagedAttachmentsByArticleIdPort listPagedAttachmentsByArticleIdPort;
    private final ListResolvedAttachmentsHelper listResolvedAttachmentsHelper;

    public List<ResolvedAttachment> listArticleAttachments(ListAttachmentsByArticleCommand command) {
        List<Attachment> attachments = listArticleAttachments(command.articleId(), command.pageNumber());

        return listResolvedAttachmentsHelper
                .listResolvedAttachments(attachments);
    }

    private List<Attachment> listArticleAttachments(String articleId, int pageNumber) {
        Attachment.ArticleId target = new Attachment.ArticleId(articleId);

        return listPagedAttachmentsByArticleIdPort
                .listByArticleId(target, pageNumber, PAGE_SIZE);
    }
}
