package com.boldfaced7.board.service;

import com.boldfaced7.board.auth.AuthInfoHolder;
import com.boldfaced7.board.domain.Article;
import com.boldfaced7.board.domain.ArticleComment;
import com.boldfaced7.board.domain.Attachment;
import com.boldfaced7.board.domain.Member;
import com.boldfaced7.board.dto.*;
import com.boldfaced7.board.dto.response.AuthResponse;
import com.boldfaced7.board.error.exception.article.ArticleNotFoundException;
import com.boldfaced7.board.error.exception.auth.ForbiddenException;
import com.boldfaced7.board.error.exception.member.MemberNotFoundException;
import com.boldfaced7.board.repository.ArticleCommentRepository;
import com.boldfaced7.board.repository.ArticleRepository;
import com.boldfaced7.board.repository.AttachmentRepository;
import com.boldfaced7.board.repository.MemberRepository;
import com.boldfaced7.board.repository.filestore.FileStore;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final AttachmentRepository attachmentRepository;
    private final FileStore fileStore;

    @Cacheable(value = "article", key = "#dto.articleId", condition = "#dto.pageable.pageNumber == 0")
    @Transactional(readOnly = true)
    public ArticleDto getArticle(ArticleDto dto) {
        Article article = findArticleById(dto.getArticleId());

        CustomPage<ArticleCommentDto> articleComments = getArticleComments(article, dto.getPageable());
        List<String> attachmentUrls = getAttachmentUrls(article);

        return new ArticleDto(article, articleComments, attachmentUrls);
    }

    private CustomPage<ArticleCommentDto> getArticleComments(Article article, Pageable pageable) {
        Page<ArticleComment> articleComments = articleCommentRepository.findAllByArticle(article, pageable);
        CustomPage<ArticleComment> converted = CustomPage.convert(articleComments);

        return converted.map(ac -> new ArticleCommentDto(ac, article));
    }

    private List<String> getAttachmentUrls(Article article) {
        List<Attachment> attachments = attachmentRepository.findAllByArticle(article);
        return fileStore.getUrls(attachments);
    }

    @Cacheable(value = "articles", key = "#pageable.pageNumber")
    @Transactional(readOnly = true)
    public CustomPage<ArticleDto> getArticles(Pageable pageable) {
        Page<Article> articles = articleRepository.findAll(pageable);
        CustomPage<Article> converted = CustomPage.convert(articles);

        return converted.map(ArticleDto::new);
    }

    @Cacheable(value = "articlesOfMember", key = "#memberDto.memberId", condition = "#memberDto.pageable.pageNumber == 0")
    @Transactional(readOnly = true)
    public CustomPage<ArticleDto> getArticles(MemberDto memberDto) {
        Member member = findMemberById(memberDto.getMemberId());
        Page<Article> articles = articleRepository
                .findAllByMember(member, memberDto.getPageable());

        CustomPage<Article> converted = CustomPage.convert(articles);
        return converted.map(article -> new ArticleDto(article, member));
    }

    @Caching(evict = {
            @CacheEvict(value = "articles", allEntries = true),
            @CacheEvict(value = "articlesOfMember", key = "#dto.memberId")
    })
    public Long saveArticle(ArticleDto dto) {
        Long memberId = AuthInfoHolder.getAuthInfo().getMemberId();
        Member member = findMemberById(memberId);
        Article article = articleRepository.save(dto.toEntityForSaving(member));

        if (!dto.getAttachmentNames().isEmpty()) {
            attachmentRepository.updateAttachments(article, dto.getAttachmentNames());
        }

        return article.getId();
    }

    @Caching(evict = {
            @CacheEvict(value = "article", key = "#dto.articleId"),
            @CacheEvict(value = "articles", allEntries = true),
            @CacheEvict(value = "articlesOfMember", key = "#dto.memberId")
    })
    public void updateArticle(ArticleDto dto) {
        Article article = findArticleById(dto.getArticleId());
        authorizeAuthor(article);
        article.update(dto.toEntityForUpdating());

        if (!dto.getAttachmentNames().isEmpty()) {
            attachmentRepository.updateAttachments(article, dto.getAttachmentNames());
        }
    }

    @Caching(evict = {
            @CacheEvict(value = "article", key = "#dto.articleId"),
            @CacheEvict(value = "articles", allEntries = true),
            @CacheEvict(value = "articlesOfMember", key = "#dto.memberId")
    })
    public void softDeleteArticle(ArticleDto dto) {
        Article article = findArticleById(dto.getArticleId());
        authorizeAuthor(article);
        article.deactivate();
        attachmentRepository.deactivateAttachments(article);
    }

    public void hardDeleteArticle(ArticleDto dto) {
        Article article = findArticleById(dto.getArticleId());
        authorizeAuthor(article);
        articleRepository.delete(article);
        attachmentRepository.deleteAttachments(article);
    }

    private Article findArticleById(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(ArticleNotFoundException::new);
    }
    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
    }
    private void authorizeAuthor(Article article) {
        AuthResponse authInfo = AuthInfoHolder.getAuthInfo();
        if (authInfo == null || !authInfo.getMemberId().equals(article.getMember().getId())) {
            throw new ForbiddenException();
        }
    }
}
