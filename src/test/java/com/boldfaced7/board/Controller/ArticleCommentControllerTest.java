package com.boldfaced7.board.Controller;

import com.boldfaced7.board.dto.ArticleCommentDto;
import com.boldfaced7.board.dto.request.ArticleCommentRequest;
import com.boldfaced7.board.service.ArticleCommentService;
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

@DisplayName("API 컨트롤러 - 댓글")
@WebMvcTest(ArticleCommentController.class)
class ArticleCommentControllerTest {

    @Autowired MockMvc mvc;
    @Autowired Gson gson;
    @MockBean private ArticleCommentService articleCommentService;
    private static String articleCommentUrlTemplate = "/api/articleComments";
    private static String articleUrlTemplate = "/api/articles";

    @DisplayName("[API][GET] 게시글 연관 댓글 리스트 조회 - 정상 호출")
    @Test
    void givenArticleId_whenRequestingArticleComments_thenReturnsArticleCommentsJsonResponse() throws Exception {
        // Given
        Long articleId = 1L;
        ArticleCommentDto dto = createArticleCommentDto(articleId);
        given(articleCommentService.getArticleComments(articleId)).willReturn(List.of(dto));

        // When & Then
        mvc.perform(get(articleUrlTemplate + "/" + articleId + "/articleComments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.articleComments[0].articleCommentId").value(dto.getArticleCommentId()))
                .andExpect(jsonPath("$.articleComments[0].content").value(dto.getContent()))
                .andExpect(jsonPath("$.articleComments[0].articleId").value(dto.getArticleId()))
                .andExpect(jsonPath("$.articleComments[0].author").value(dto.getAuthor()));

        then(articleCommentService).should().getArticleComments(articleId);
    }

    @DisplayName("[API][GET] 게시글 연관 댓글 단일 조회 - 정상 호출")
    @Test
    void givenArticleIdAndArticleCommentId_whenRequestingArticleComment_thenReturnsArticleCommentJsonResponse() throws Exception {
        // Given
        Long articleId = 1L;
        Long articleCommentId = 1L;
        ArticleCommentDto dto = createArticleCommentDto(articleId);
        given(articleCommentService.getArticleComment(articleCommentId)).willReturn(dto);

        // When & Then
        mvc.perform(get(articleUrlTemplate + "/" + articleId + "/articleComments/" + articleCommentId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.articleCommentId").value(dto.getArticleCommentId()))
                .andExpect(jsonPath("$.content").value(dto.getContent()))
                .andExpect(jsonPath("$.author").value(dto.getAuthor()))
                .andExpect(jsonPath("$.articleId").value(dto.getArticleId()));

        then(articleCommentService).should().getArticleComment(articleCommentId);
    }

    @DisplayName("[API][POST] 게시글 연관 댓글 단일 등록 - 정상 호출")
    @Test
    void givenNewArticleCommentInfo_whenRequestingSaving_thenSavesNewArticleComment() throws Exception {
        // Given
        Long articleId = 1L;
        Long articleCommentId = 1L;
        ArticleCommentRequest articleCommentRequest = createArticleCommentRequest();
        ArticleCommentDto dto = articleCommentRequest.toDto(articleId);
        given(articleCommentService.saveArticleComment(dto)).willReturn(articleCommentId);

        // When & Then
        mvc.perform(post(articleUrlTemplate + "/" + articleId + "/articleComments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(articleCommentRequest))
                )
                .andExpect(status().isCreated())
                .andExpect(redirectedUrl(articleUrlTemplate + "/" + articleId + "/articleComments/" + articleCommentId));

        then(articleCommentService).should().saveArticleComment(dto);
    }

    @DisplayName("[API][PATCH] 게시글 연관 댓글 단일 수정 - 정상 호출")
    @Test
    void givenArticleIdAndArticleCommentIdAndModifiedArticleCommentInfo_whenRequestingUpdating_thenUpdatesArticleComment() throws Exception {
        // Given
        Long articleId = 1L;
        Long articleCommentId = 1L;
        ArticleCommentRequest articleCommentRequest = createArticleCommentRequest();
        ArticleCommentDto dto = articleCommentRequest.toDto();
        willDoNothing().given(articleCommentService).updateArticleComment(articleCommentId, dto);

        // When & Then
        mvc.perform(patch(articleUrlTemplate + "/" + articleId + "/articleComments/" + articleCommentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(articleCommentRequest))
                )
                .andExpect(status().isOk());
        then(articleCommentService).should().updateArticleComment(articleCommentId, dto);
    }

    @DisplayName("[API][DELETE] 게시글 연관 댓글 단일 삭제 - 정상 호출")
    @Test
    void givenArticleIdAndArticleCommentId_whenRequestingDeleting_thenDeletesArticleComment() throws Exception {
        // Given
        Long articleId = 1L;
        Long articleCommentId = 1L;
        willDoNothing().given(articleCommentService).softDeleteArticleComment(articleCommentId);

        // When & Then
        mvc.perform(delete(articleUrlTemplate + "/" + articleId + "/articleComments/" + articleCommentId))
                .andExpect(status().isOk());
        then(articleCommentService).should().softDeleteArticleComment(articleCommentId);
    }

    @DisplayName("[API][GET] 전체 댓글 리스트 조회 - 정상 호출")
    @Test
    void givenNothing_whenRequestingArticleComments_thenReturnsArticleCommentsJsonResponse() throws Exception {
        // Given
        ArticleCommentDto dto = createArticleCommentDto();
        given(articleCommentService.getArticleComments()).willReturn(List.of(dto));

        // When & Then
        mvc.perform(get(articleCommentUrlTemplate))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.articleComments[0].articleCommentId").value(dto.getArticleCommentId()))
                .andExpect(jsonPath("$.articleComments[0].content").value(dto.getContent()))
                .andExpect(jsonPath("$.articleComments[0].articleId").value(dto.getArticleId()))
                .andExpect(jsonPath("$.articleComments[0].author").value(dto.getAuthor()));

        then(articleCommentService).should().getArticleComments();
    }

    @DisplayName("[API][GET] 댓글 단건 조회 - 정상 호출")
    @Test
    void givenArticleCommentId_whenRequestingArticleComment_thenReturnsArticleCommentJsonResponse() throws Exception {
        // Given
        Long articleCommentId = 1L;
        ArticleCommentDto dto = createArticleCommentDto();
        given(articleCommentService.getArticleComment(articleCommentId)).willReturn(dto);

        // When & Then
        mvc.perform(get(articleCommentUrlTemplate + "/" + articleCommentId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.articleCommentId").value(dto.getArticleCommentId()))
                .andExpect(jsonPath("$.content").value(dto.getContent()))
                .andExpect(jsonPath("$.author").value(dto.getAuthor()))
                .andExpect(jsonPath("$.articleId").value(dto.getArticleId()));

        then(articleCommentService).should().getArticleComment(articleCommentId);
    }

    @DisplayName("[API][PATCH] 댓글 수정 - 정상 호출")
    @Test
    void givenArticleCommentIdAndModifiedArticleCommentInfo_whenRequestingUpdating_thenUpdatesArticleComment() throws Exception {
        // Given
        Long articleCommentId = 1L;
        ArticleCommentRequest articleCommentRequest = createArticleCommentRequest();
        ArticleCommentDto dto = articleCommentRequest.toDto();
        willDoNothing().given(articleCommentService).updateArticleComment(articleCommentId, dto);

        // When & Then
        mvc.perform(patch(articleCommentUrlTemplate + "/" + articleCommentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(articleCommentRequest))
                )
                .andExpect(status().isOk());
        then(articleCommentService).should().updateArticleComment(articleCommentId, dto);
    }

    @DisplayName("[API][DELETE] 댓글 삭제 - 정상 호출")
    @Test
    void ArticleCommentId_whenRequestingDeleting_thenDeletesArticleComment() throws Exception {
        // Given
        Long articleCommentId = 1L;
        willDoNothing().given(articleCommentService).softDeleteArticleComment(articleCommentId);

        // When & Then
        mvc.perform(delete(articleCommentUrlTemplate + "/" + articleCommentId))
                .andExpect(status().isOk());
        then(articleCommentService).should().softDeleteArticleComment(articleCommentId);
    }

        /*
    @DisplayName("[API][]")
    @Test
    void given_when_then() throws Exception {
        // Given

        // When & Then

    }
     */

    private ArticleCommentDto createArticleCommentDto() {
        return ArticleCommentDto.builder()
                .articleCommentId(1L)
                .content("content")
                .author("boldfaced7")
                .articleId(1L)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
    }

    private ArticleCommentDto createArticleCommentDto(Long articleId) {
        return ArticleCommentDto.builder()
                .articleCommentId(1L)
                .content("content")
                .author("boldfaced7")
                .articleId(articleId)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
    }

    private ArticleCommentRequest createArticleCommentRequest() {
        return ArticleCommentRequest.builder()
                .content("content")
                .author("boldfaced7")
                .build();
    }
}