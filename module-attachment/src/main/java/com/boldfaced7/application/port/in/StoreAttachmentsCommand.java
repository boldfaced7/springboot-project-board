package com.boldfaced7.application.port.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record StoreAttachmentsCommand(
        @NotBlank String memberId,
        @NotEmpty List<MultipartFile> multipartFiles
) {

    public List<String> uploadedNames() {
        return multipartFiles.stream()
                .map(MultipartFile::getOriginalFilename)
                .toList();
    }
}
