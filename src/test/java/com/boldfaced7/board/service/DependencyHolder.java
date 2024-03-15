package com.boldfaced7.board.service;

import com.boldfaced7.board.repository.ArticleCommentRepository;
import com.boldfaced7.board.repository.ArticleRepository;
import com.boldfaced7.board.repository.MemberRepository;
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
    private BCryptPasswordEncoder encoder;
}
