package com.boldfaced7.board.service;

import com.boldfaced7.board.auth.AuthInfoHolder;
import com.boldfaced7.board.domain.Article;
import com.boldfaced7.board.domain.ArticleComment;
import com.boldfaced7.board.domain.Attachment;
import com.boldfaced7.board.domain.Member;
import com.boldfaced7.board.dto.ArticleDto;
import com.boldfaced7.board.dto.AttachmentDto;
import com.boldfaced7.board.dto.MemberDto;
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
import org.springframework.beans.factory.annotation.Value;
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

    @Transactional(readOnly = true)
    public ArticleDto getArticle(ArticleDto dto) {
        Article article = findArticleById(dto.getArticleId());
        Page<ArticleComment> articleComments = findArticleCommentsByArticle(article, dto.getPageable());
        List<String> attachmentUrls = fileStore.getUrls(findAttachmentsByArticle(article));

        return new ArticleDto(article, articleComments, attachmentUrls);
    }

    @Transactional(readOnly = true)
    public Page<ArticleDto> getArticles(Pageable pageable) {
        return articleRepository.findAll(pageable).map(ArticleDto::new);
    }

    @Transactional(readOnly = true)
    public Page<ArticleDto> getArticles(MemberDto memberDto) {
        Member member = findMemberById(memberDto.getMemberId());
        Pageable pageable = memberDto.getPageable();

        return articleRepository.findAllByMember(member, pageable).map(ArticleDto::new);
    }

    public Long saveArticle(ArticleDto dto) {
        Long memberId = AuthInfoHolder.getAuthInfo().getMemberId();
        Member member = findMemberById(memberId);
        Article article = articleRepository.save(dto.toEntityForSaving(member));

        if (!dto.getAttachmentNames().isEmpty()) {
            attachmentRepository.updateAttachments(article, dto.getAttachmentNames());
        }

        return article.getId();
    }

    public void updateArticle(ArticleDto dto) {
        Article article = findArticleById(dto.getArticleId());
        authorizeAuthor(article);
        article.update(dto.toEntityForUpdating());

        if (!dto.getAttachmentNames().isEmpty()) {
            attachmentRepository.updateAttachments(article, dto.getAttachmentNames());
        }
    }

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
    private Page<ArticleComment> findArticleCommentsByArticle(Article article, Pageable pageable) {
        return articleCommentRepository.findAllByArticle(article, pageable);
    }
    private List<Attachment> findAttachmentsByArticle(Article article) {
        return attachmentRepository.findAllByArticle(article);
    }
    private void authorizeAuthor(Article article) {
        AuthResponse authInfo = AuthInfoHolder.getAuthInfo();
        if (authInfo == null || !authInfo.getMemberId().equals(article.getMember().getId())) {
            throw new ForbiddenException();
        }
    }
}
