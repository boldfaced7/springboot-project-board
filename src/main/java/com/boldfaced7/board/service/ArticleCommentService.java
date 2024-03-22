package com.boldfaced7.board.service;

import com.boldfaced7.board.auth.AuthInfoHolder;
import com.boldfaced7.board.domain.Article;
import com.boldfaced7.board.domain.ArticleComment;
import com.boldfaced7.board.domain.Member;
import com.boldfaced7.board.dto.ArticleCommentDto;
import com.boldfaced7.board.dto.ArticleDto;
import com.boldfaced7.board.dto.MemberDto;
import com.boldfaced7.board.dto.response.AuthResponse;
import com.boldfaced7.board.error.exception.article.ArticleNotFoundException;
import com.boldfaced7.board.error.exception.articlecomment.ArticleCommentNotFoundException;
import com.boldfaced7.board.error.exception.auth.ForbiddenException;
import com.boldfaced7.board.error.exception.member.MemberNotFoundException;
import com.boldfaced7.board.repository.ArticleCommentRepository;
import com.boldfaced7.board.repository.ArticleRepository;
import com.boldfaced7.board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
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

    @Transactional(readOnly = true)
    public Page<ArticleCommentDto> getArticleComments(Pageable pageable) {
        return articleCommentRepository.findAll(pageable).map(ArticleCommentDto::new);
    }

    @Transactional(readOnly = true)
    public Page<ArticleCommentDto> getArticleComments(ArticleDto articleDto) {
        Article article = findArticleById(articleDto.getArticleId());
        Pageable pageable = articleDto.getPageable();

        return articleCommentRepository.findAllByArticle(article, pageable).map(ArticleCommentDto::new);
    }

    @Transactional(readOnly = true)
    public Page<ArticleCommentDto> getArticleComments(MemberDto memberDto) {
        authorizeMember(memberDto.getMemberId());
        Member member = findMemberById(memberDto.getMemberId());
        Pageable pageable = memberDto.getPageable();

        return articleCommentRepository.findAllByMember(member, pageable).map(ArticleCommentDto::new);
    }

    @Transactional(readOnly = true)
    public ArticleCommentDto getArticleComment(Long articleCommentId) {
        ArticleComment articleComment = findArticleCommentById(articleCommentId);
        return new ArticleCommentDto(articleComment);
    }

    public Long saveArticleComment(ArticleCommentDto dto) {
        Long memberId = AuthInfoHolder.getAuthInfo().getMemberId();

        Member member = findMemberById(memberId);
        Article article = findArticleById(dto.getArticleId());

        return articleCommentRepository.save(dto.toEntityForSaving(article, member)).getId();
    }

    public void updateArticleComment(ArticleCommentDto dto) {
        ArticleComment articleComment = findArticleCommentById(dto.getArticleCommentId());
        authorizeAuthor(articleComment);
        articleComment.update(dto.toEntityForUpdating());
    }

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
