package com.boldfaced7.board;

import com.boldfaced7.board.domain.Article;
import com.boldfaced7.board.domain.ArticleComment;
import com.boldfaced7.board.domain.Attachment;
import com.boldfaced7.board.domain.Member;
import com.boldfaced7.board.repository.*;
import com.boldfaced7.board.repository.filestore.FileStore;
import com.boldfaced7.board.service.DependencyHolder;
import org.assertj.core.util.TriFunction;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class RepoMethod {
    public static final BiConsumer<ArticleRepository, Article> deleteArticle = ArticleRepository::delete;
    public static final BiFunction<ArticleRepository, Article, Article> saveArticle = ArticleRepository::save;
    public static final BiFunction<ArticleRepository, Long, Optional<Article>> findArticleById = ArticleRepository::findById;
    public static final Function<ArticleRepository, List<Article>> findArticles = ArticleRepository::findAll;
    public static final BiFunction<ArticleRepository, Member, List<Article>> findArticlesByMember = ArticleRepository::findAllByMember;

    public static final BiConsumer<ArticleCommentRepository, ArticleComment> deleteArticleComment = ArticleCommentRepository::delete;
    public static final BiFunction<ArticleCommentRepository, ArticleComment, ArticleComment> saveArticleComment = ArticleCommentRepository::save;
    public static final BiFunction<ArticleCommentRepository, Long, Optional<ArticleComment>> findArticleCommentById = ArticleCommentRepository::findById;
    public static final Function<ArticleCommentRepository, List<ArticleComment>> findArticleComments = ArticleCommentRepository::findAll;
    public static final BiFunction<ArticleCommentRepository, Member, List<ArticleComment>> findArticleCommentsByMember = ArticleCommentRepository::findAllByMember;
    public static final BiFunction<ArticleCommentRepository, Article, List<ArticleComment>> findArticleCommentsByArticle = ArticleCommentRepository::findAllByArticle;

    public static final BiConsumer<MemberRepository, Member> deleteMember = MemberRepository::delete;
    public static final BiFunction<MemberRepository, Member, Member> saveMember = MemberRepository::save;
    public static final BiFunction<MemberRepository, Long, Optional<Member>> findMemberById = MemberRepository::findById;
    public static final BiFunction<MemberRepository, String, Optional<Member>> findMemberByEmail = MemberRepository::findByEmail;
    public static final Function<MemberRepository, List<Member>> findMembers = MemberRepository::findAll;
    public static final BiFunction<MemberRepository, Boolean, List<Member>> findMembersByIsActive = MemberRepository::findAll;
    public static final BiFunction<MemberRepository, String, Boolean> existsMemberByEmail = MemberRepository::existsByEmail;
    public static final BiFunction<MemberRepository, String, Boolean> existsMemberByNickname = MemberRepository::existsByNickname;

    public static final BiFunction<PasswordEncoder, String, String> encode = PasswordEncoder::encode;
    public static final TriFunction<PasswordEncoder, String, String, Boolean> matches = PasswordEncoder::matches;
    public static final BiConsumer<AttachmentRepository, Attachment> deleteAttachment = AttachmentRepository::delete;
    public static final BiFunction<AttachmentRepository, Attachment, Attachment> saveAttachment = AttachmentRepository::save;
    public static final BiFunction<AttachmentRepository, Long, Optional<Attachment>> findAttachmentById = AttachmentRepository::findById;
    public static final BiFunction<AttachmentRepository, String, Optional<Attachment>> findAttachmentByStoredName = AttachmentRepository::findByStoredName;
    public static final BiFunction<AttachmentRepository, Article, List<Attachment>> findAttachmentsByArticle = AttachmentRepository::findAllByArticle;
    public static final Function<AttachmentRepository, List<Attachment>> findAttachments = AttachmentRepository::findAll;
    public static final TriFunction<AttachmentRepository, Article, List<String>, Integer> updateAttachments = AttachmentRepository::updateAttachments;
    public static final BiFunction<AttachmentRepository, Article, Integer> deactivateAttachments = AttachmentRepository::deactivateAttachments;
    public static final BiFunction<AttachmentRepository, Article, Integer> deleteAttachments = AttachmentRepository::deleteAttachments;


    public static final BiFunction<FileStore, MultipartFile, Attachment> storeFile = FileStore::storeFile;
    public static final BiFunction<FileStore, List<MultipartFile>, List<Attachment>> storeFiles = FileStore::storeFiles;
    public static final BiFunction<FileStore, List<Attachment>, List<String>> getUrls = FileStore::getUrls;
    public static final BiFunction<FileStore, Attachment, String> getUrl = FileStore::getUrl;
    public static final BiConsumer<FileStore, Attachment> removeFile = FileStore::removeFile;
    public static final BiConsumer<FileStore, List<Attachment>> removeFiles = FileStore::removeFiles;

    public static final Function<DependencyHolder, ArticleRepository> articleRepoFunc = DependencyHolder::getArticleRepository;
    public static final Function<DependencyHolder, ArticleCommentRepository> articleCommentRepoFunc = DependencyHolder::getArticleCommentRepository;
    public static final Function<DependencyHolder, MemberRepository> memberRepoFunc = DependencyHolder::getMemberRepository;
    public static final Function<DependencyHolder, AttachmentRepository> attachmentRepoFunc = DependencyHolder::getAttachmentRepository;
    public static final Function<DependencyHolder, PasswordEncoder> encoderFunc = DependencyHolder::getEncoder;
    public static final Function<DependencyHolder, FileStore> fileStoreFunc = DependencyHolder::getFileStore;

}
