package com.example.basic.test01junit;

import org.junit.jupiter.api.Test;                 // JUnit Jupiter (JUnit 5·6 공통)

import static org.junit.jupiter.api.Assertions.*;  // assertEquals 등

/**
 * [테스트 1장 2.1] 첫 번째 테스트 — @Test 와 given-when-then.
 */
class CalculatorTest {

    @Test                                  // 이 메소드가 "테스트"임을 표시
    void 두_수를_더한다() {
        // given - 준비
        Calculator calc = new Calculator();

        // when - 실행
        int result = calc.add(2, 3);

        // then - 검증
        assertEquals(5, result);           // 기대값 5 == 실제값 result?
    }

    @Test
    void 영으로_나누면_예외() {
        Calculator calc = new Calculator();

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> calc.divide(1, 0));  // 이 람다를 실행했을 때 예외가 나야 통과

        assertEquals("0으로 나눌 수 없습니다", e.getMessage());
    }
}
