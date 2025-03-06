package com.boldfaced7.adapter.out.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentJpaEntity {

    public static final int MAX_TITLE_LENGTH = 100;
    public static final int MAX_CONTENT_LENGTH = 10000;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attachment_id")
    private Long id;

    @Column(nullable = false)
    private String articleId;

    @Column(nullable = false)
    private String memberId;

    @Column(nullable = false)
    private String uploadedName;

    @Column(nullable = false)
    private String storedName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttachmentJpaEntity attachmentJpaEntity = (AttachmentJpaEntity) o;
        return Objects.equals(id, attachmentJpaEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
