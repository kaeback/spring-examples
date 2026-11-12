package com.example.basic.test03tdd;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * [테스트 3장 2.2] Red → Green → Refactor를 반복하며 한 회차에 하나씩 추가한 테스트.
 */
@DisplayName("비밀번호 규칙")
class PasswordValidatorTest {

    PasswordValidator validator = new PasswordValidator();

    @Test
    @DisplayName("규칙을 모두 지키면 위반 항목이 없다")            // 1회차
    void 규칙을_모두_지키면_통과() {
        assertThat(validator.validate("abcd1234")).isEmpty();
    }

    @Test
    @DisplayName("8자보다 짧으면 길이 규칙 위반")                 // 2회차
    void 짧으면_길이_위반() {
        assertThat(validator.validate("abc123")).containsExactly("8자 이상이어야 합니다");
    }

    @Test
    @DisplayName("숫자가 없으면 숫자 규칙 위반")                  // 3회차
    void 숫자가_없으면_위반() {
        assertThat(validator.validate("abcdefgh")).containsExactly("숫자를 포함해야 합니다");
    }

    @Test
    @DisplayName("여러 규칙을 어기면 위반 항목을 모두 알려 준다")     // 4회차
    void 여러_규칙_위반() {
        assertThat(validator.validate("abc"))
                .containsExactly("8자 이상이어야 합니다", "숫자를 포함해야 합니다");
    }

    @Test
    @DisplayName("영문자가 없으면 영문자 규칙 위반")               // 5회차
    void 영문자가_없으면_위반() {
        assertThat(validator.validate("12345678")).containsExactly("영문자를 포함해야 합니다");
    }

    @Test
    @DisplayName("공백이 있으면 공백 규칙 위반")                   // 6회차
    void 공백이_있으면_위반() {
        assertThat(validator.validate("abcd 1234")).containsExactly("공백을 포함할 수 없습니다");
    }
}
