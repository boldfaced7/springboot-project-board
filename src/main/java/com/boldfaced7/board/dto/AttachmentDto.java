package com.boldfaced7.board.dto;

import com.boldfaced7.board.domain.Article;
import com.boldfaced7.board.domain.Attachment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class AttachmentDto {
    private Long attachmentId;
    private Long articleId;
    private String uploadedName;
    private String storedName;
    private MultipartFile multipartFile;
    private LocalDateTime createdAt;

    public AttachmentDto(Attachment attachment) {
        this.attachmentId = attachment.getId();
        this.articleId = attachment.getArticle().getId();
        this.uploadedName = attachment.getUploadedName();
        this.storedName = attachment.getStoredName();
        this.createdAt = attachment.getCreatedAt();
    }
    public AttachmentDto(String storedName) {
        this.storedName = storedName;
    }

    public AttachmentDto(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }

    public Attachment toEntity(Article article) {
        return Attachment.builder()
                .article(article)
                .uploadedName(uploadedName)
                .build();
    }
}
