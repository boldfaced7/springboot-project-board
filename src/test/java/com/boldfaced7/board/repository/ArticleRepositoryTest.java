package com.boldfaced7.board.repository;

import com.boldfaced7.board.TestUtil;
import com.boldfaced7.board.config.JpaConfig;
import com.boldfaced7.board.domain.Article;
import com.boldfaced7.board.domain.ArticleComment;
import com.boldfaced7.board.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static com.boldfaced7.board.TestUtil.*;
import static org.assertj.core.api.Assertions.*;

@DisplayName("ArticleRepository 테스트")
@Import(JpaConfig.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ArticleRepositoryTest {

    @Autowired private ArticleRepository articleRepository;
    @Autowired private MemberRepository memberRepository;

    @DisplayName("findAll()이 isActive가 true인 Article 객체를 반환하는지 확인")
    @Test
    void givenArticle_whenSelecting_thenWorksFine() {
        // Given
        Article article = createArticle();
        articleRepository.save(article);

        // When
        List<Article> articles = articleRepository.findAll();

        //Then
        assertThat(articles).hasSize(1);
        assertThat(articles.get(0).getId()).isEqualTo(article.getId());
        assertThat(articles.get(0).getTitle()).isEqualTo(article.getTitle());
        assertThat(articles.get(0).getContent()).isEqualTo(article.getContent());
        assertThat(articles.get(0).isActive()).isTrue();
    }

    @DisplayName("findAll()이 isActive가 false인 Article 객체를 반환하지 않는지 확인")
    @Test
    void givenDeletedArticle_whenSelecting_thenWorksFine() {
        // Given
        Article article = createArticle();
        articleRepository.save(article);
        article.deactivate();

        // When
        List<Article> articles = articleRepository.findAllByIsActiveIsTrue();

        //Then
        assertThat(articles).hasSize(0);
    }

    @DisplayName("findAllByMember()가 isActive가 true인 Article 객체를 반환하는지 확인")
    @Test
    void givenMemberAndArticle_whenSelecting_thenWorksFine() {
        //Given
        Member member = memberRepository.save(createMember());
        Article article = articleRepository.save(createArticle(member));

        // When
        List<Article> members = articleRepository.findAllByMember(member);

        // Then
        assertThat(members.get(0).isActive()).isTrue();
    }

    @DisplayName("findAllByMember()가 isActive가 false인 Article 객체를 반환하지 않는지 확인")
    @Test
    void givenMemberAndInactiveArticle_whenSelecting_thenWorksFine() {
        //Given
        Member member = memberRepository.save(createMember());
        Article article = articleRepository.save(createArticle(member));
        article.deactivate();

        // When
        List<Article> members = articleRepository.findAllByMember(member);

        // Then
        assertThat(members).isEmpty();
    }

    private Article createArticle() {
        return Article.builder()
                .title(TITLE)
                .content(CONTENT)
                .build();
    }

    private Article createArticle(Member member) {
        return Article.builder()
                .title(TITLE)
                .content(CONTENT)
                .member(member)
                .build();
    }

    private Member createMember() {
        return Member.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .nickname(NICKNAME)
                .build();
    }
}