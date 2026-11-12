package com.example.basic.web01requestresponse;

import org.springframework.web.bind.annotation.*;

/**
 * [교안 2.1] 요청 매핑 (@RequestMapping)
 *
 * <p>클래스 레벨 @RequestMapping 으로 기본 경로(/ex01/members)를 정하고,
 * 메서드 레벨 매핑으로 세부 경로·HTTP 메서드를 지정한다.
 *
 * <p>@RestController = @Controller + @ResponseBody.
 * 즉 메서드가 반환하는 문자열이 '뷰 이름'이 아니라 '응답 바디 그 자체'가 된다.
 * (이 장은 화면을 그리지 않으므로 결과를 바로 글자로 확인하려고 @RestController 를 썼다.
 *  화면 렌더링은 web02 Thymeleaf 에서 다룬다.)
 *
 * <p>브라우저로 확인:
 *   GET  http://localhost:8080/ex01/members      → list()
 *   GET  http://localhost:8080/ex01/members/1    → detail()
 *   POST http://localhost:8080/ex01/members      → save()  (POST라 브라우저 주소창으론 호출 불가)
 */
@RestController
@RequestMapping("/ex01/members")   // 클래스 레벨: 이 컨트롤러의 모든 경로 앞에 붙는 기본 경로
public class RequestMappingController {

    @GetMapping                    // GET /ex01/members
    public String list() {
        return "회원 목록 조회";
    }

    @GetMapping("/{id}")           // GET /ex01/members/1  → URL 경로의 {id}를 받음
    public String detail(@PathVariable Long id) {
        return "회원 상세 조회 id=" + id;
    }

    @PostMapping                   // POST /ex01/members
    public String save() {
        return "회원 저장";
    }
}
