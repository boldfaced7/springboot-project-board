package com.boldfaced7;

import com.boldfaced7.adapter.out.persistence.ArticleCommentJpaEntity;
import com.boldfaced7.domain.ArticleComment;
import com.boldfaced7.domain.ResolvedArticleComment;
import org.assertj.core.api.Assertions;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

public class ArticleCommentTestUtil {
    public final static String ID = "1";
    public final static String ARTICLE_ID = "1";
    public final static String MEMBER_ID = "1";
    public final static String INVALID_MEMBER_ID = "2";
    public final static String NICKNAME = "nickname";
    public final static String CONTENT = "content";
    public final static String NEW_CONTENT = "new content";
    public final static int PAGE_NUMBER = 0;
    public final static int PAGE_SIZE = 20;
    public final static boolean VALID = true;

    public static ArticleComment articleComment(
            String id,
            String articleId,
            String memberId,
            String content
    ) {
        return ArticleComment.generate(
                new ArticleComment.Id(id),
                new ArticleComment.ArticleId(articleId),
                new ArticleComment.MemberId(memberId),
                new ArticleComment.Content(content),
                LocalDateTime.MIN,
                LocalDateTime.MIN,
                null
        );
    }

    public static ArticleCommentJpaEntity jpaEntity(
            String id,
            String articleId,
            String memberId,
            String content
    ) {
        return new ArticleCommentJpaEntity(
                Long.parseLong(id),
                articleId,
                memberId,
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
            ResolvedArticleComment result,
            String id,
            String articleId,
            String memberId,
            String content,
            String nickname
    ) {
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(id);
        Assertions.assertThat(result.getArticleId()).isEqualTo(articleId);
        Assertions.assertThat(result.getMemberId()).isEqualTo(memberId);
        Assertions.assertThat(result.getContent()).isEqualTo(content);
        Assertions.assertThat(result.getNickname()).isEqualTo(nickname);
    }

    public static void assertThat(
            ArticleComment result,
            String id,
            String articleId,
            String memberId,
            String content
    ) {
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(id);
        Assertions.assertThat(result.getArticleId()).isEqualTo(articleId);
        Assertions.assertThat(result.getMemberId()).isEqualTo(memberId);
        Assertions.assertThat(result.getContent()).isEqualTo(content);
    }
}
