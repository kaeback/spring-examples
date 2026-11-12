package com.example.basic.test01junit;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * [테스트 1장 2.2~2.3] 검증 — JUnit Assertions 와 AssertJ 를 나란히 비교한다.
 */
class AssertionsTest {

    @Test
    void 자주_쓰는_검증_JUnit() {
        Calculator calc = new Calculator();
        int result = calc.add(2, 3);
        Member member = new Member(1L, "kim");               // 새 회원은 활성 상태로 만들어진다
        Member found = new MemoryMemberRepository()
                .findById(99L).orElse(null);                 // 없는 회원을 찾으면 null

        assertEquals(5, result);              // 같은가
        assertTrue(member.isActive());        // 참인가
        assertNull(found);                    // null인가
        assertThrows(IllegalArgumentException.class,
                () -> calc.divide(1, 0));     // 예외가 발생하는가
    }

    @Test
    void 같은_검증_AssertJ() {
        int result = new Calculator().add(2, 3);
        Member memberA = new Member(1L, "kim");
        List<Member> members = List.of(memberA, new Member(2L, "lee"), new Member(3L, "park"));

        assertThat(result).isEqualTo(5);                       // assertThat(실제값).검증(기대값)
        assertThat(members).hasSize(3).contains(memberA);      // 검증을 이어서 쓸 수 있다
        assertThat(members).extracting(Member::getName)        // 이름만 뽑아서
                .containsExactly("kim", "lee", "park");        // 순서까지 같은가
        assertThatThrownBy(() -> new Calculator().divide(1, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("0으로");
    }
}
