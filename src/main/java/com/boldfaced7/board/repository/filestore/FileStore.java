package com.boldfaced7.board.repository.filestore;

import com.boldfaced7.board.domain.Attachment;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface FileStore {
    public Attachment storeFile(MultipartFile multipartFile);
    public List<Attachment> storeFiles(List<MultipartFile> multipartFiles);
    public void removeFile(Attachment attachment);
    public void removeFiles(List<Attachment> attachments);
    public String getUrl(Attachment attachment);
    public List<String> getUrls(List<Attachment> attachments);
}