package com.example.basic.test01junit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * [테스트 1장 2.5] DB 대신 Map 에 저장하는 구현.
 *
 * <p>스프링 빈으로 등록하지 않는다(@Repository 없음). 테스트에서 new 로 직접 만들어 쓴다.
 */
public class MemoryMemberRepository implements MemberRepository {

    private final Map<Long, Member> store = new HashMap<>();

    @Override
    public Member save(Member member) {
        store.put(member.getId(), member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }
}
