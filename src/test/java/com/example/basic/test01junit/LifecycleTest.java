package com.example.basic.test01junit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * [테스트 1장 2.5] 생명주기 — 어노테이션이 붙은 메서드가 언제 호출되는지 콘솔로 확인한다.
 */
class LifecycleTest {

    LifecycleTest() {
        System.out.println("생성자 — 인스턴스 " + Integer.toHexString(hashCode()));
    }

    @BeforeAll
    static void beforeAll() {              // 클래스 전체에서 한 번. 인스턴스가 없을 때 불리므로 static
        System.out.println("@BeforeAll");
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("  @BeforeEach");
    }

    @Test
    void 테스트1() {
        System.out.println("    @Test 테스트1");
    }

    @Test
    void 테스트2() {
        System.out.println("    @Test 테스트2");
    }

    @AfterEach
    void afterEach() {
        System.out.println("  @AfterEach");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("@AfterAll");
    }
}
