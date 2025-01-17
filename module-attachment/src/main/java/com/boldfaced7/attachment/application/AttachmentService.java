package com.boldfaced7.attachment.application;

import com.boldfaced7.attachment.domain.Attachment;
import com.boldfaced7.attachment.infrastructure.AttachmentRepository;
import com.boldfaced7.attachment.infrastructure.filestore.FileStore;
import com.boldfaced7.common.auth.AuthInfoHolder;
import com.boldfaced7.article.domain.Article;
import com.boldfaced7.article.application.ArticleDto;
import com.boldfaced7.common.auth.presentation.response.AuthResponse;
import com.boldfaced7.common.exception.exception.article.ArticleNotFoundException;
import com.boldfaced7.common.exception.exception.attachment.AttachmentNotFoundException;
import com.boldfaced7.common.exception.exception.auth.ForbiddenException;
import com.boldfaced7.article.infrastructure.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AttachmentService {
    private final ArticleRepository articleRepository;
    private final AttachmentRepository attachmentRepository;
    private final FileStore fileStore;

    @Cacheable(value = "attachment", key = "#attachmentId")
    @Transactional(readOnly = true)
    public AttachmentDto getAttachment(Long attachmentId) {
        Attachment attachment = findAttachmentById(attachmentId);
        return new AttachmentDto(attachment);
    }

    @Transactional(readOnly = true)
    public List<AttachmentDto> getAttachments() {
        return attachmentRepository.findAll()
                .stream().map(AttachmentDto::new)
                .toList();
    }

    @Cacheable(value = "attachmentsOfArticle", key = "#articleDto.articleId")
    @Transactional(readOnly = true)
    public List<AttachmentDto> getAttachments(ArticleDto articleDto) {
        Article article = findArticleById(articleDto.getArticleId());
        return attachmentRepository.findAllByArticle(article)
                .stream().map(AttachmentDto::new)
                .toList();
    }

    public String saveAttachment(AttachmentDto dto) {
        return fileStore.storeFile(dto.getMultipartFile()).getStoredName();
    }

    public List<String> saveAttachments(List<AttachmentDto> dtos) {
        List<MultipartFile> multipartFiles = dtos.stream().map(AttachmentDto::getMultipartFile).toList();
        List<Attachment> attachments = fileStore.storeFiles(multipartFiles);
        return attachments.stream().map(Attachment::getStoredName).toList();
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "attachment", key = "#dto.attachmentId"),
                    @CacheEvict(value = "attachmentsOfArticle", key = "#dto.articleId")
            }
    )
    public void softDeleteAttachment(AttachmentDto dto) {
        Attachment attachment = findAttachmentById(dto.getAttachmentId());
        authorizeAuthor(attachment);
        attachment.deactivate();
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "attachment", key = "#dto.attachmentId"),
                    @CacheEvict(value = "attachmentsOfArticle", key = "#dto.articleId")
            }
    )
    public void hardDeleteAttachment(AttachmentDto dto) {
        Attachment attachment = findAttachmentById(dto.getAttachmentId());
        authorizeAuthor(attachment);
        fileStore.removeFile(attachment);
        attachmentRepository.delete(attachment);
    }

    private Attachment findAttachmentById(Long attachmentId) {
        return attachmentRepository.findById(attachmentId)
                .orElseThrow(AttachmentNotFoundException::new);
    }

    private Article findArticleById(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(ArticleNotFoundException::new);
    }

    private void authorizeAuthor(Attachment attachment) {
        AuthResponse authInfo = AuthInfoHolder.getAuthInfo();
        if (authInfo == null || !authInfo.getMemberId().equals(attachment.getArticle().getMember().getId())) {
            throw new ForbiddenException();
        }
    }
}
