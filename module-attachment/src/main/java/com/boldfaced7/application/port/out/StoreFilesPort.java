package com.boldfaced7.application.port.out;

import java.util.UUID;

public interface StoreFilesPort {
    StoreFilesResponse storeFiles(StoreFilesRequest request);

    default String createStoredName(String uploadedName) {
        String ext = extractExt(uploadedName);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String uploadedName) {
        int pos = uploadedName.lastIndexOf(".");
        return uploadedName.substring(pos+1);
    }

}
