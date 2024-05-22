package com.boldfaced7.board.repository.filestore;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.boldfaced7.board.domain.Attachment;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Profile("prod")
@RequiredArgsConstructor
public class S3FileStore implements FileStore {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    public Attachment storeFile(MultipartFile multipartFile) {
        String uploadedName = multipartFile.getOriginalFilename();
        String storedName = createStoredName(uploadedName);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/" + extractExt(uploadedName));

        try {
            InputStream inputStream = multipartFile.getInputStream();
            PutObjectRequest request = new PutObjectRequest(bucket, storedName, inputStream, metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead);
            amazonS3.putObject(request);
        } catch (IOException e) {
            e.printStackTrace();
            return Attachment.builder().build();
        }
        return Attachment.builder().uploadedName(uploadedName).storedName(storedName).build();
    }

    @Override
    public void removeFile(Attachment attachment) {
        try {
            amazonS3.deleteObject(bucket, attachment.getStoredName());
        } catch (AmazonServiceException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getUrl(Attachment attachment) {
        return getFullPath(attachment.getStoredName());
    }

    @Override
    public List<Attachment> storeFiles(List<MultipartFile> multipartFiles) {
        return multipartFiles.stream().map(this::storeFile).toList();
    }

    @Override
    public void removeFiles(List<Attachment> attachments) {
        attachments.forEach(this::removeFile);
    }

    @Override
    public List<String> getUrls(List<Attachment> attachments) {
        if (attachments.isEmpty()) return new ArrayList<>();
        return attachments.stream().map(this::getUrl).toList();
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

    private String getFullPath(String fileName) {
        return amazonS3.getUrl(bucket, fileName).toString();
    }
}
