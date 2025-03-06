package com.boldfaced7.adapter.out.resolver;

import com.boldfaced7.application.port.out.*;
import com.boldfaced7.domain.Article;
import com.boldfaced7.domain.ResolvedArticle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ResolveArticleListAdapter implements ResolveArticleListPort {

    private final ListMembersInfoPort listMembersInfoPort;

    @Override
    public List<ResolvedArticle> resolveArticles(List<Article> articles) {
        List<String> nicknames = getNicknames(articles);
        return ResolvedArticle.resolveAll(articles, nicknames);
    }

    List<String> getNicknames(List<Article> articles) {
        ListMembersInfoRequest request = articles.stream()
                .map(Article::getMemberId)
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        ListMembersInfoRequest::new
                ));
        return listMembersInfoPort.getMembers(request).nicknames();
    }
}
