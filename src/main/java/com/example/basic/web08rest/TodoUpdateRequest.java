package com.example.basic.web08rest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * [교안 2.4] 수정 요청 본문(JSON) — PUT 은 자원 전체를 바꾸므로 모든 값을 받는다.
 * <pre>{"title":"장보기","done":true}</pre>
 */
public record TodoUpdateRequest(
        @NotBlank(message = "할 일을 입력하세요.")
        @Size(max = 30, message = "할 일은 30자 이하로 입력하세요.")
        String title,
        boolean done) {
}
