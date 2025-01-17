package com.boldfaced7.api.adapter.in.web;

import com.boldfaced7.api.adapter.in.web.response.SaveAttachmentsResponse;
import com.boldfaced7.attachment.application.AttachmentDto;
import com.boldfaced7.attachment.application.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/api/attachments")
@RestController
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;

    @PostMapping
    public ResponseEntity<SaveAttachmentsResponse> postNewAttachment(
            @Validated @RequestPart(value = "image") List<MultipartFile> multipartFiles) {

        List<AttachmentDto> dtos = multipartFiles.stream().map(AttachmentDto::new).toList();
        List<String> savedNames = attachmentService.saveAttachments(dtos);
        SaveAttachmentsResponse response = new SaveAttachmentsResponse(savedNames);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
