package com.example.basic.web01requestresponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * [교안 2.4] HTTP 응답 — 세 가지 응답 방식 + ResponseEntity.
 *
 * <p>스프링 MVC의 응답 방식은 세 가지다.
 *   ① 정적 리소스   : src/main/resources/static 의 파일을 그대로 제공
 *   ② 뷰 템플릿     : 논리 뷰 이름(String)을 반환 → ViewResolver 가 Thymeleaf 렌더링
 *   ③ 메시지 바디   : @ResponseBody → 뷰를 거치지 않고 문자열/JSON 을 바디에 직접
 *
 * <p>이 컨트롤러는 @Controller(뷰 컨트롤러)다. @ResponseBody 가 붙은 메서드는 ③ 메시지 바디 응답,
 * 붙지 않은 메서드(view())는 ② 뷰 템플릿 응답이 된다. (타임리프 문법은 web02 에서 배웠다.)
 */
@Controller
@RequestMapping("/ex01/response")
public class ResponseController {

    /**
     * ② 뷰 템플릿 응답 — @ResponseBody 가 '없으면' 반환 문자열은 '뷰 이름'으로 해석된다.
     * "ex01/response" → templates/ex01/response.html 을 렌더링한다.
     * 예) GET /ex01/response/view
     */
    @GetMapping("/view")
    public String view(Model model) {
        model.addAttribute("message", "컨트롤러가 Model 에 담은 값이 화면에 렌더링되었습니다.");
        return "ex01/response";
    }

    /** ③ @ResponseBody — 반환 문자열을 그대로 응답 바디에 담는다(뷰 해석 X). */
    @GetMapping("/body")
    @ResponseBody
    public String body() {
        return "메시지 바디로 직접 응답";
    }

    /**
     * 객체/컬렉션을 반환하면 Jackson 이 자동으로 JSON 으로 직렬화한다.
     * 예) GET /ex01/response/json → [{"id":1,...}, {"id":2,...}]
     *
     * 주의: 실무에서는 엔티티를 그대로 노출하지 않고 DTO 로 변환해 응답한다. (web05 참고)
     */
    @GetMapping("/json")
    @ResponseBody
    public List<Member> json() {
        return List.of(
                new Member(1L, "kim", 20),
                new Member(2L, "lee", 30)
        );
    }

    /**
     * ResponseEntity — 바디뿐 아니라 '상태 코드'와 '헤더'까지 직접 정해서 응답한다.
     * (ResponseEntity 를 반환하면 @ResponseBody 가 없어도 메시지 바디 응답이 된다.)
     * 예) GET /ex01/response/entity/1   → 200 + {"id":1,"name":"kim","age":20}
     *     GET /ex01/response/entity/99  → 404 + 바디 없음
     */
    @GetMapping("/entity/{id}")
    public ResponseEntity<Member> entity(@PathVariable Long id) {
        if (id > 2) {
            return ResponseEntity.notFound().build();              // 404, 바디 없음
        }
        return ResponseEntity.status(HttpStatus.OK)                // 상태 코드
                .header("X-Sample", "ex01")                        // 응답 헤더 추가
                .body(new Member(id, "kim", 20));                  // 바디(JSON 으로 변환)
    }
}
