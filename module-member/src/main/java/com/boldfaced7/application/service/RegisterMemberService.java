package com.boldfaced7.application.service;

import com.boldfaced7.UseCase;
import com.boldfaced7.application.port.in.RegisterMemberCommand;
import com.boldfaced7.application.port.in.RegisterMemberUseCase;
import com.boldfaced7.application.port.out.*;
import com.boldfaced7.domain.Member;
import com.boldfaced7.exception.member.MemberEmailDuplicatedException;
import com.boldfaced7.exception.member.MemberNicknameDuplicatedException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import static com.boldfaced7.domain.Member.*;

@UseCase
@Transactional
@RequiredArgsConstructor
public class RegisterMemberService implements RegisterMemberUseCase {

    private final CheckEmailDuplicationPort checkEmailDuplicationPort;
    private final CheckNicknameDuplicationPort checkNicknameDuplicationPort;
    private final SaveMemberPort saveMemberPort;

    @Override
    public Member registerMember(RegisterMemberCommand command) {
        throwIfEmailExists(command.email());
        throwIfNicknameExists(command.nickname());
        return saveMember(command);
    }

    private void throwIfEmailExists(String email) {
        Email source = new Email(email);
        boolean exists = checkEmailDuplicationPort.isDuplicated(source);

        if (exists) {
            throw new MemberEmailDuplicatedException();
        }
    }

    private void throwIfNicknameExists(String nickname) {
        Nickname source = new Nickname(nickname);
        boolean exists = checkNicknameDuplicationPort.isDuplicated(source);

        if (exists) {
            throw new MemberNicknameDuplicatedException();
        }
    }

    private Member saveMember(RegisterMemberCommand command) {
        Member generated = generate(
                new Email(command.email()),
                new Password(command.password()),
                new Nickname(command.nickname())
        );
        return saveMemberPort.save(generated);
    }
}
