package com.boldfaced7.adapter.out.persistence;

import com.boldfaced7.PersistenceAdapter;
import com.boldfaced7.application.port.out.*;
import com.boldfaced7.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static com.boldfaced7.adapter.out.persistence.MemberMapper.mapToDomain;
import static com.boldfaced7.adapter.out.persistence.MemberMapper.mapToJpaEntity;

@PersistenceAdapter
@RequiredArgsConstructor
public class SpringDataMemberPersistenceAdapter implements
        CheckEmailDuplicationPort,
        CheckNicknameDuplicationPort,
        FindMemberPort,
        ListAllMembersPort,
        SaveMemberPort,
        UpdateMemberPort {

    private final MemberJpaRepository memberJpaRepository;

    @Override
    public Optional<Member> findById(Member.Id id) {
        Long parsedId = Long.parseLong(id.value());
        return memberJpaRepository.findById(parsedId)
                .map(MemberMapper::mapToDomain);
    }

    @Override
    public List<Member> listAll(int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return memberJpaRepository.findAll(pageRequest)
                .stream()
                .map(MemberMapper::mapToDomain)
                .toList();
    }

    @Override
    public Member save(Member member) {
        MemberJpaEntity source = mapToJpaEntity(member);
        MemberJpaEntity saved = memberJpaRepository.save(source);
        return mapToDomain(saved);
    }

    @Override
    public Member update(Member member) {
        MemberJpaEntity source = mapToJpaEntity(member);
        MemberJpaEntity updated = memberJpaRepository.save(source);
        return mapToDomain(updated);
    }

    @Override
    public boolean isDuplicated(Member.Email email) {
        return memberJpaRepository.existsByEmail(email.value());
    }

    @Override
    public boolean isDuplicated(Member.Nickname nickname) {
        return memberJpaRepository.existsByNickname(nickname.value());
    }
}
