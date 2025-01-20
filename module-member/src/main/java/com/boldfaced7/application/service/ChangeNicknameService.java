package com.boldfaced7.application.service;

import com.boldfaced7.UseCase;
import com.boldfaced7.application.port.in.ChangeNicknameCommand;
import com.boldfaced7.application.port.in.ChangeNicknameUseCase;
import com.boldfaced7.application.port.in.UpdateMemberHelper;
import com.boldfaced7.application.port.out.CheckNicknameDuplicationPort;
import com.boldfaced7.domain.Member;
import com.boldfaced7.exception.member.MemberNicknameDuplicatedException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import static com.boldfaced7.domain.Member.*;

@UseCase
@Transactional
@RequiredArgsConstructor
public class ChangeNicknameService implements ChangeNicknameUseCase {

    private final CheckNicknameDuplicationPort checkNicknameDuplicationPort;
    private final UpdateMemberHelper updateMemberHelper;

    @Override
    public Member changeNickname(ChangeNicknameCommand command) {
        Nickname newNickname = createNickname(command.nickname());
        return updateMemberHelper.updateMember(
                command.memberId(),
                member -> member.changeNickname(newNickname)
        );
    }

    private Nickname createNickname(String value) {
        Nickname nickname = new Nickname(value);
        if (checkNicknameDuplicationPort.isDuplicated(nickname)) {
            throw new MemberNicknameDuplicatedException();
        }
        return nickname;
    }
}
