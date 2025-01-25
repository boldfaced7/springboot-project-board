package com.boldfaced7.application.service;

import com.boldfaced7.UseCase;
import com.boldfaced7.application.port.in.StoreAttachmentsCommand;
import com.boldfaced7.application.port.in.StoreAttachmentsUseCase;
import com.boldfaced7.application.port.out.*;
import com.boldfaced7.application.port.out.GetMemberInfoPort;
import com.boldfaced7.application.port.out.GetMemberInfoRequest;
import com.boldfaced7.domain.Attachment;
import com.boldfaced7.exception.member.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static com.boldfaced7.domain.Attachment.*;

@UseCase
@Transactional
@RequiredArgsConstructor
public class StoreAttachmentsService implements StoreAttachmentsUseCase {

    private final GetMemberInfoPort getMemberInfoPort;
    private final StoreFilesPort storeFilesPort;
    private final SaveAttachmentsPort saveAttachmentsPort;

    @Override
    public List<Attachment> storeAttachments(StoreAttachmentsCommand command) {
        ensureMemberExists(command.memberId());
        List<String> uploadedNames = command.uploadedNames();
        List<String> storedNames = storeFiles(command.multipartFiles());

        return saveAttachments(
                command.memberId(),
                uploadedNames,
                storedNames
        );
    }

    private void ensureMemberExists(String memberId) {
        getMemberInfoPort.getMember(new GetMemberInfoRequest(memberId))
                .orElseThrow(MemberNotFoundException::new);
    }

    private List<String> storeFiles(List<MultipartFile> multipartFiles) {
        StoreFilesRequest request = new StoreFilesRequest(multipartFiles);
        StoreFilesResponse response = storeFilesPort.storeFiles(request);
        return response.storedNames();
    }

    private List<Attachment> saveAttachments(
            String memberId,
            List<String> uploadedNames,
            List<String> storedNames
    ) {
        List<Attachment> attachments = new ArrayList<>();

        for (int i = 0; i < uploadedNames.size(); i++) {
            Attachment generated = generate(
                    new MemberId(memberId),
                    new UploadedName(uploadedNames.get(i)),
                    new StoredName(storedNames.get(i))
            );
            attachments.add(generated);
        }
        return saveAttachmentsPort.saveAttachments(attachments);
    }
}