package com.example.basic.web02thymeleaf;

import lombok.Data;

import java.util.List;

/**
 * [교안 5.2] th:field 실습용 폼 객체.
 *
 * <p>th:field 는 단일 값(name/grade/city)뿐 아니라 다중 값(langs)·불린(agree)도
 * 화면에 '자동으로 선택 상태(checked/selected)'로 그려 준다.
 */
@Data
public class SurveyForm {
    private String name;            // 텍스트
    private String grade;           // 라디오 (단일)
    private List<String> langs;     // 체크박스 (다중)
    private String city;            // select (단일)
    private boolean agree;          // 체크박스 (불린, 단일)
}
