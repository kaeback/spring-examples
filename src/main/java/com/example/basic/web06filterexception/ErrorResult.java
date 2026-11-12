package com.example.basic.web06filterexception;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * [교안 2.6] API 오류 응답 DTO.
 *
 * <p>@ExceptionHandler 에서 이 객체를 반환하면 JSON 오류 본문이 클라이언트에 내려간다.
 * 예) {"code":"BAD", "message":"회원이 없습니다. id=101"}
 */
@Data
@AllArgsConstructor          // (code, message)를 받는 생성자 자동 생성
public class ErrorResult {
    private String code;     // 오류 분류 코드 (예: "BAD", "VALIDATION", "INTERNAL")
    private String message;  // 사용자에게 보여 줄 메시지
}
