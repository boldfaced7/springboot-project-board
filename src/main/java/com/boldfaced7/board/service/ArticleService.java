package com.boldfaced7.board.service;

import com.boldfaced7.board.auth.AuthInfoHolder;
import com.boldfaced7.board.domain.Article;
import com.boldfaced7.board.domain.ArticleComment;
import com.boldfaced7.board.domain.Member;
import com.boldfaced7.board.dto.ArticleDto;
import com.boldfaced7.board.dto.MemberDto;
import com.boldfaced7.board.dto.response.AuthResponse;
import com.boldfaced7.board.repository.ArticleCommentRepository;
import com.boldfaced7.board.repository.ArticleRepository;
import com.boldfaced7.board.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;
    private final MemberRepository memberRepository;

    public static final String NO_ARTICLE_MESSAGE = "게시글이 없습니다 - articleId: ";
    public static final String NO_MEMBER_MESSAGE = "회원을 찾을 수 없습니다 - memberId: ";


    @Transactional(readOnly = true)
    public List<ArticleDto> getArticles() {
        return articleRepository.findAll().stream()
                .map(ArticleDto::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ArticleDto> getArticles(MemberDto memberDto) {
        Member member = findMemberById(memberDto.getMemberId());

        return articleRepository.findAllByMember(member).stream()
                .map(ArticleDto::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public ArticleDto getArticle(Long articleId) {
        Article article = findArticleById(articleId);
        List<ArticleComment> articleComments = findArticleCommentsByArticle(article);

        return new ArticleDto(article, articleComments);
    }

    public Long saveArticle(ArticleDto dto) {
        Long memberId = AuthInfoHolder.getAuthInfo().getMemberId();

        Member member = findMemberById(memberId);
        Article article = articleRepository.save(dto.toEntityForSaving(member));

        return article.getId();
    }

    public void updateArticle(ArticleDto dto) {
        Article article = findArticleByDto(dto);
        authorizeAuthor(article);
        article.update(dto.toEntityForUpdating());
    }

    public void softDeleteArticle(ArticleDto dto) {
        Article article = findArticleByDto(dto);
        authorizeAuthor(article);
        article.deactivate();
    }

    public void hardDeleteArticle(ArticleDto dto) {
        Article article = findArticleByDto(dto);
        authorizeAuthor(article);
        articleRepository.delete(article);
    }

    private Article findArticleById(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException(NO_ARTICLE_MESSAGE + articleId));
    }

    private Article findArticleByDto(ArticleDto dto) {
        return findArticleById(dto.getArticleId());
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException(NO_MEMBER_MESSAGE + memberId));
    }

    private List<ArticleComment> findArticleCommentsByArticle(Article article) {
        return articleCommentRepository.findAllByArticle(article);
    }

    private void authorizeAuthor(Article article) {
        AuthResponse authInfo = AuthInfoHolder.getAuthInfo();
        if (authInfo == null || !authInfo.getMemberId().equals(article.getMember().getId())) {
            throw new EntityNotFoundException(NO_ARTICLE_MESSAGE + article.getId());
        }
    }
}
