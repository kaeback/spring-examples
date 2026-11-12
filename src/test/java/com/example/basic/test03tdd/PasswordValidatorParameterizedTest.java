package com.example.basic.test03tdd;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * [테스트 3장 3.2] 같은 행동을 입력만 바꿔 여러 번 확인한다 — 경계값(7자·8자)과 규칙별 위반.
 */
@DisplayName("비밀번호 규칙 — 경계값")
class PasswordValidatorParameterizedTest {

    PasswordValidator validator = new PasswordValidator();

    @ParameterizedTest(name = "{0}자 → 길이 위반")
    @ValueSource(ints = {2, 7})                       // 7자: 통과하지 못하는 가장 긴 길이
    void 여덟_자_미만은_길이_위반(int length) {
        assertThat(validator.validate(passwordOfLength(length)))
                .containsExactly("8자 이상이어야 합니다");
    }

    @ParameterizedTest(name = "{0}자 → 통과")
    @ValueSource(ints = {8, 9, 64})                   // 8자: 통과하는 가장 짧은 길이
    void 여덟_자_이상은_통과(int length) {
        assertThat(validator.validate(passwordOfLength(length))).isEmpty();
    }

    @ParameterizedTest(name = "{0} → {1}")
    @CsvSource({
            "abcdefgh,    숫자를 포함해야 합니다",
            "12345678,    영문자를 포함해야 합니다",
            "'abcd 1234', 공백을 포함할 수 없습니다"       // 공백이 든 값은 작은따옴표로 감싼다
    })
    void 규칙_하나만_어기면_그_위반만_나온다(String password, String expected) {
        assertThat(validator.validate(password)).containsExactly(expected);
    }

    /** 영문자와 숫자를 모두 포함하는, 원하는 길이의 비밀번호 */
    private static String passwordOfLength(int length) {
        return "a".repeat(length - 1) + "1";
    }
}
