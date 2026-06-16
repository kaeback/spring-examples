package com.example.basic.web06filterexception;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * API(@RequestBody) 검증 실습용 요청 DTO.
 *
 * <p>@Valid 와 함께 쓰면, 검증 실패 시 MethodArgumentNotValidException 이 던져지고
 * ExControllerAdvice 가 이를 잡아 JSON 오류로 변환한다.
 */
@Data
public class MemberCreateRequest {

    @NotBlank(message = "이름은 필수입니다.")
    private String name;

    @Min(value = 1, message = "나이는 1 이상이어야 합니다.")
    private int age;
}
