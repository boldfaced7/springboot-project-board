package com.boldfaced7.application.port.out;

import java.util.List;

public record StoreFilesResponse(
        List<String> storedNames
) {
}
