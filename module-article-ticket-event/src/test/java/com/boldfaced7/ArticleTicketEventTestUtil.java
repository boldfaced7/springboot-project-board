package com.boldfaced7;

import com.boldfaced7.adapter.out.persistence.ArticleTicketEventJpaEntity;
import com.boldfaced7.domain.ArticleTicketEvent;
import org.assertj.core.api.Assertions;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

public class ArticleTicketEventTestUtil {
    public final static String ID = "1";
    public final static String MEMBER_ID = "1";
    public final static String DISPLAY_NAME = "display name";
    public final static LocalDateTime EXPIRING_AT = LocalDateTime.MAX;
    public final static long ISSUE_LIMIT = 20;
    public final static int PAGE_NUMBER = 0;
    public final static int PAGE_SIZE = 20;

    public static ArticleTicketEvent articleTicketEvent(
            String id,
            String displayName,
            LocalDateTime expiringAt,
            Long issueLimit
    ) {
        return ArticleTicketEvent.generate(
                new ArticleTicketEvent.Id(id),
                new ArticleTicketEvent.DisplayName(displayName),
                new ArticleTicketEvent.ExpiringAt(expiringAt),
                new ArticleTicketEvent.IssueLimit(issueLimit),
                LocalDateTime.MIN,
                LocalDateTime.MIN,
                null
        );
    }

    public static ArticleTicketEventJpaEntity jpaEntity(
            String id,
            String displayName,
            LocalDateTime expiringAt,
            Long issueLimit
    ) {
        return new ArticleTicketEventJpaEntity(
                Long.parseLong(id),
                displayName,
                expiringAt,
                issueLimit,
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
            ArticleTicketEvent result,
            String id,
            String displayName,
            LocalDateTime expiringAt,
            Long issueLimit
    ) {
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(id);
        Assertions.assertThat(result.getDisplayName()).isEqualTo(displayName);
        Assertions.assertThat(result.getExpiringAt()).isEqualTo(expiringAt);
        Assertions.assertThat(result.getIssueLimit()).isEqualTo(issueLimit);
    }
}
