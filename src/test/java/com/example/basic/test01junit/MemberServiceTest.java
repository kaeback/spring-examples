package com.example.basic.test01junit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * [테스트 1장 2.4] @BeforeEach — 테스트마다 새 저장소로 시작해 서로 영향을 주지 않는다.
 */
class MemberServiceTest {

    MemberService service;

    @BeforeEach                 // 각 @Test 실행 전마다 새로 준비
    void setUp() {
        service = new MemberService(new MemoryMemberRepository());
    }

    @Test
    void 회원_가입() {
        Member saved = service.join(new Member(1L, "kim"));

        assertEquals("kim", service.find(1L).getName());
        assertTrue(saved.isActive());
    }

    @Test
    void 중복_회원_예외() {
        service.join(new Member(1L, "kim"));

        assertThrows(IllegalStateException.class,
                () -> service.join(new Member(1L, "lee")));   // 같은 id로 다시 가입
    }
}
