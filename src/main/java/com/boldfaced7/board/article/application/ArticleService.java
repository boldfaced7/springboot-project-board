package com.boldfaced7.board.article.application;

import com.boldfaced7.board.article.infrastructure.ArticleRepository;
import com.boldfaced7.board.attachment.infrastructure.AttachmentRepository;
import com.boldfaced7.board.comment.application.ArticleCommentDto;
import com.boldfaced7.board.comment.infrastructure.ArticleCommentRepository;
import com.boldfaced7.board.common.CustomPage;
import com.boldfaced7.board.common.auth.AuthInfoHolder;
import com.boldfaced7.board.common.auth.presentation.response.AuthResponse;
import com.boldfaced7.board.common.exception.exception.article.ArticleNotFoundException;
import com.boldfaced7.board.common.exception.exception.articleticket.ArticleTicketNotFoundException;
import com.boldfaced7.board.common.exception.exception.auth.ForbiddenException;
import com.boldfaced7.board.common.exception.exception.member.MemberNotFoundException;
import com.boldfaced7.board.attachment.infrastructure.filestore.FileStore;
import com.boldfaced7.board.article.domain.Article;
import com.boldfaced7.board.attachment.domain.Attachment;
import com.boldfaced7.board.comment.domain.ArticleComment;
import com.boldfaced7.board.member.application.MemberDto;
import com.boldfaced7.board.member.domain.Member;
import com.boldfaced7.board.member.infrastructure.MemberRepository;
import com.boldfaced7.board.ticket.domain.ArticleTicket;
import com.boldfaced7.board.ticket.infrastructure.ArticleTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;
    private final MemberRepository memberRepository;
    private final AttachmentRepository attachmentRepository;
    private final ArticleTicketRepository articleTicketRepository;
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
            @CacheEvict(value = "articlesOfMember", key = "#dto.memberId", condition = "#dto.memberId != null")
    })
    public Long saveArticle(ArticleDto dto) {
        Long memberId = AuthInfoHolder.getAuthInfo().getMemberId();
        Member member = findMemberById(memberId);
        ArticleTicket articleTicket = findAvailableArticleTicket(member);
        Article article = articleRepository.save(dto.toEntityForSaving(member));

        if (!dto.getAttachmentNames().isEmpty()) {
            attachmentRepository.updateAttachments(article, dto.getAttachmentNames());
        }
        articleTicket.useTicket();
        return article.getId();
    }

    @Caching(evict = {
            @CacheEvict(value = "article", key = "#dto.articleId", condition = "#dto.articleId != null"),
            @CacheEvict(value = "articles", allEntries = true),
            @CacheEvict(value = "articlesOfMember", key = "#dto.memberId", condition = "#dto.memberId != null")
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
            @CacheEvict(value = "article", key = "#dto.articleId", condition = "#dto.articleId != null"),
            @CacheEvict(value = "articles", allEntries = true),
            @CacheEvict(value = "articlesOfMember", key = "#dto.memberId", condition = "#dto.memberId != null")
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

    private ArticleTicket findAvailableArticleTicket(Member member) {
        LocalDateTime today = LocalDate.now().atStartOfDay();
        return articleTicketRepository.findAvailable(member, today, today.plusDays(1)).stream().findFirst()
                .orElseThrow(ArticleTicketNotFoundException::new);
    }

    private void authorizeAuthor(Article article) {
        AuthResponse authInfo = AuthInfoHolder.getAuthInfo();
        if (authInfo == null || !authInfo.getMemberId().equals(article.getMember().getId())) {
            throw new ForbiddenException();
        }
    }
}
