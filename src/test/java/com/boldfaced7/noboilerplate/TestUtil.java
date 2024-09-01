package com.boldfaced7.noboilerplate;

import com.boldfaced7.board.article.application.ArticleDto;
import com.boldfaced7.board.attachment.application.AttachmentDto;
import com.boldfaced7.board.comment.application.ArticleCommentDto;
import com.boldfaced7.board.common.auth.application.AuthDto;
import com.boldfaced7.board.common.auth.presentation.response.AuthResponse;
import com.boldfaced7.board.article.domain.Article;
import com.boldfaced7.board.attachment.domain.Attachment;
import com.boldfaced7.board.comment.domain.ArticleComment;
import com.boldfaced7.board.common.CustomPage;
import com.boldfaced7.board.member.application.MemberDto;
import com.boldfaced7.board.member.domain.Member;
import com.boldfaced7.board.ticket.domain.ArticleTicket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.ResultMatcher;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TestUtil {

    public static final String EMAIL = "boldfaced7@email.com";
    public static final String PASSWORD = "password";
    public static final String NICKNAME = "nickname";
    public static final String TITLE = "title";
    public static final String CONTENT = "content";
    public static final String AUTHOR = "author";
    public static final String UPLOADED_NAME = "uploadedName";
    public static final String STORED_NAME = "storedName";
    public static final String STORED_URL = "storedUrl";
    public static final String NEW = "New";

    public static final String EXCEEDED_ARTICLE_TITLE = "a".repeat(Article.MAX_TITLE_LENGTH + 1);
    public static final String EXCEEDED_ARTICLE_CONTENT = "a".repeat(Article.MAX_CONTENT_LENGTH + 1);
    public static final String EXCEEDED_COMMENT_CONTENT = "a".repeat(ArticleComment.MAX_CONTENT_LENGTH + 1);
    public static final String EXCEEDED_EMAIL = "a".repeat(Member.MAX_EMAIL_LENGTH + 1);
    public static final String EXCEEDED_PASSWORD = "a".repeat(Member.MAX_PASSWORD_LENGTH + 1);
    public static final String EXCEEDED_NICKNAME = "a".repeat(Member.MAX_EMAIL_LENGTH + 1);

    public static final ResultMatcher OK = status().isOk();
    public static final ResultMatcher CREATED = status().isCreated();
    public static final ResultMatcher FOUND = status().isFound();
    public static final ResultMatcher UNAUTHORIZED = status().isUnauthorized();
    public static final ResultMatcher BAD_REQUEST = status().isBadRequest();
    public static final ResultMatcher NOT_FOUND = status().isNotFound();
    public static final ResultMatcher FORBIDDEN = status().isForbidden();

    public static final String API = "/api";
    public static final String ARTICLES = "articles";
    public static final String ARTICLE_COMMENTS = "articleComments";
    public static final String ARTICLE_TICKETS = "articleTickets";

    public static final String MEMBERS = "members";
    public static final String ATTACHMENTS = "attachments";
    public static final String PASSWORDS = "passwords";
    public static final String NICKNAMES = "nicknames";
    public static final String IMAGE = "image";
    public static final String JPG = ".jpg";
    public static final String SIGNUP = "signUp";
    public static final String LOGIN = "login";
    public static final String LOGOUT = "logout";

    /*
    엔티티
     */
    public static Member member() {
        Member member = Member.builder()
                .password(PASSWORD)
                .email(EMAIL)
                .nickname(NICKNAME)
                .build();

        ReflectionTestUtils.setField(member, "id", 1L);
        return member;
    }
    public static Member inactiveMember() {
        Member member = member();
        member.deactivate();
        return member;
    }
    public static Page<Member> members() {
        return new PageImpl<>(List.of(member()));
    }

    public static Article article() {
        Article article = Article.builder()
                .member(member())
                .title(TITLE)
                .content(CONTENT)
                .build();

        ReflectionTestUtils.setField(article, "id", 1L);
        return article;
    }
    public static Article article(Long memberId) {
        Article article = article();
        ReflectionTestUtils.setField(article.getMember(), "id", memberId);
        return article;
    }
    public static Page<Article> articles() {
        return new PageImpl<>(List.of(article()));
    }

    public static ArticleComment articleComment(Long id) {
        ArticleComment articleComment = ArticleComment.builder()
                .article(article())
                .member(member())
                .content(CONTENT)
                .build();

        ReflectionTestUtils.setField(articleComment, "id", id);

        return articleComment;
    }
    public static ArticleComment articleComment() {
        return articleComment(1L);
    }
    public static Page<ArticleComment> articleComments() {
        return new PageImpl<>(List.of(articleComment()));
    }

    public static ArticleTicket articleTicket(Long id) {
        ArticleTicket articleTicket = ArticleTicket.builder()
                .member(member())
                .build();
        ReflectionTestUtils.setField(articleTicket, "id", id);
        return articleTicket;
    }
    public static ArticleTicket articleTicket() {
        return articleTicket(1L);
    }
    public static Page<ArticleTicket> articleTickets() {
        return new PageImpl<>(List.of(articleTicket()));
    }

    public static Attachment attachment(Long id) {
        Attachment attachment = Attachment.builder()
                .uploadedName(UPLOADED_NAME)
                .storedName(STORED_NAME)
                .article(article())
                .build();

        ReflectionTestUtils.setField(attachment, "id", id);
        return attachment;
    }
    public static Attachment attachment() {
        return attachment(1L);
    }
    public static List<Attachment> attachments() {
        return List.of(attachment());
    }

    public static Pageable pageable() {
        return PageRequest.ofSize(20);
    }

    public static MockMultipartFile multipartFile() {
        return new MockMultipartFile(IMAGE, UPLOADED_NAME+JPG, "image/jpeg", UPLOADED_NAME.getBytes());
    }
    public static List<MockMultipartFile> multipartFiles() {
        return List.of(multipartFile());
    }

    /*
    DTO
     */
    public static MemberDto memberDto(Long id, String password, String nickname) {
        return MemberDto.builder()
                .memberId(id)
                .email(EMAIL)
                .password(password)
                .nickname(nickname)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
    }
    public static MemberDto memberDto() {
        return memberDto(1L, PASSWORD, NICKNAME);
    }
    public static MemberDto memberDto(Long id) {
        return memberDto(id, PASSWORD, NICKNAME);
    }
    public static MemberDto memberDto(String password, String nickname) {
        return memberDto(1L, password, nickname);
    }
    public static CustomPage<MemberDto> memberDtos() {
        return new CustomPage<>(List.of(memberDto()), 0, 0, 1);
    }

    public static ArticleDto articleDto(Long id, String title, String content) {
        return ArticleDto.builder()
                .articleId(id)
                .memberId(1L)
                .title(title)
                .content(content)
                .author(AUTHOR)
                .pageable(PageRequest.of(0,20))
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .attachmentNames(List.of(STORED_NAME))
                .attachmentUrls(List.of(STORED_URL))
                .articleComments(articleCommentDtos())
                .build();
    }
    public static ArticleDto articleDto() {
        return articleDto(1L, TITLE, CONTENT);
    }
    public static ArticleDto articleDto(Long id) {
        return articleDto(id, TITLE, CONTENT);
    }
    public static ArticleDto articleDto(String title, String content) {
        return articleDto(1L, title, content);
    }
    public static CustomPage<ArticleDto> articleDtos() {
        return new CustomPage<>(List.of(articleDto()), 0, 0, 1);
    }

    public static ArticleCommentDto articleCommentDto(String content) {
        return ArticleCommentDto.builder()
                .articleCommentId(1L)
                .articleId(1L)
                .memberId(1L)
                .content(content)
                .author(AUTHOR)
                .pageable(PageRequest.of(0,20))
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
    }
    public static ArticleCommentDto articleCommentDto() {
        return articleCommentDto(CONTENT);
    }
    public static CustomPage<ArticleCommentDto> articleCommentDtos() {
        return new CustomPage<>(List.of(articleCommentDto()), 0, 0, 1);
    }





    public static AttachmentDto attachmentDto(Long id) {
        return AttachmentDto.builder()
                .attachmentId(id)
                .articleId(1L)
                .uploadedName(UPLOADED_NAME)
                .storedName(STORED_NAME)
                .createdAt(LocalDateTime.now())
                .build();
    }
    public static AttachmentDto attachmentDto() {
        return attachmentDto(1L);
    }

    public static AuthDto responseAuthDto() {
        return AuthDto.builder()
                .memberId(1L)
                .email(EMAIL)
                .nickname(NICKNAME)
                .build();
    }
    public static AuthDto authDto() {
        return AuthDto.builder()
                .memberId(1L)
                .email(EMAIL)
                .password(PASSWORD)
                .nickname(NICKNAME)
                .build();
    }

    /*
    Response
     */
    public static AuthResponse authResponse(Long memberId) {
        return AuthResponse.builder()
                .memberId(memberId)
                .email(EMAIL)
                .nickname(NICKNAME)
                .build();
    }
    public static AuthResponse authResponse() {
        return authResponse(1L);
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
    public static String memberArticleCommentUrl(Long memberId) {
        return combine(memberUrl(memberId), ARTICLE_COMMENTS);
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

    public static String articleTicketUrl() {
        return combine(API, ARTICLE_TICKETS);
    }
    public static String articleTicketUrl(Long articleTicketId) {
        return combine(articleTicketUrl(), articleTicketId);
    }
    public static String memberArticleTicketUrl(Long memberId) {
        return combine(memberUrl(memberId), ARTICLE_TICKETS);
    }


}