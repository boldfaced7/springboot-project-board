package com.boldfaced7;

import com.boldfaced7.adapter.out.persistence.ArticleTicketJpaEntity;
import com.boldfaced7.domain.ArticleTicket;
import com.boldfaced7.domain.ResolvedArticleTicket;
import org.assertj.core.api.Assertions;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

public class ArticleTicketTestUtil {
    public final static String ID = "1";
    public final static String EVENT_ID = "1";
    public final static String MEMBER_ID = "1";
    public final static String INVALID_MEMBER_ID = "2";
    public final static String EMAIL = "boldfaced7@naver.com";
    public final static String NICKNAME = "nickname";
    public final static String DISPLAY_NAME = "display name";
    public final static LocalDateTime EXPIRING_AT = LocalDateTime.MAX;
    public final static long ISSUE_LIMIT = 20;
    public final static int PAGE_NUMBER = 0;
    public final static int PAGE_SIZE = 20;
    public final static boolean USED = true;
    public final static boolean UNUSED = false;
    public final static boolean VALID = true;
    public final static boolean INVALID = false;

    public static ArticleTicket articleTicket(
            String id,
            String eventId,
            String memberId,
            boolean used
    ) {
        return ArticleTicket.generate(
                new ArticleTicket.Id(id),
                new ArticleTicket.TicketEventId(eventId),
                new ArticleTicket.MemberId(memberId),
                LocalDateTime.MIN,
                (used) ? LocalDateTime.MIN : null,
                null
        );
    }

    public static ArticleTicketJpaEntity jpaEntity(
            String id,
            String eventId,
            String memberId,
            boolean used
    ) {
        return new ArticleTicketJpaEntity(
                Long.parseLong(id),
                eventId,
                memberId,
                LocalDateTime.MIN,
                (used) ? LocalDateTime.MIN : null,
                null
        );
    }

    public static <T> T setField(T target, String name, Object value) {
        ReflectionTestUtils.setField(target, name, value);
        return target;
    }

    public static void assertThat(
            ResolvedArticleTicket result,
            String id,
            String eventId,
            String memberId,
            String nickname,
            String displayName,
            LocalDateTime expiringAt,
            Long issueLimit,
            boolean used
    ) {
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(id);
        Assertions.assertThat(result.getTicketEventId()).isEqualTo(eventId);
        Assertions.assertThat(result.getMemberId()).isEqualTo(memberId);
        Assertions.assertThat(result.getOwnerNickname()).isEqualTo(nickname);
        Assertions.assertThat(result.getDisplayName()).isEqualTo(displayName);
        Assertions.assertThat(result.getExpiringAt()).isEqualTo(expiringAt);
        Assertions.assertThat(result.getIssueLimit()).isEqualTo(issueLimit);
        Assertions.assertThat(result.isUsed()).isEqualTo(used);
    }

    public static void assertThat(
            ArticleTicket result,
            String id,
            String eventId,
            String memberId
    ) {
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(id);
        Assertions.assertThat(result.getTicketEventId()).isEqualTo(eventId);
        Assertions.assertThat(result.getMemberId()).isEqualTo(memberId);
    }
}
