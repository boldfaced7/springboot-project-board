package com.boldfaced7.application.service;

import com.boldfaced7.Query;
import com.boldfaced7.application.port.in.ListArticleAttachmentsCommand;
import com.boldfaced7.application.port.in.ListArticleAttachmentsQuery;
import com.boldfaced7.application.port.in.ListResolvedAttachmentsHelper;
import com.boldfaced7.application.port.out.ListArticleAttachmentsPort;
import com.boldfaced7.domain.Attachment;
import com.boldfaced7.domain.ResolvedAttachment;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Query
@RequiredArgsConstructor
public class ListArticleAttachmentsService implements ListArticleAttachmentsQuery {

    private static final int PAGE_SIZE = 20;

    private final ListArticleAttachmentsPort listArticleAttachmentsPort;
    private final ListResolvedAttachmentsHelper listResolvedAttachmentsHelper;

    public List<ResolvedAttachment> listArticleAttachments(ListArticleAttachmentsCommand command) {
        List<Attachment> attachments = listArticleAttachments(command.articleId(), command.pageNumber());

        return listResolvedAttachmentsHelper
                .listResolvedAttachments(attachments);
    }

    private List<Attachment> listArticleAttachments(String articleId, int pageNumber) {
        Attachment.ArticleId target = new Attachment.ArticleId(articleId);

        return listArticleAttachmentsPort
                .listArticleAttachments(target, pageNumber, PAGE_SIZE);
    }
}
