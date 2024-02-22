package com.boldfaced7.board.Controller;

import com.boldfaced7.board.dto.ArticleCommentDto;
import com.boldfaced7.board.dto.ArticleDto;
import com.boldfaced7.board.dto.MemberDto;
import com.boldfaced7.board.dto.request.ArticleCommentRequest;
import com.boldfaced7.board.dto.response.AuthResponse;
import com.boldfaced7.board.auth.SessionConst;
import com.boldfaced7.board.service.ArticleCommentService;
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

@DisplayName("API 컨트롤러 - 댓글")
@WebMvcTest(ArticleCommentController.class)
class ArticleCommentControllerTest {


    @Autowired MockMvc mvc;
    @Autowired Gson gson;
    @Autowired WebApplicationContext webApplicationContext;
    @MockBean  ArticleCommentService articleCommentService;
    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        session = new MockHttpSession();
        session.setAttribute(SessionConst.AUTH_RESPONSE, createAuthResponse());
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .alwaysDo(print()) // 모든 요청에 대해 print() 적용
                .build();
    }

    @DisplayName("[API][GET] 댓글 단건 조회 - 정상 호출")
    @Test
    void givenArticleCommentId_whenRequestingArticleComment_thenReturnsArticleCommentJsonResponse() throws Exception {
        // Given
        ArticleCommentDto dto = createArticleCommentDto();
        given(articleCommentService.getArticleComment(ARTICLE_COMMENT_ID)).willReturn(dto);

        // When & Then
        mvc.perform(get(articleCommentUrl(ARTICLE_COMMENT_ID))
                        .session(session)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.articleCommentId").value(dto.getArticleCommentId()))
                .andExpect(jsonPath("$.content").value(dto.getContent()))
                .andExpect(jsonPath("$.author").value(dto.getAuthor()))
                .andExpect(jsonPath("$.articleId").value(dto.getArticleId()));

        then(articleCommentService).should().getArticleComment(ARTICLE_COMMENT_ID);
    }

    @DisplayName("[API][GET] 전체 댓글 리스트 조회 - 정상 호출")
    @Test
    void givenNothing_whenRequestingArticleComments_thenReturnsArticleCommentsJsonResponse() throws Exception {
        // Given
        ArticleCommentDto dto = createArticleCommentDto();
        given(articleCommentService.getArticleComments()).willReturn(List.of(createArticleCommentDto()));

        // When & Then
        mvc.perform(get(articleCommentUrl())
                        .session(session)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.articleComments[0].articleCommentId").value(dto.getArticleCommentId()))
                .andExpect(jsonPath("$.articleComments[0].content").value(dto.getContent()))
                .andExpect(jsonPath("$.articleComments[0].articleId").value(dto.getArticleId()))
                .andExpect(jsonPath("$.articleComments[0].author").value(dto.getAuthor()));

        then(articleCommentService).should().getArticleComments();
    }

    @DisplayName("[API][GET] 게시글 연관 댓글 리스트 조회 - 정상 호출")
    @Test
    void givenArticleId_whenRequestingArticleComments_thenReturnsArticleCommentsJsonResponse() throws Exception {
        // Given
        ArticleCommentDto dto = createArticleCommentDto();
        ArticleDto articleDto = new ArticleDto(ARTICLE_ID);
        given(articleCommentService.getArticleComments(articleDto)).willReturn(List.of(dto));

        // When & Then
        mvc.perform(get(articleArticleCommentUrl(dto.getArticleId()))
                        .session(session)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.articleComments[0].articleCommentId").value(dto.getArticleCommentId()))
                .andExpect(jsonPath("$.articleComments[0].content").value(dto.getContent()))
                .andExpect(jsonPath("$.articleComments[0].articleId").value(dto.getArticleId()))
                .andExpect(jsonPath("$.articleComments[0].author").value(dto.getAuthor()));

        then(articleCommentService).should().getArticleComments(articleDto);
    }

    @DisplayName("[API][GET] 회원 연관 댓글 리스트 조회 - 정상 호출")
    @Test
    void givenMemberId_whenRequestingArticleComments_thenReturnsArticleCommentsJsonResponse() throws Exception {
        // Given
        ArticleCommentDto dto = createArticleCommentDto();
        MemberDto memberDto = new MemberDto(MEMBER_ID);
        given(articleCommentService.getArticleComments(memberDto)).willReturn(List.of(dto));

        // When & Then
        mvc.perform(get(memberArticleCommentUrl(memberDto.getMemberId()))
                        .session(session)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.articleComments[0].articleCommentId").value(dto.getArticleCommentId()))
                .andExpect(jsonPath("$.articleComments[0].content").value(dto.getContent()))
                .andExpect(jsonPath("$.articleComments[0].articleId").value(dto.getArticleId()))
                .andExpect(jsonPath("$.articleComments[0].author").value(dto.getAuthor()));

        then(articleCommentService).should().getArticleComments(memberDto);
    }

    @DisplayName("[API][POST] 새 댓글 등록 - 정상 호출")
    @Test
    void givenAuthorizedMemberAndNewArticleCommentInfo_whenRequestingSaving_thenSavesNewArticleComment() throws Exception {
        // Given
        ArticleCommentRequest articleCommentRequest = createArticleCommentRequest();
        ArticleCommentDto articleCommentDto = articleCommentRequest.toDto(ARTICLE_ID);
        given(articleCommentService.saveArticleComment(articleCommentDto)).willReturn(ARTICLE_COMMENT_ID);

        // When & Then
        mvc.perform(post(articleArticleCommentUrl(ARTICLE_ID))
                        .session(session)
                        .content(gson.toJson(articleCommentRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(redirectedUrl(articleArticleCommentUrl(ARTICLE_ID, ARTICLE_COMMENT_ID)));

        then(articleCommentService).should().saveArticleComment(articleCommentDto);
    }

    @DisplayName("[API][PATCH] 댓글 수정 - 정상 호출")
    @Test
    void givenArticleCommentIdAndModifiedArticleCommentInfo_whenRequestingUpdating_thenUpdatesArticleComment() throws Exception {
        // Given
        ArticleCommentRequest articleCommentRequest = createArticleCommentRequest();
        ArticleCommentDto articleCommentDto = articleCommentRequest.toDtoForUpdating(ARTICLE_COMMENT_ID);
        willDoNothing().given(articleCommentService).updateArticleComment(articleCommentDto);

        // When & Then
        mvc.perform(patch(articleCommentUrl(ARTICLE_COMMENT_ID))
                        .session(session)
                        .content(gson.toJson(articleCommentRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        then(articleCommentService).should().updateArticleComment(articleCommentDto);
    }

    @DisplayName("[API][DELETE] 댓글 삭제 - 정상 호출")
    @Test
    void ArticleCommentId_whenRequestingDeleting_thenDeletesArticleComment() throws Exception {
        // Given
        ArticleCommentDto articleCommentDto = createArticleCommentDto(ARTICLE_COMMENT_ID);

        willDoNothing().given(articleCommentService).softDeleteArticleComment(articleCommentDto);

        // When & Then
        mvc.perform(delete(articleCommentUrl(ARTICLE_COMMENT_ID))
                        .session(session)
                )
                .andExpect(status().isOk());

        then(articleCommentService).should().softDeleteArticleComment(articleCommentDto);
    }

        /*
    @DisplayName("[API][]")
    @Test
    void given_when_then() throws Exception {
        // Given

        // When & Then

    }
     */
}