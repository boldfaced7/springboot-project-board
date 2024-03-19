package com.boldfaced7.board.service;

import com.boldfaced7.board.repository.*;
import com.boldfaced7.board.repository.filestore.FileStore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Builder
@Getter
@AllArgsConstructor
public class DependencyHolder {
    private ArticleRepository articleRepository;
    private ArticleCommentRepository articleCommentRepository;
    private MemberRepository memberRepository;
    private AttachmentRepository attachmentRepository;
    private BCryptPasswordEncoder encoder;
    private FileStore fileStore;
}