package com.boldfaced7.board;

import com.boldfaced7.board.dto.*;
import com.boldfaced7.board.service.*;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ServiceMethod {
    public static final BiFunction<ArticleService, Long, ArticleDto> getArticle = ArticleService::getArticle;
    public static final Function<ArticleService, List<ArticleDto>> getArticles = ArticleService::getArticles;
    public static final BiFunction<ArticleService, MemberDto, List<ArticleDto>> getArticlesOfMember = ArticleService::getArticles;
    public static final BiFunction<ArticleService, ArticleDto, Long> saveArticle = ArticleService::saveArticle;
    public static final BiConsumer<ArticleService, ArticleDto> updateArticle = ArticleService::updateArticle;
    public static final BiConsumer<ArticleService, ArticleDto> softDeleteArticle = ArticleService::softDeleteArticle;

    public static final BiFunction<ArticleCommentService, Long, ArticleCommentDto> getArticleComment = ArticleCommentService::getArticleComment;
    public static final Function<ArticleCommentService, List<ArticleCommentDto>> getArticleComments = ArticleCommentService::getArticleComments;
    public static final BiFunction<ArticleCommentService, ArticleDto, List<ArticleCommentDto>> getArticleCommentsOfArticle = ArticleCommentService::getArticleComments;
    public static final BiFunction<ArticleCommentService, MemberDto, List<ArticleCommentDto>> getArticleCommentsOfMember = ArticleCommentService::getArticleComments;
    public static final BiFunction<ArticleCommentService, ArticleCommentDto, Long> saveArticleComment = ArticleCommentService::saveArticleComment;
    public static final BiConsumer<ArticleCommentService, ArticleCommentDto> updateArticleComment = ArticleCommentService::updateArticleComment;
    public static final BiConsumer<ArticleCommentService, ArticleCommentDto> softDeleteArticleComment = ArticleCommentService::softDeleteArticleComment;

    public static final BiFunction<MemberService, Long, MemberDto> getMember = MemberService::getMember;
    public static final Function<MemberService, List<MemberDto>> getMembers = MemberService::getMembers;
    public static final BiFunction<MemberService, MemberDto, Long> saveMember = MemberService::saveMember;
    public static final BiConsumer<MemberService, MemberDto> updateMemberNickname = MemberService::updateMember;
    public static final BiConsumer<MemberService, MemberDto> updateMemberPassword = MemberService::updateMember;
    public static final BiConsumer<MemberService, MemberDto> softDeleteMember = MemberService::softDeleteMember;

    public static final BiFunction<AuthService, AuthDto, AuthDto> login = AuthService::login;


    public static final BiFunction<AttachmentService, Long, AttachmentDto> getAttachment = AttachmentService::getAttachment;
    public static final Function<AttachmentService, List<AttachmentDto>> getAttachments = AttachmentService::getAttachments;
    public static final BiFunction<AttachmentService, ArticleDto, List<AttachmentDto>> getAttachmentOfArticle = AttachmentService::getAttachments;
    public static final BiFunction<AttachmentService, AttachmentDto, String> saveAttachment = AttachmentService::saveAttachment;
    public static final BiFunction<AttachmentService, List<AttachmentDto>, List<String>> saveAttachments = AttachmentService::saveAttachments;
    public static final BiConsumer<AttachmentService, AttachmentDto> softDeleteAttachment = AttachmentService::softDeleteAttachment;
    public static final BiConsumer<AttachmentService, AttachmentDto> hardDeleteAttachment = AttachmentService::hardDeleteAttachment;

}
