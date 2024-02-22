package com.boldfaced7.board.Controller;

import com.boldfaced7.board.dto.MemberDto;
import com.boldfaced7.board.dto.response.AuthResponse;
import com.boldfaced7.board.auth.SessionConst;
import com.boldfaced7.board.service.ArticleService;
import com.boldfaced7.board.dto.ArticleDto;
import com.boldfaced7.board.dto.request.ArticleRequest;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static com.boldfaced7.board.TestUtil.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("API 컨트롤러 - 게시글")
@WebMvcTest(ArticleController.class)
class ArticleControllerTest {

    @Autowired MockMvc mvc;
    @Autowired Gson gson;
    @Autowired WebApplicationContext webApplicationContext;
    @MockBean  ArticleService articleService;
    MockHttpSession session;

    @BeforeEach
    void setUp() {
        session = new MockHttpSession();
        session.setAttribute(SessionConst.AUTH_RESPONSE, createAuthResponse());

        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .alwaysDo(print()) // 모든 요청에 대해 print() 적용
                .build();
    }
    @DisplayName("[API][GET] 회원 연관 게시글 리스트 조회 - 정상 호출")
    @Test
    void givenMemberId_whenRequestingArticles_thenReturnsArticlesJsonResponse() throws Exception {
        // Given
        ArticleDto articleDto = createArticleDto();
        MemberDto memberDto = new MemberDto(MEMBER_ID);
        given(articleService.getArticles(memberDto)).willReturn(List.of(articleDto));

        // When & Then
        mvc.perform(get(memberArticleUrl(memberDto.getMemberId()))
                        .session(session)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.articles[0].articleId").value(articleDto.getArticleId()))
                .andExpect(jsonPath("$.articles[0].content").value(articleDto.getContent()))
                .andExpect(jsonPath("$.articles[0].articleId").value(articleDto.getArticleId()))
                .andExpect(jsonPath("$.articles[0].author").value(articleDto.getAuthor()));

        then(articleService).should().getArticles(memberDto);
    }

    @DisplayName("[API][GET] 게시글 리스트 조회 - 정상 호출")
    @Test
    void givenNothing_whenRequestingArticles_thenReturnsArticlesJsonResponse() throws Exception {
        // Given
        ArticleDto articleDto = createArticleDto();
        given(articleService.getArticles()).willReturn(List.of(articleDto));

        // When & Then
        mvc.perform(get(articleUrl())
                        .session(session)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.articles[0].articleId").value(articleDto.getArticleId()))
                .andExpect(jsonPath("$.articles[0].title").value(articleDto.getTitle()))
                .andExpect(jsonPath("$.articles[0].content").value(articleDto.getContent()))
                .andExpect(jsonPath("$.articles[0].author").value(articleDto.getAuthor()));

        then(articleService).should().getArticles();
    }

    @DisplayName("[API][GET] 게시글 단건 조회 - 정상 호출")
    @Test
    void givenNothing_whenRequestingArticle_thenReturnsArticleJsonResponse() throws Exception {
        // Given
        ArticleDto articleDto = createArticleDto();
        given(articleService.getArticle(ARTICLE_ID)).willReturn(articleDto);

        // When & Then
        mvc.perform(get(articleUrl(ARTICLE_ID))
                        .session(session)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.articleId").value(articleDto.getArticleId()))
                .andExpect(jsonPath("$.title").value(articleDto.getTitle()))
                .andExpect(jsonPath("$.content").value(articleDto.getContent()))
                .andExpect(jsonPath("$.author").value(articleDto.getAuthor()));

        then(articleService).should().getArticle(ARTICLE_ID);
    }

    @DisplayName("[API][POST] 새 게시글 등록 - 정상 호출")
    @Test
    void givenAuthorizedMemberAndNewArticleInfo_whenRequestingSaving_thenSavesNewArticle() throws Exception {
        // Given
        ArticleRequest articleRequest = createArticleRequest();
        ArticleDto articleDto = articleRequest.toDto();
        given(articleService.saveArticle(articleDto)).willReturn(ARTICLE_ID);

        // When & Then
        mvc.perform(post(articleUrl())
                        .session(session)
                        .content(gson.toJson(articleRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(redirectedUrl(articleUrl(ARTICLE_ID)));
        then(articleService).should().saveArticle(articleDto);
    }

    @DisplayName("[API][PATCH] 게시글 수정 - 정상 호출")
    @Test
    void givenAuthorizedMemberAndModifiedArticleInfo_whenRequestingUpdating_thenUpdatesArticle() throws Exception {
        // Given
        ArticleRequest articleRequest = createArticleRequest();
        ArticleDto articleDto = articleRequest.toDto(ARTICLE_ID);
        willDoNothing().given(articleService).updateArticle(articleDto);

        // When & Then
        mvc.perform(patch(articleUrl(ARTICLE_ID))
                        .session(session)
                        .content(gson.toJson(articleRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
        then(articleService).should().updateArticle(articleDto);
    }

    @DisplayName("[API][DELETE] 게시글 삭제 - 정상 호출")
    @Test
    void givenAuthorizedMemberAndArticleId_whenRequestingDeleting_thenDeletesArticle() throws Exception {
        // Given
        ArticleDto articleDto = createArticleDto(ARTICLE_ID);

        willDoNothing().given(articleService).softDeleteArticle(articleDto);

        // When & Then
        mvc.perform(delete(articleUrl(ARTICLE_ID))
                        .session(session))
                .andExpect(status().isOk());
        then(articleService).should().softDeleteArticle(articleDto);
    }

    /*
    @DisplayName("[API][]  - 정상 호출")
    @Test
    void given_when_then() throws Exception {
        // Given

        // When & Then

    }
     */
}