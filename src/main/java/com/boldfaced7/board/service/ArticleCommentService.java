package com.boldfaced7.board.service;

import com.boldfaced7.board.auth.AuthInfoHolder;
import com.boldfaced7.board.domain.Article;
import com.boldfaced7.board.domain.ArticleComment;
import com.boldfaced7.board.domain.Member;
import com.boldfaced7.board.dto.ArticleCommentDto;
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
public class ArticleCommentService {

    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;
    private final MemberRepository memberRepository;

    public static final String NO_ARTICLE_MESSAGE = "게시글이 없습니다 - articleId: ";
    public static final String NO_ARTICLE_COMMENT_MESSAGE = "댓글이 없습니다 - articleCommentId: ";
    public static final String NO_MEMBER_MESSAGE = "회원을 찾을 수 없습니다 - memberId: ";


    @Transactional(readOnly = true)
    public List<ArticleCommentDto> getArticleComments() {
        return articleCommentRepository.findAll().stream()
                .map(ArticleCommentDto::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ArticleCommentDto> getArticleComments(ArticleDto articleDto) {
        Article article = findArticleById(articleDto.getArticleId());

        return articleCommentRepository.findAllByArticle(article).stream()
                .map(ArticleCommentDto::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ArticleCommentDto> getArticleComments(MemberDto memberDto) {
        Member member = findMemberById(memberDto.getMemberId());

        return articleCommentRepository.findAllByMember(member).stream()
                .map(ArticleCommentDto::new)
                .toList();
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
        ArticleComment articleComment = findArticleCommentByDto(dto);
        authorizeAuthor(articleComment);
        articleComment.update(dto.toEntityForUpdating());
    }

    public void softDeleteArticleComment(ArticleCommentDto dto) {
        ArticleComment articleComment = findArticleCommentByDto(dto);
        authorizeAuthor(articleComment);
        articleComment.deactivate();
    }

    public void hardDeleteArticleComment(ArticleCommentDto dto) {
        ArticleComment articleComment = findArticleCommentByDto(dto);
        authorizeAuthor(articleComment);
        articleCommentRepository.delete(articleComment);
    }

    private ArticleComment findArticleCommentById(Long articleCommentId) {
        return articleCommentRepository.findById(articleCommentId)
                .orElseThrow(() -> new EntityNotFoundException(NO_ARTICLE_COMMENT_MESSAGE + articleCommentId));
    }

    private ArticleComment findArticleCommentByDto(ArticleCommentDto articleCommentDto) {
        return findArticleCommentById(articleCommentDto.getArticleCommentId());
    }

    private Article findArticleById(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException(NO_ARTICLE_MESSAGE + articleId));
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException(NO_MEMBER_MESSAGE + memberId));
    }

    private void authorizeAuthor(ArticleComment articleComment) {
        AuthResponse authInfo = AuthInfoHolder.getAuthInfo();
        if (authInfo == null || !authInfo.getMemberId().equals(articleComment.getMember().getId())) {
            throw new EntityNotFoundException(NO_ARTICLE_COMMENT_MESSAGE + articleComment.getId());
        }
    }
}
