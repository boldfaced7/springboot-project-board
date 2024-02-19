package com.boldfaced7.board.Controller;

import com.boldfaced7.board.dto.ArticleCommentDto;
import com.boldfaced7.board.service.ArticleService;
import com.boldfaced7.board.dto.ArticleDto;
import com.boldfaced7.board.dto.request.ArticleRequest;
import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("API 컨트롤러 - 게시글")
@WebMvcTest(ArticleController.class)
class ArticleControllerTest {

    @Autowired MockMvc mvc;
    @Autowired Gson gson;
    @MockBean private ArticleService articleService;
    final String urlTemplate = "/api/articles";

    @DisplayName("[API][GET] 게시글 리스트 조회 - 정상 호출")
    @Test
    void givenNothing_whenRequestingArticles_thenReturnsArticlesJsonResponse() throws Exception {
        // Given
        ArticleDto articleDto = createArticleDto();
        given(articleService.getArticles()).willReturn(List.of(articleDto));

        // When & Then
        mvc.perform(get(urlTemplate))
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
        Long articleId = 1L;
        ArticleDto articleDto = createArticleDto();
        given(articleService.getArticle(articleId)).willReturn(articleDto);

        // When & Then
        mvc.perform(get(urlTemplate + "/" + articleId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.articleId").value(articleDto.getArticleId()))
                .andExpect(jsonPath("$.title").value(articleDto.getTitle()))
                .andExpect(jsonPath("$.content").value(articleDto.getContent()))
                .andExpect(jsonPath("$.author").value(articleDto.getAuthor()));

        then(articleService).should().getArticle(articleId);
    }

    @DisplayName("[API][POST] 새 게시글 등록 - 정상 호출")
    @Test
    void givenNewArticleInfo_whenRequestingSaving_thenSavesNewArticle() throws Exception {
        // Given
        Long articleId = 1L;
        ArticleRequest articleRequest = createArticleRequest();
        ArticleDto articleDto = articleRequest.toDto();
        given(articleService.saveArticle(articleDto)).willReturn(articleId);

        // When & Then
        mvc.perform(post(urlTemplate)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(articleRequest))
                )
                .andExpect(status().isCreated());
        then(articleService).should().saveArticle(articleDto);
    }

    @DisplayName("[API][PATCH] 게시글 수정 - 정상 호출")
    @Test
    void givenModifiedArticleInfo_whenRequestingUpdating_thenUpdatesArticle() throws Exception {
        // Given
        Long articleId = 1L;
        ArticleRequest articleRequest = createArticleRequest();
        ArticleDto articleDto = articleRequest.toDto();
        willDoNothing().given(articleService).updateArticle(articleId, articleDto);

        // When & Then
        mvc.perform(patch(urlTemplate + "/" + articleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(articleRequest))
                )
                .andExpect(status().isOk());
        then(articleService).should().updateArticle(articleId, articleDto);
    }

    @DisplayName("[API][DELETE] 게시글 삭제 - 정상 호출")
    @Test
    void given_when_then() throws Exception {
        // Given
        Long articleId = 1L;
        willDoNothing().given(articleService).softDeleteArticle(articleId);

        // When & Then
        mvc.perform(delete(urlTemplate + "/" + articleId))
                .andExpect(status().isOk());
        then(articleService).should().softDeleteArticle(articleId);
    }

    /*
    @DisplayName("")
    @Test
    void given_when_then() throws Exception {
        // Given

        // When & Then

    }
     */

    private ArticleDto createArticleDto() {
        return ArticleDto.builder()
                .articleId(1L)
                .title("title")
                .content("content")
                .author("boldfaced7")
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .articleComments(List.of(createArticleCommentDto()))
                .build();
    }

    private ArticleCommentDto createArticleCommentDto() {
        return ArticleCommentDto.builder()
                .articleCommentId(1L)
                .articleId(1L)
                .content("content")
                .author("boldfaced7")
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
    }

    private ArticleRequest createArticleRequest() {
        return ArticleRequest.builder()
                .title("title")
                .content("content")
                .author("author")
                .build();
    }
}