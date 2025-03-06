package com.boldfaced7.adapter.out.filestore;

import com.boldfaced7.application.port.out.StoreFilesPort;
import com.boldfaced7.application.port.out.StoreFilesRequest;
import com.boldfaced7.application.port.out.StoreFilesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class NoOpFileStoreAdapter implements StoreFilesPort {
    @Override
    public StoreFilesResponse storeFiles(StoreFilesRequest request) {
        return null;
    }
}
