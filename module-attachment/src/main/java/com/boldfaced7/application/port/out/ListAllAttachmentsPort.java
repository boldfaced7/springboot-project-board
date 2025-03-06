package com.boldfaced7.application.port.out;

import com.boldfaced7.domain.Attachment;

import java.util.List;

public interface ListAllAttachmentsPort {
    List<Attachment> listAllAttachments(int pageNumber, int pageSize);
}
