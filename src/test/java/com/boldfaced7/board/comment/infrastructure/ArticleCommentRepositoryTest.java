package com.boldfaced7.board.comment.infrastructure;

import com.boldfaced7.board.article.infrastructure.ArticleRepository;
import com.boldfaced7.board.common.config.JpaConfig;
import com.boldfaced7.board.article.domain.Article;
import com.boldfaced7.board.comment.domain.ArticleComment;
import com.boldfaced7.board.member.domain.Member;
import com.boldfaced7.board.member.infrastructure.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static com.boldfaced7.noboilerplate.TestUtil.*;
import static org.assertj.core.api.Assertions.*;

@DisplayName("ArticleCommentRepository 테스트")
@Import(JpaConfig.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ArticleCommentRepositoryTest {

    @Autowired private ArticleCommentRepository articleCommentRepository;
    @Autowired private ArticleRepository articleRepository;
    @Autowired private MemberRepository memberRepository;

    @DisplayName("findById()가 isActive가 true인 ArticleComment 객체를 반환하는지 확인")
    @Test
    void givenArticleComment_whenSelecting_thenWorksFine() {
        // Given
        ArticleComment articleComment = articleCommentRepository.save(createArticleComment());

        // When
        ArticleComment foundComment = articleCommentRepository.findById(articleComment.getId()).get();

        // Then
        assertThat(foundComment.isActive()).isTrue();
    }

    @DisplayName("findById()가 isActive가 false인 ArticleComment 객체를 반환하지 않는지 확인")
    @Test
    void givenDeletedArticleComment_whenSelecting_thenWorksFine() {
        // Given
        ArticleComment articleComment = articleCommentRepository.save(createArticleComment());
        articleComment.deactivate();

        // When
        Throwable t = catchThrowable(() ->
                articleCommentRepository.findById(articleComment.getId())
                .orElseThrow(EntityNotFoundException::new));

        // Then
        assertThat(t).isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("findAll()이 isActive가 true인 ArticleComment 객체를 반환하는지 확인")
    @Test
    void givenArticleComment_whenSelectingAll_thenWorksFine() {
        // Given
        ArticleComment articleComment = articleCommentRepository.save(createArticleComment());

        // When
        List<ArticleComment> articleComments = articleCommentRepository.findAll();

        // Then
        assertThat(articleComments.get(0).isActive()).isTrue();

    }
    @DisplayName("findAll()이 isActive가 false인 ArticleComment 객체를 반환하지 않는지 확인")
    @Test
    void givenDeletedArticleComment_whenSelectingAll_thenWorksFine() {
        // Given
        ArticleComment articleComment = articleCommentRepository.save(createArticleComment());
        articleComment.deactivate();

        // When
        List<ArticleComment> articleComments = articleCommentRepository.findAll();

        // Then
        assertThat(articleComments).isEmpty();
    }

    @DisplayName("findAllByArticle()이 isActive가 true인 ArticleComment 객체를 반환하는지 확인")
    @Test
    void givenArticleAndArticleComment_whenSelectingArticleCommentsOfArticle_thenWorksFine() {
        //Given
        Article article = articleRepository.save(createArticle());
        articleCommentRepository.save(createArticleComment(article));
        PageRequest pageable = PageRequest.of(0, 0);

        // When
        Page<ArticleComment> articleComments = articleCommentRepository.findAllByArticle(article, pageable);

        // Then
        assertThat(articleComments.getContent().get(0).isActive()).isTrue();
    }

    @DisplayName("findAllByArticle()이 isActive가 false인 ArticleComment 객체를 반환하지 않는지 확인")
    @Test
    void givenArticleAndDeletedArticleComment_whenSelectingArticleCommentsOfArticle_thenWorksFine() {
        //Given
        Article article = articleRepository.save(createArticle());
        ArticleComment articleComment = articleCommentRepository.save(createArticleComment(article));
        articleComment.deactivate();
        PageRequest pageable = PageRequest.of(0, 0);

        // When
        Page<ArticleComment> articleComments = articleCommentRepository.findAllByArticle(article, pageable);

        // Then
        assertThat(articleComments).isEmpty();
    }

    @DisplayName("findAllByMember()가 isActive가 true인 Article 객체를 반환하는지 확인")
    @Test
    void givenMemberAndArticle_whenSelecting_thenWorksFine() {
        //Given
        Member member = memberRepository.save(createMember());
        articleCommentRepository.save(createArticleComment(member));
        PageRequest pageable = PageRequest.of(0, 0);

        // When
        Page<ArticleComment> articleComments = articleCommentRepository.findAllByMember(member, pageable);

        // Then
        assertThat(articleComments.getContent().get(0).isActive()).isTrue();
    }

    @DisplayName("findAllByMember()가 isActive가 false인 Article 객체를 반환하지 않는지 확인")
    @Test
    void givenMemberAndInactiveArticle_whenSelecting_thenWorksFine() {
        //Given
        Member member = memberRepository.save(createMember());
        ArticleComment articleComment = articleCommentRepository.save(createArticleComment(member));
        articleComment.deactivate();
        PageRequest pageable = PageRequest.of(0, 0);

        // When
        Page<ArticleComment> articleComments = articleCommentRepository.findAllByMember(member, pageable);

        // Then
        assertThat(articleComments).isEmpty();
    }

    /*
    @DisplayName("")
    @Test
    void given_when_then() {
        //Given


        // When
        articleCommentRepository.

        // Then

    }
     */

    private ArticleComment createArticleComment() {
        return ArticleComment.builder()
                .content(CONTENT)
                .build();
    }

    private ArticleComment createArticleComment(Article article) {
        return ArticleComment.builder()
                .content(CONTENT)
                .article(article)
                .build();
    }

    private ArticleComment createArticleComment(Member member) {
        return ArticleComment.builder()
                .content(CONTENT)
                .member(member)
                .build();
    }

    private Article createArticle() {
        return Article.builder()
                .title(TITLE)
                .content(CONTENT)
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