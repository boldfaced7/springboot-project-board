package com.boldfaced7.board;

import com.boldfaced7.board.domain.Article;
import com.boldfaced7.board.domain.ArticleComment;
import com.boldfaced7.board.domain.Member;
import com.boldfaced7.board.repository.ArticleCommentRepository;
import com.boldfaced7.board.repository.ArticleRepository;
import com.boldfaced7.board.repository.MemberRepository;
import com.boldfaced7.board.service.DependencyHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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

    public static final BiFunction<BCryptPasswordEncoder, String, String> encode = BCryptPasswordEncoder::encode;

    public static final Function<DependencyHolder, ArticleRepository> articleRepoFunc = DependencyHolder::getArticleRepository;
    public static final Function<DependencyHolder, ArticleCommentRepository> articleCommentRepoFunc = DependencyHolder::getArticleCommentRepository;
    public static final Function<DependencyHolder, MemberRepository> memberRepoFunc = DependencyHolder::getMemberRepository;
    public static final Function<DependencyHolder, BCryptPasswordEncoder> encoderFunc = DependencyHolder::getEncoder;
}
