package com.example.basic.web01requestresponse;

import lombok.Data;

import java.util.List;

/**
 * [교안 2.5] @ModelAttribute 바인딩 대상 — 다중 값(List) 필드를 가진 폼 객체.
 *
 * <p>폼 필드명이 객체 필드명과 같으면, 체크박스·다중 select 로 들어온 '여러 값'도
 * List 필드에 자동으로 바인딩된다. (단일 값은 그대로 단일 필드에)
 */
@Data
public class SignupForm {
    private String name;            // 텍스트 (단일)
    private String gender;          // 라디오 (단일)
    private List<String> hobbies;   // 체크박스 (다중)
    private List<String> cities;    // 다중 select (다중)
}
