package com.boldfaced7;

import com.boldfaced7.adapter.out.persistence.AttachmentJpaEntity;
import com.boldfaced7.domain.Attachment;
import com.boldfaced7.domain.ResolvedAttachment;
import org.assertj.core.api.Assertions;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

public class AttachmentTestUtil {
    public final static String ID = "1";
    public final static String ARTICLE_ID = "1";
    public final static String MEMBER_ID = "1";
    public final static String WRONG_MEMBER_ID = "2";
    public final static String UPLOADED_NAME = "uploaded name";
    public final static String STORED_NAME = "stored name";
    public final static String URL = "url";
    public final static int PAGE_NUMBER = 0;
    public final static int PAGE_SIZE = 20;
    public final static boolean VALID = true;
    public final static boolean INVALID = false;

    public static Attachment attachment(
            String id,
            String articleId,
            String memberId,
            String uploadedName,
            String storedName
    ) {
        return Attachment.generate(
                new Attachment.Id(id),
                new Attachment.ArticleId(articleId),
                new Attachment.MemberId(memberId),
                new Attachment.UploadedName(uploadedName),
                new Attachment.StoredName(storedName),
                LocalDateTime.MIN,
                LocalDateTime.MIN,
                null
        );
    }

    public static AttachmentJpaEntity jpaEntity(
            String id,
            String articleId,
            String memberId,
            String uploadedName,
            String storedName
    ) {
        return new AttachmentJpaEntity(
                Long.parseLong(id),
                articleId,
                memberId,
                uploadedName,
                storedName,
                LocalDateTime.MIN,
                LocalDateTime.MIN,
                null
        );
    }

    public static <T> T setField(T target, String name, Object value) {
        ReflectionTestUtils.setField(target, name, value);
        return target;
    }

    public static void assertThat(
            ResolvedAttachment result,
            String id,
            String articleId,
            String memberId,
            String uploadedName,
            String storedName,
            String attachmentUrl
    ) {
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(id);
        Assertions.assertThat(result.getArticleId()).isEqualTo(articleId);
        Assertions.assertThat(result.getMemberId()).isEqualTo(memberId);
        Assertions.assertThat(result.getUploadedName()).isEqualTo(uploadedName);
        Assertions.assertThat(result.getStoredName()).isEqualTo(storedName);
        Assertions.assertThat(result.getAttachmentUrl()).isEqualTo(attachmentUrl);
    }

    public static void assertThat(
            Attachment result,
            String id,
            String articleId,
            String memberId,
            String uploadedName,
            String storedName
    ) {
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(id);
        Assertions.assertThat(result.getArticleId()).isEqualTo(articleId);
        Assertions.assertThat(result.getMemberId()).isEqualTo(memberId);
        Assertions.assertThat(result.getUploadedName()).isEqualTo(uploadedName);
        Assertions.assertThat(result.getStoredName()).isEqualTo(storedName);
    }
}
