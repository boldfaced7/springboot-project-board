package com.boldfaced7.board.service;

import com.boldfaced7.board.repository.ArticleCommentRepository;
import com.boldfaced7.board.repository.ArticleRepository;
import com.boldfaced7.board.repository.AttachmentRepository;
import com.boldfaced7.board.repository.MemberRepository;
import com.boldfaced7.board.repository.filestore.FileStore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.function.Function;

@Builder
@Getter
@AllArgsConstructor
public class Facade {
    private ArticleRepository mockArticleRepository;
    private ArticleCommentRepository mockArticleCommentRepository;
    private MemberRepository mockMemberRepository;
    private AttachmentRepository mockAttachmentRepository;
    private FileStore mockFileStore;
    private PasswordEncoder mockPasswordEncoder;

    public static final Function<Facade, ArticleRepository> articleRepository = Facade::getMockArticleRepository;
    public static final Function<Facade, ArticleCommentRepository> articleCommentRepository = Facade::getMockArticleCommentRepository;
    public static final Function<Facade, MemberRepository> memberRepository = Facade::getMockMemberRepository;
    public static final Function<Facade, AttachmentRepository> attachmentRepository = Facade::getMockAttachmentRepository;
    public static final Function<Facade, FileStore> fileStore = Facade::getMockFileStore;
    public static final Function<Facade, PasswordEncoder> passwordEncoder = Facade::getMockPasswordEncoder;
}