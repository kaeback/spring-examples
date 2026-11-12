package com.example.basic.test03tdd;

import java.util.List;
import java.util.function.Predicate;

/**
 * [테스트 3장 2.2·3.3] TDD로 만든 비밀번호 규칙 검사기.
 * 지키지 못한 규칙의 메시지를 모두 돌려준다. 빈 목록이면 통과다.
 */
public class PasswordValidator {

    // 규칙 하나 = 통과 조건 + 어겼을 때 메시지
    private record Rule(Predicate<String> condition, String message) {
    }

    private static final List<Rule> RULES = List.of(
            new Rule(pw -> pw.length() >= 8, "8자 이상이어야 합니다"),
            new Rule(pw -> pw.matches(".*[0-9].*"), "숫자를 포함해야 합니다"),
            new Rule(pw -> pw.matches(".*[A-Za-z].*"), "영문자를 포함해야 합니다"),
            new Rule(pw -> !pw.contains(" "), "공백을 포함할 수 없습니다")
    );

    public List<String> validate(String password) {
        if (password == null || password.isBlank()) {   // 3.3에서 추가: 빈 값은 규칙을 검사하기 전에 막는다
            throw new IllegalArgumentException("비밀번호를 입력하세요");
        }
        return RULES.stream()
                .filter(rule -> !rule.condition().test(password))   // 지키지 못한 규칙만 남긴다
                .map(Rule::message)
                .toList();
    }
}
