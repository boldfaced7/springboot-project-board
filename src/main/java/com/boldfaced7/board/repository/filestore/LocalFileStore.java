package com.boldfaced7.board.repository.filestore;

import com.boldfaced7.board.domain.Attachment;
import com.boldfaced7.board.error.ErrorCode;
import com.boldfaced7.board.error.exception.BusinessBaseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class LocalFileStore implements FileStore {

    @Value("${file.dir}")
    private String fileDir;

    @Override
    public Attachment storeFile(MultipartFile multipartFile) {
        return store(multipartFile);
    }

    @Override
    public List<Attachment> storeFiles(List<MultipartFile> multipartFiles) {
        return multipartFiles.stream().map(this::storeFile).toList();
    }

    public void removeFile(Attachment attachment) {
        remove(attachment);
    }

    public void removeFiles(List<Attachment> attachments) {
        attachments.forEach(this::removeFile);
    }

    @Override
    public String getUrl(Attachment attachment) {
        return getFullPath(attachment.getStoredName());
    }

    @Override
    public List<String> getUrls(List<Attachment> attachments) {
        return attachments.stream().map(this::getUrl).toList();
    }

    private Attachment store(MultipartFile multipartFile) {
        String uploadedName = multipartFile.getOriginalFilename();
        String storedName = createStoredName(uploadedName);

        try {
            Path targetLocation = Paths.get(getFullPath(storedName));
            Files.copy(multipartFile.getInputStream(), targetLocation);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return Attachment.builder().uploadedName(uploadedName).storedName(storedName).build();
    }

    private void remove(Attachment attachment) {
        try {
            Path targetLocation = Paths.get(getFullPath(attachment.getStoredName()));
            Files.deleteIfExists(targetLocation);

        } catch (IOException e) {
            throw new BusinessBaseException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private String createStoredName(String uploadedName) {
        String ext = extractExt(uploadedName);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String uploadedName) {
        int pos = uploadedName.lastIndexOf(".");
        return uploadedName.substring(pos + 1);
    }

    public String getFullPath(String fileName) {
        return fileDir + fileName;
    }
}
