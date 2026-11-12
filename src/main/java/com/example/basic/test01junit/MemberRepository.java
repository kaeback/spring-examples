package com.example.basic.test01junit;

import java.util.List;
import java.util.Optional;

/**
 * [테스트 1장 2.5] 저장소 인터페이스 — 구현을 바꿔 끼울 수 있게 인터페이스로 둔다.
 * (테스트 2장에서 가짜(Fake)·목(Mock)으로 바꿔 끼운다)
 */
public interface MemberRepository {
    Member save(Member member);
    Optional<Member> findById(Long id);   // 없으면 Optional.empty()
    List<Member> findAll();
}
