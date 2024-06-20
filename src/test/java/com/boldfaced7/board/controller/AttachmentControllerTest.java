package com.boldfaced7.board.controller;

import com.boldfaced7.board.Mocker;
import com.boldfaced7.board.auth.AuthInfoHolder;
import com.boldfaced7.board.auth.SessionConst;
import com.boldfaced7.board.dto.response.SaveAttachmentsResponse;
import com.boldfaced7.board.service.AttachmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.List;
import java.util.stream.Stream;

import static com.boldfaced7.board.TestUtil.*;
import static org.mockito.ArgumentMatchers.any;

@DisplayName("AttachmentController 테스트")
@WebMvcTest({AttachmentController.class})
class AttachmentControllerTest {
    @Autowired MockMvc mvc;
    @MockBean  AttachmentService attachmentService;
    ControllerTestTemplate<AttachmentService> testTemplate;
    MockHttpSession session;

    @BeforeEach
    void setSessionAndTestTemplate() {
        session = new MockHttpSession();
        AuthInfoHolder.setAuthInfo(authResponse());
        testTemplate = new ControllerTestTemplate<>(mvc, session, attachmentService);
    }

    @DisplayName("[POST] 첨부 파일 업로드")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource
    <T> void postAttachmentTest(Mocker<AttachmentService> mock, List<MockMultipartFile> request, T response, ResultMatcher status) throws Exception {
        testTemplate.doMultipart(mock, attachmentUrl(), request, response, status);
    }
    static Stream<Arguments> postAttachmentTest() {
        Mocker<AttachmentService> valid = new Mocker<>("정상 호출");
        List<String> names = List.of(STORED_NAME + JPG);
        valid.mocks(a -> a.saveAttachments(any()), names);

        return Stream.of(
                Arguments.of(valid, multipartFiles(), new SaveAttachmentsResponse(names), CREATED)
        );
    }
}