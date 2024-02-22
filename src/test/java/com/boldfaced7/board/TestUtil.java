package com.boldfaced7.board;

import com.boldfaced7.board.domain.Article;
import com.boldfaced7.board.domain.ArticleComment;
import com.boldfaced7.board.domain.Member;
import com.boldfaced7.board.dto.ArticleCommentDto;
import com.boldfaced7.board.dto.ArticleDto;
import com.boldfaced7.board.dto.AuthDto;
import com.boldfaced7.board.dto.MemberDto;
import com.boldfaced7.board.dto.request.ArticleCommentRequest;
import com.boldfaced7.board.dto.request.ArticleRequest;
import com.boldfaced7.board.dto.request.AuthRequest;
import com.boldfaced7.board.dto.request.MemberRequest;
import com.boldfaced7.board.dto.response.AuthResponse;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;

public class TestUtil {

    public final static Long ARTICLE_ID = 1L;
    public final static Long ARTICLE_COMMENT_ID = 1L;
    public final static Long MEMBER_ID = 1L;
    public final static String EMAIL = "boldfaced7@email.com";
    public final static String PASSWORD = "password";
    public final static String NICKNAME = "nickname";
    public final static String TITLE = "title";
    public final static String CONTENT = "content";
    public final static String AUTHOR = "author";
    public final static String API = "/api";
    public final static String ARTICLES = "articles";
    public final static String ARTICLE_COMMENTS = "articleComments";
    public final static String MEMBERS = "members";

    /*
    엔티티
     */

    public static Member createMember() {
        Member member = Member.builder()
                .password(PASSWORD)
                .email(EMAIL)
                .nickname(NICKNAME)
                .build();

        ReflectionTestUtils.setField(member, "id", MEMBER_ID);
        return member;
    }

    public static Article createArticle() {
        Article article = Article.builder()
                .member(createMember())
                .title(TITLE)
                .content(CONTENT)
                .build();

        ReflectionTestUtils.setField(article, "id", ARTICLE_ID);

        return article;
    }

    public static ArticleComment createArticleComment() {
        ArticleComment articleComment = ArticleComment.builder()
                .article(createArticle())
                .member(createMember())
                .content(CONTENT)
                .build();

        ReflectionTestUtils.setField(articleComment, "id", ARTICLE_COMMENT_ID);

        return articleComment;
    }

    /*
    DTO
     */

    public static MemberDto createRequestMemberDto() {
        return MemberDto.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .nickname(NICKNAME)
                .build();
    }

    public static MemberDto createRequestMemberDto(Long memberId) {
        return MemberDto.builder()
                .email(EMAIL)
                .memberId(memberId)
                .password(PASSWORD)
                .nickname(NICKNAME)
                .build();
    }
    public static ArticleDto createArticleDto() {
        return ArticleDto.builder()
                .articleId(ARTICLE_ID)
                .memberId(MEMBER_ID)
                .title(TITLE)
                .content(CONTENT)
                .author(AUTHOR)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .articleComments(List.of(createArticleCommentDto()))
                .build();
    }

    public static ArticleDto createArticleDto(Long articleId) {
        return ArticleDto.builder()
                .articleId(articleId)
                .build();
    }

    public static ArticleCommentDto createArticleCommentDto() {
        return ArticleCommentDto.builder()
                .articleCommentId(ARTICLE_COMMENT_ID)
                .articleId(ARTICLE_ID)
                .memberId(MEMBER_ID)
                .content(CONTENT)
                .author(AUTHOR)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
    }

    public static ArticleCommentDto createArticleCommentDto(Long articleCommentId) {
        return ArticleCommentDto.builder()
                .articleCommentId(articleCommentId)
                .build();
    }

    public static AuthDto createRequestAuthDto() {
        return AuthDto.builder()
                .memberId(MEMBER_ID)
                .email(EMAIL)
                .password(PASSWORD)
                .nickname(NICKNAME)
                .build();
    }
    public static AuthDto createResponseAuthDto() {
        return AuthDto.builder()
                .memberId(MEMBER_ID)
                .email(EMAIL)
                .nickname(NICKNAME)
                .build();
    }
    /*
    Request
     */

    public static ArticleRequest createArticleRequest() {
        return ArticleRequest.builder()
                .title(TITLE)
                .content(CONTENT)
                .author(AUTHOR)
                .build();
    }

    public static ArticleCommentRequest createArticleCommentRequest() {
        return ArticleCommentRequest.builder()
                .content(CONTENT)
                .author(AUTHOR)
                .build();
    }

    public static MemberRequest createMemberRequest() {
        return MemberRequest.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .nickname(NICKNAME)
                .build();
    }

    public static AuthRequest createAuthRequest() {
        return AuthRequest.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .build();
    }

    /*
    Response
     */
    public static AuthResponse createAuthResponse() {
        return AuthResponse.builder()
                .memberId(MEMBER_ID)
                .email(EMAIL)
                .nickname(NICKNAME)
                .build();
    }

    public static AuthResponse createAuthResponse(Long memberId) {
        return AuthResponse.builder()
                .memberId(memberId)
                .email(EMAIL)
                .nickname(NICKNAME)
                .build();
    }

    /*
    Url
     */

    public static String articleUrl() {
        return combine(API, ARTICLES);
    }

    public static String articleUrl(Long articleId) {
        return combine(articleUrl(), articleId);
    }

    public static String articleCommentUrl() {
        return combine(API, ARTICLE_COMMENTS);
    }

    public static String articleCommentUrl(Long articleCommentId) {
        return combine(articleCommentUrl(), articleCommentId);
    }

    public static String articleArticleCommentUrl(Long articleId) {
        return combine(articleUrl(articleId), ARTICLE_COMMENTS);
    }

    public static String articleArticleCommentUrl(Long articleId, Long articleCommentId) {
        return combine(articleArticleCommentUrl(articleId), articleCommentId);
    }

    public static String memberUrl() {
        return combine(API, MEMBERS);
    }

    public static String memberUrl(Long memberId) {
        return combine(memberUrl(), memberId);
    }

    public static String memberArticleUrl(Long memberId) {
        return combine(memberUrl(memberId), ARTICLES);
    }

    public static String memberArticleUrl(Long memberId, Long articleId) {
        return combine(memberArticleUrl(memberId), articleId);
    }

    public static String memberArticleCommentUrl(Long memberId) {
        return combine(memberUrl(memberId), ARTICLE_COMMENTS);
    }

    public static String memberArticleCommentUrl(Long memberId, Long articleCommentId) {
        return combine(memberUrl(memberId), articleCommentId);
    }

    private static String combine(String Url, Long Id) {
        return Url + "/" + Id;
    }
    private static String combine(String frontUrl, String backUrl) {
        return frontUrl + "/" + backUrl;
    }
}
