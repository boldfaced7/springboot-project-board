package com.boldfaced7.application.service;

import com.boldfaced7.UseCase;
import com.boldfaced7.application.port.in.*;
import com.boldfaced7.application.port.out.EncodePasswordPort;
import com.boldfaced7.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import static com.boldfaced7.domain.Member.*;

@UseCase
@Transactional
@RequiredArgsConstructor
public class ChangePasswordService implements ChangePasswordUseCase {

    private final EncodePasswordPort encodePasswordPort;
    private final UpdateMemberHelper updateMemberHelper;

    @Override
    public Member changePassword(ChangePasswordCommand command) {
        Password encoded = encodePasswordPort
                .encodePassword(command.password());

        return updateMemberHelper.updateMember(
                command.memberId(),
                member -> member.changePassword(encoded)
        );
    }
}
