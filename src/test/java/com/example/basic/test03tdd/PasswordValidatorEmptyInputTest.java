package com.example.basic.test03tdd;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

/**
 * [테스트 3장 3.3] 빠뜨리기 쉬운 입력(null·빈 문자열·공백뿐인 문자열)을 예외로 막는지 확인한다.
 */
@DisplayName("비밀번호 규칙 — 빈 값")
class PasswordValidatorEmptyInputTest {

    PasswordValidator validator = new PasswordValidator();

    @ParameterizedTest(name = "[{index}] {0} → 예외")
    @NullAndEmptySource                               // null 과 "" 두 가지
    @ValueSource(strings = {" ", "   "})              // 공백만 있는 문자열
    void 비어_있으면_예외(String password) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> validator.validate(password))
                .withMessage("비밀번호를 입력하세요");
    }
}
