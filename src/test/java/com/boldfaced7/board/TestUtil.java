package com.boldfaced7.board;

import com.boldfaced7.board.domain.Article;
import com.boldfaced7.board.domain.ArticleComment;
import com.boldfaced7.board.domain.Attachment;
import com.boldfaced7.board.domain.Member;
import com.boldfaced7.board.dto.*;
import com.boldfaced7.board.dto.request.*;
import com.boldfaced7.board.dto.response.AuthResponse;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.ResultMatcher;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TestUtil {

    public static final Long ARTICLE_ID = 1L;
    public static final Long ARTICLE_COMMENT_ID = 1L;
    public static final Long MEMBER_ID = 1L;
    public static final String EMAIL = "boldfaced7@email.com";
    public static final String PASSWORD = "password";
    public static final String NICKNAME = "nickname";
    public static final String TITLE = "title";
    public static final String CONTENT = "content";
    public static final String AUTHOR = "author";
    public static final String API = "/api";
    public static final String ARTICLES = "articles";
    public static final String ARTICLE_COMMENTS = "articleComments";
    public static final String MEMBERS = "members";
    public static final String ATTACHMENTS = "attachments";

    public static final String PASSWORDS = "passwords";
    public static final String NICKNAMES = "nicknames";
    public static final String IMAGE = "image";
    public static final String JPG = ".jpg";
    public static final String UPLOADED_NAME = "uploadedName";
    public static final String STORED_NAME = "storedName";
    public static final String SIGNUP = "signUp";
    public static final String LOGIN = "login";
    public static final String LOGOUT = "logout";
    public static final String VALID = "valid";
    public static final String NOT_FOUND = "notFound";
    public static final String ARTICLE_NOT_FOUND = "articleNotFound";
    public static final String ARTICLE_COMMENT_NOT_FOUND = "articleCommentNotFound";
    public static final String MEMBER_NOT_FOUND = "MemberNotFound";
    public static final String FORBIDDEN = "forbidden";


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

    public static Attachment createAttachment() {
        return Attachment.builder()
                .uploadedName(UPLOADED_NAME)
                .storedName(STORED_NAME)
                .article(createArticle())
                .build();
    }

    public static Attachment createAttachment(Long id) {
        Attachment attachment = Attachment.builder()
                .uploadedName(UPLOADED_NAME)
                .storedName(STORED_NAME)
                .article(createArticle())
                .build();

        ReflectionTestUtils.setField(attachment, "id", id);
        return attachment;
    }

    public static MockMultipartFile createMultipartFile() {
        return new MockMultipartFile(IMAGE, UPLOADED_NAME+JPG, "image/jpeg", UPLOADED_NAME.getBytes());
    }

    /*
    DTO
     */

    public static CustomPage<ArticleDto> createArticleDtoCustomPage() {
        return new CustomPage<>(List.of(createArticleDto()), 0, 0, 1);
    }
    public static CustomPage<ArticleCommentDto> createArticleCommentDtoCustomPage() {
        return new CustomPage<>(List.of(createArticleCommentDto()), 0, 0, 1);
    }

    public static CustomPage<MemberDto> createMemberDtoCustomPage() {
        return new CustomPage<>(List.of(createMemberDto()), 0, 0, 1);
    }

    public static MemberDto createMemberDto() {
        return MemberDto.builder()
                .memberId(MEMBER_ID)
                .email(EMAIL)
                .nickname(NICKNAME)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
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
                .attachmentNames(List.of(STORED_NAME))
                .attachmentUrls(List.of("/resources/attachments/" + STORED_NAME))
                .articleComments(createArticleCommentDtoCustomPage())
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
    ResultMatcher
     */
    public static List<ResultMatcher> ok() {
        return List.of(status().isOk());
    }
    public static List<ResultMatcher> created() {
        return List.of(status().isCreated());
    }
    public static List<ResultMatcher> badRequest() {
        return List.of(status().isBadRequest());
    }
    public static List<ResultMatcher> unauthorized() {
        return List.of(status().isUnauthorized());
    }

    public static List<ResultMatcher> forbidden() {
        return List.of(status().isForbidden());
    }
    public static List<ResultMatcher> notFound() {
        return List.of(status().isNotFound());
    }
    public static List<ResultMatcher> contentTypeJson() {
        return List.of(content().contentType(MediaType.APPLICATION_JSON));
    }

    public static List<ResultMatcher> contentType(MediaType mediaType) {
        return List.of(content().contentType(mediaType));
    }

    public static List<ResultMatcher> exists(List<String> validationTargets, String path) {
        List<ResultMatcher> results = new ArrayList<>();

        for (String target : validationTargets) {
            results.add(jsonPath("$" + path + "." + target).exists());
        }
        return results;
    }

    public static List<ResultMatcher> doesNotExists(List<String> validationTargets, String path) {
        List<ResultMatcher> results = new ArrayList<>();

        for (String target : validationTargets) {
            results.add(jsonPath("$" + path + "." + target).doesNotExist());
        }
        return results;
    }

    /*
    Url
     */
    public static String signUpUrl() {
        return combine(API, SIGNUP);
    }

    public static String loginUrl() {
        return combine(API, LOGIN);
    }

    public static String logoutUrl() {
        return combine(API, LOGOUT);
    }

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

    public static String memberNicknameUrl(Long memberId) {
        return combine(memberUrl(memberId), NICKNAMES);
    }
    public static String memberPasswordUrl(Long memberId) {
        return combine(memberUrl(memberId), PASSWORDS);
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

    public static String attachmentUrl() {
        return combine(API, ATTACHMENTS);
    }

    private static String combine(String Url, Long Id) {
        return Url + "/" + Id;
    }
    private static String combine(String frontUrl, String backUrl) {
        return frontUrl + "/" + backUrl;
    }
}
