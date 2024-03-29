package com.boldfaced7.board.service;

import com.boldfaced7.board.auth.AuthInfoHolder;
import com.boldfaced7.board.domain.Article;
import com.boldfaced7.board.domain.Attachment;
import com.boldfaced7.board.dto.ArticleDto;
import com.boldfaced7.board.dto.AttachmentDto;
import com.boldfaced7.board.dto.response.AuthResponse;
import com.boldfaced7.board.error.exception.article.ArticleNotFoundException;
import com.boldfaced7.board.error.exception.attachment.AttachmentNotFoundException;
import com.boldfaced7.board.error.exception.auth.ForbiddenException;
import com.boldfaced7.board.repository.ArticleRepository;
import com.boldfaced7.board.repository.AttachmentRepository;
import com.boldfaced7.board.repository.filestore.FileStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AttachmentService {
    private final ArticleRepository articleRepository;
    private final AttachmentRepository attachmentRepository;
    private final FileStore fileStore;

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
        return dtos.stream().map(this::saveAttachment).toList();
    }

    public void softDeleteAttachment(AttachmentDto dto) {
        Attachment attachment = findAttachmentById(dto.getAttachmentId());
        authorizeAuthor(attachment);
        attachment.deactivate();
    }

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
