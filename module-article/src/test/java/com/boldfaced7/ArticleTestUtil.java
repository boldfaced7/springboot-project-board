package com.boldfaced7;

import com.boldfaced7.adapter.out.persistence.ArticleJpaEntity;
import com.boldfaced7.domain.Article;
import com.boldfaced7.domain.ResolvedArticle;
import org.assertj.core.api.Assertions;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;

public class ArticleTestUtil {
    public final static String ID = "1";
    public final static String MEMBER_ID = "1";
    public final static String INVALID_MEMBER_ID = "2";
    public final static String EMAIL = "boldfaced7@naver.com";
    public final static String EMPTY_EMAIL = "";
    public final static String NICKNAME = "nickname";
    public final static String TITLE = "title";
    public final static String NEW_TITLE = "new title";
    public final static String CONTENT = "content";
    public final static String NEW_CONTENT = "new content";
    public final static String ATTACHMENT = "attachment";
    public final static List<String> ATTACHMENTS = List.of(ATTACHMENT);
    public final static int PAGE_NUMBER = 0;
    public final static int PAGE_SIZE = 20;
    public final static boolean VALID = true;

    public static Article article(
            String id,
            String memberId,
            String title,
            String content
    ) {
        return Article.generate(
                new Article.Id(id),
                new Article.MemberId(memberId),
                new Article.Title(title),
                new Article.Content(content),
                LocalDateTime.MIN,
                LocalDateTime.MIN,
                null
        );
    }

    public static ArticleJpaEntity jpaEntity(
            String id,
            String memberId,
            String title,
            String content
    ) {
        return new ArticleJpaEntity(
                Long.parseLong(id),
                memberId,
                title,
                content,
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
            ResolvedArticle result,
            String id,
            String memberId,
            String title,
            String content,
            String email,
            String nickname,
            List<String> attachmentUrls
    ) {
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(id);
        Assertions.assertThat(result.getMemberId()).isEqualTo(memberId);
        Assertions.assertThat(result.getTitle()).isEqualTo(title);
        Assertions.assertThat(result.getContent()).isEqualTo(content);
        Assertions.assertThat(result.getEmail()).isEqualTo(email);
        Assertions.assertThat(result.getNickname()).isEqualTo(nickname);
        Assertions.assertThat(result.getAttachmentUrls()).isEqualTo(attachmentUrls);
    }

    public static void assertThat(
            Article result,
            String id,
            String memberId,
            String title,
            String content
    ) {
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(id);
        Assertions.assertThat(result.getMemberId()).isEqualTo(memberId);
        Assertions.assertThat(result.getTitle()).isEqualTo(title);
        Assertions.assertThat(result.getContent()).isEqualTo(content);
    }
}
