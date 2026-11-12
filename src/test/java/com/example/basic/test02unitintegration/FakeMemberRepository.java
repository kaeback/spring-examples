package com.example.basic.test02unitintegration;

import com.example.basic.test01junit.Member;
import com.example.basic.test01junit.MemberRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * [테스트 2장 2.1] Fake — DB 대신 메모리에 저장하는 가짜 구현. 테스트만을 위해 src/test 에 둔다.
 */
class FakeMemberRepository implements MemberRepository {

    private final Map<Long, Member> store = new HashMap<>();

    public Member save(Member m) { store.put(m.getId(), m); return m; }
    public Optional<Member> findById(Long id) { return Optional.ofNullable(store.get(id)); }
    public List<Member> findAll() { return new ArrayList<>(store.values()); }
}
