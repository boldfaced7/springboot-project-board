package com.boldfaced7.board.repository;

import com.boldfaced7.board.config.JpaConfig;
import com.boldfaced7.board.domain.Article;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("ArticleRepository 테스트")
@Import(JpaConfig.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ArticleRepositoryTest {

    @Autowired private ArticleRepository articleRepository;

    private Article article;

    @BeforeEach
    void setup() {
        article = Article.builder().content("test").title("test").build();
    }

    @DisplayName("findAll()이 isActive가 true인 Article 객체를 반환하는지 확인")
    @Test
    void givenArticle_whenSelecting_thenWorksFine() {
        // Given
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
z
    @DisplayName("findAll()이 isActive가 false인 Article 객체를 반환하지 않는지 확인")
    @Test
    void givenDeletedArticle_whenSelecting_thenWorksFine() {
        // Given
        articleRepository.save(article);
        article.deactivate();

        // When
        List<Article> articles = articleRepository.findAllByIsActiveIsTrue();

        //Then
        assertThat(articles).hasSize(0);
    }
}