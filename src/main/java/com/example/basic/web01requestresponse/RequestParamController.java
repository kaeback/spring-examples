package com.example.basic.web01requestresponse;

import org.springframework.web.bind.annotation.*;

/**
 * [교안 5] HTTP 요청 파라미터 받기
 *
 * <p>요청 데이터를 받는 4가지 대표 방법을 한 컨트롤러에 모았다.
 *  - @RequestParam   : 쿼리 파라미터·폼 데이터를 개별 변수로
 *  - @PathVariable   : URL 경로 변수 (RequestMappingController 참고)
 *  - @ModelAttribute : 요청 파라미터를 객체에 한 번에 바인딩
 *  - @RequestBody    : HTTP 바디(JSON)를 객체로 변환
 *
 * <p>여기서는 단일 값(String·int) 위주로 다룬다. 라디오·체크박스·다중 select 처럼
 * '여러 값이 배열/List 로 들어오는' 실무형 폼 바인딩은 {@link FormBindingController} 에서 이어서 다룬다.
 */
@RestController
@RequestMapping("/ex01")
public class RequestParamController {

    /**
     * @RequestParam — 쿼리 파라미터를 개별 변수로 받는다.
     * 예) GET /ex01/hello?username=kim&age=20
     */
    @GetMapping("/hello")
    public String hello(@RequestParam String username,
                        @RequestParam int age) {
        return "hello " + username + " (" + age + "세)";
    }

    /**
     * @RequestParam 옵션 — required / defaultValue / name.
     * 기본적으로 파라미터가 없으면 400 오류이므로, 선택 파라미터는 옵션으로 제어한다.
     * 예) GET /ex01/search                → keyword=null, page=1
     *     GET /ex01/search?q=spring&page=2 → keyword=spring, page=2
     */
    @GetMapping("/search")
    public String search(
            @RequestParam(required = false) String keyword,     // 없어도 됨 → null
            @RequestParam(defaultValue = "1") int page,         // 없으면 기본값 1
            @RequestParam(name = "q", required = false) String q) {  // 파라미터명(q)과 변수명을 다르게
        // name="q" 를 줬으므로 ?q=... 값이 q 변수로 들어온다 (keyword 와 별개)
        return "keyword=" + keyword + ", page=" + page + ", q=" + q;
    }

    /**
     * @ModelAttribute — 여러 요청 파라미터를 객체 하나로 묶어 바인딩한다.
     * 예) GET /ex01/model?id=1&name=kim&age=20  → Member{id=1, name=kim, age=20}
     * (@RestController 라서 반환 객체는 JSON 으로 직렬화되어 응답된다.)
     */
    @GetMapping("/model")
    public Member model(@ModelAttribute Member member) {
        return member;
    }

    /**
     * @RequestBody — 요청 바디의 JSON 을 객체로 변환한다(HTTP API 방식).
     * 예) POST /ex01/api/members
     *     Content-Type: application/json
     *     {"id":1, "name":"kim", "age":20}
     */
    @PostMapping("/api/members")
    public Member apiSave(@RequestBody Member member) {
        return member;   // 받은 그대로 JSON 으로 돌려준다(에코)
    }
}
