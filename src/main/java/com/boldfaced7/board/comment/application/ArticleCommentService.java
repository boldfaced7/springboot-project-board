package com.boldfaced7.board.comment.application;

import com.boldfaced7.board.common.auth.AuthInfoHolder;
import com.boldfaced7.board.article.domain.Article;
import com.boldfaced7.board.comment.domain.ArticleComment;
import com.boldfaced7.board.member.domain.Member;
import com.boldfaced7.board.article.application.ArticleDto;
import com.boldfaced7.board.common.CustomPage;
import com.boldfaced7.board.member.application.MemberDto;
import com.boldfaced7.board.common.auth.presentation.response.AuthResponse;
import com.boldfaced7.board.common.exception.exception.article.ArticleNotFoundException;
import com.boldfaced7.board.common.exception.exception.articlecomment.ArticleCommentNotFoundException;
import com.boldfaced7.board.common.exception.exception.auth.ForbiddenException;
import com.boldfaced7.board.common.exception.exception.member.MemberNotFoundException;
import com.boldfaced7.board.comment.infrastructure.ArticleCommentRepository;
import com.boldfaced7.board.article.infrastructure.ArticleRepository;
import com.boldfaced7.board.member.infrastructure.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleCommentService {

    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;
    private final MemberRepository memberRepository;

    @Cacheable(value = "articleComments", key = "#pageable.pageNumber")
    @Transactional(readOnly = true)
    public CustomPage<ArticleCommentDto> getArticleComments(Pageable pageable) {
        Page<ArticleComment> articleComments = articleCommentRepository.findAll(pageable);
        CustomPage<ArticleComment> converted = CustomPage.convert(articleComments);

        return converted.map(ArticleCommentDto::new);
    }

    @Cacheable(value = "articleCommentsOfArticle", key = "#articleDto.articleId", condition = "#articleDto.pageable.pageNumber == 0")
    @Transactional(readOnly = true)
    public CustomPage<ArticleCommentDto> getArticleComments(ArticleDto articleDto) {
        Article article = findArticleById(articleDto.getArticleId());
        Pageable pageable = articleDto.getPageable();

        Page<ArticleComment> articleComments = articleCommentRepository.findAllByArticle(article, pageable);
        CustomPage<ArticleComment> converted = CustomPage.convert(articleComments);

        return converted.map(ac -> new ArticleCommentDto(ac, article));
    }

    @Cacheable(value = "articleCommentsOfMember", key = "#memberDto.memberId", condition = "#memberDto.pageable.pageNumber == 0")
    @Transactional(readOnly = true)
    public CustomPage<ArticleCommentDto> getArticleComments(MemberDto memberDto) {
        authorizeMember(memberDto.getMemberId());
        Member member = findMemberById(memberDto.getMemberId());
        Pageable pageable = memberDto.getPageable();

        Page<ArticleComment> articleComments = articleCommentRepository.findAllByMember(member, pageable);
        CustomPage<ArticleComment> converted = CustomPage.convert(articleComments);

        return converted.map(ac -> new ArticleCommentDto(ac, member));
    }

    @Cacheable(value = "articleComment", key = "#articleCommentId")
    @Transactional(readOnly = true)
    public ArticleCommentDto getArticleComment(Long articleCommentId) {
        ArticleComment articleComment = findArticleCommentById(articleCommentId);
        return new ArticleCommentDto(articleComment);
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "article", key = "#dto.articleId", condition = "#dto.articleId != null"),
                    @CacheEvict(value = "articleComments", allEntries = true),
                    @CacheEvict(value = "articleCommentsOfArticle", key = "#dto.articleId", condition = "#dto.articleId != null"),
                    @CacheEvict(value = "articleCommentsOfMember", key = "#authInfoHolder.authInfo.memberId", condition = "#authInfoHolder != null")
            }
    )
    public Long saveArticleComment(ArticleCommentDto dto) {
        Long memberId = AuthInfoHolder.getAuthInfo().getMemberId();

        Member member = findMemberById(memberId);
        Article article = findArticleById(dto.getArticleId());

        return articleCommentRepository.save(dto.toEntityForSaving(article, member)).getId();
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "articleComment", key = "#dto.articleCommentId", condition = "#dto.articleCommentId != null"),
                    @CacheEvict(value = "articleComments", allEntries = true),
                    @CacheEvict(value = "articleCommentsOfArticle", key = "#dto.articleId", condition = "#dto.articleId != null"),
                    @CacheEvict(value = "articleCommentsOfMember", key = "#authInfoHolder.authInfo.memberId", condition = "#authInfoHolder != null")
            }
    )
    public void updateArticleComment(ArticleCommentDto dto) {
        ArticleComment articleComment = findArticleCommentById(dto.getArticleCommentId());
        authorizeAuthor(articleComment);
        articleComment.update(dto.toEntityForUpdating());
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "articleComment", key = "#dto.articleCommentId", condition = "#dto.articleCommentId != null"),
                    @CacheEvict(value = "articleComments", allEntries = true),
                    @CacheEvict(value = "articleCommentsOfArticle", key = "#dto.articleId", condition = "#dto.articleId != null"),
                    @CacheEvict(value = "articleCommentsOfMember", key = "#authInfoHolder.authInfo.memberId", condition = "#authInfoHolder != null")
            }
    )
    public void softDeleteArticleComment(ArticleCommentDto dto) {
        ArticleComment articleComment = findArticleCommentById(dto.getArticleCommentId());
        authorizeAuthor(articleComment);
        articleComment.deactivate();
    }

    public void hardDeleteArticleComment(ArticleCommentDto dto) {
        ArticleComment articleComment = findArticleCommentById(dto.getArticleCommentId());
        authorizeAuthor(articleComment);
        articleCommentRepository.delete(articleComment);
    }

    private ArticleComment findArticleCommentById(Long articleCommentId) {
        return articleCommentRepository.findById(articleCommentId)
                .orElseThrow(ArticleCommentNotFoundException::new);
    }

    private Article findArticleById(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(ArticleNotFoundException::new);
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
    }

    private void authorizeAuthor(ArticleComment articleComment) {
        AuthResponse authInfo = AuthInfoHolder.getAuthInfo();
        if (authInfo == null || !authInfo.getMemberId().equals(articleComment.getMember().getId())) {
            throw new ForbiddenException();
        }
    }

    private void authorizeMember(Long memberId) {
        AuthResponse authInfo = AuthInfoHolder.getAuthInfo();
        if (authInfo == null || !authInfo.getMemberId().equals(memberId)) {
            throw new ForbiddenException();
        }
    }
}
