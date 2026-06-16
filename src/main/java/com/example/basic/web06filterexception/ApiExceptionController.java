package com.example.basic.web06filterexception;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * [교안 11] API 예외 처리 실습용 컨트롤러.
 *
 * <p>여기서 던지는 예외는 같은 패키지의 ExControllerAdvice 가 잡아 JSON 오류로 바꿔 준다.
 *
 * <p>브라우저로 확인:
 *  - GET /ex06/api/members/1    → 정상 (회원 반환)
 *  - GET /ex06/api/members/101  → IllegalArgumentException → 400 {"code":"BAD", ...}
 *  - GET /ex06/api/boom         → 일반 예외 → 500 {"code":"INTERNAL", ...}
 *  - POST /ex06/api/members (잘못된 JSON) → 검증 실패 → 400 {"code":"VALIDATION", ...}
 */
@RestController
@RequestMapping("/ex06/api")
public class ApiExceptionController {

    /** id 가 100 보다 크면 '회원 없음'으로 간주해 예외를 던진다. */
    @GetMapping("/members/{id}")
    public String getMember(@PathVariable Long id) {
        if (id > 100) {
            throw new IllegalArgumentException("회원이 없습니다. id=" + id);
        }
        return "회원 조회 성공 id=" + id;
    }

    /** 커스텀 예외 → 404. 같은 '없음'이라도 업무 의미에 맞는 예외로 던지면 상태코드를 정확히 줄 수 있다. */
    @GetMapping("/members/{id}/strict")
    public String getMemberStrict(@PathVariable Long id) {
        if (id > 100) {
            throw new MemberNotFoundException("회원을 찾을 수 없습니다. id=" + id);
        }
        return "회원 조회 성공(strict) id=" + id;
    }

    /** 처리되지 않는 일반 예외 → 500 안전망으로 잡힌다. */
    @GetMapping("/boom")
    public String boom() {
        throw new RuntimeException("일부러 터뜨린 예외");
    }

    /**
     * @RequestBody + @Valid → 검증 실패 시 MethodArgumentNotValidException.
     * 예) POST /ex06/api/members  body: {"name":"", "age":0}
     *     → 400 {"code":"VALIDATION", "message":"name: 이름은 필수입니다., age: 나이는 1 이상이어야 합니다."}
     */
    @PostMapping("/members")
    public String create(@Valid @RequestBody MemberCreateRequest request) {
        return "회원 생성 성공: " + request.getName();
    }
}
