package com.example.basic.web08rest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * [교안 2.3] 생성 요청 본문(JSON).
 * <pre>{"title":"장보기"}</pre>
 * Jackson 이 JSON 을 record 로 바꿔 주고, @Valid 가 아래 제약을 검사한다.
 */
public record TodoCreateRequest(
        @NotBlank(message = "할 일을 입력하세요.")
        @Size(max = 30, message = "할 일은 30자 이하로 입력하세요.")
        String title) {
}
