package com.boldfaced7.noboilerplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ControllerTestTemplate<S> {
    private final ObjectMapper objectMapper = objectMapper();
    MockMvc mvc;
    MockHttpSession session;
    S mockedElement;

    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .registerModule(new JavaTimeModule());

        return objectMapper;
    }

    public <T> void doMultipart(String url, List<MockMultipartFile> files, T response, ResultMatcher status) throws Exception {
        MockMultipartHttpServletRequestBuilder multipart = multipart(url);

        for (MockMultipartFile file : files) {
            multipart = multipart.file(file);
        }

        ResultActions resultActions = mvc.perform(multipart.session(session))
                .andDo(print())
                .andExpect(status);

        validateResponse(resultActions, response);
    }
    public <T> void doGet(String url, ResultMatcher status, T response) throws Exception {

        ResultActions resultActions = mvc.perform(get(url).session(session))
                .andDo(print())
                .andExpect(status)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        validateResponse(resultActions, response);
    }
    public void doGet(String url, ResultMatcher status) throws Exception {
        mvc.perform(get(url).session(session))
                .andDo(print())
                .andExpect(status)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    public void doGetWithoutContentType(String url, ResultMatcher status) throws Exception {
        mvc.perform(get(url).session(session))
                .andDo(print())
                .andExpect(status);
    }
    public <R> void doPost(String url, R request, ResultMatcher status) throws Exception {
        mvc.perform(post(url)
                        .session(session)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status);
    }
    public <R> void doPatch(String url, R request, ResultMatcher status) throws Exception {
        mvc.perform(patch(url)
                        .session(session)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status);
    }
    public void doDelete(String url, ResultMatcher status) throws Exception {
        mvc.perform(delete(url)
                        .session(session))
                .andDo(print())
                .andExpect(status);
    }

    public <T> void doMultipart(Mocker<S> mocker, String url, List<MockMultipartFile> files, T response, ResultMatcher status) throws Exception {
        mocker.executeGivens(mockedElement);
        doMultipart(url, files, response, status);
        mocker.executeThens(mockedElement);
    }
    public <T> void doGet(Mocker<S> mocker, String url, ResultMatcher status, T response) throws Exception {
        mocker.executeGivens(mockedElement);
        doGet(url, status, response);
        mocker.executeThens(mockedElement);
    }
    public <T> void doGet(Mocker<S> mocker, String url, ResultMatcher status) throws Exception {
        mocker.executeGivens(mockedElement);
        doGetWithoutContentType(url, status);
        mocker.executeThens(mockedElement);
    }
    public <R> void doPost(Mocker<S> mocker, String url, R request, ResultMatcher status) throws Exception {
        mocker.executeGivens(mockedElement);
        doPost(url, request, status);
        mocker.executeThens(mockedElement);
    }
    public <R> void doPatch(Mocker<S> mocker, String url, R request, ResultMatcher status) throws Exception {
        mocker.executeGivens(mockedElement);
        doPatch(url, request, status);
        mocker.executeThens(mockedElement);
    }
    public void doDelete(Mocker<S> mocker, String url, ResultMatcher status) throws Exception {
        mocker.executeGivens(mockedElement);
        doDelete(url, status);
        mocker.executeThens(mockedElement);
    }

    private <T> void validateResponse(ResultActions resultActions, T response) throws Exception {
        if (response == null) {
            return;
        }
        ObjectMapper objectMapper = objectMapper();
        resultActions.andExpect(content().json(objectMapper.writeValueAsString(response)));
    }
}