package com.example.basic.web02thymeleaf;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * [교안 2.1~2.9] Thymeleaf 문법 실습 컨트롤러.
 *
 * <p>각 메서드는 모델에 데이터를 담아 templates/ex02/*.html 을 렌더링한다.
 * 브라우저로 확인: GET http://localhost:8080/ex02
 */
@Controller
@RequestMapping("/ex02")
public class BasicController {

    /** 실습 페이지 목록(허브). */
    @GetMapping
    public String index() {
        return "ex02/index";
    }

    /** [교안 2.1] 텍스트 출력: th:text / [[...]] / utext / 스프링 빈 호출. */
    @GetMapping("/text")
    public String text(Model model) {
        model.addAttribute("data", "Hello <b>Spring</b>");  // <b> 태그가 섞인 문자열 → escape/utext 차이 확인
        return "ex02/text";
    }

    /** [교안 2.2] 변수 표현식(객체/리스트/맵), th:with, 날짜 유틸리티. */
    @GetMapping("/variable")
    public String variable(Model model) {
        User userA = new User("userA", 10);
        User userB = new User("userB", 20);
        model.addAttribute("user", userA);
        model.addAttribute("users", List.of(userA, userB));
        model.addAttribute("userMap", Map.of("userA", userA, "userB", userB));
        model.addAttribute("now", LocalDateTime.now());
        return "ex02/variable";
    }

    /**
     * [교안 2.3] 기본 객체·편의 객체: ${param.xxx}, ${session.xxx}, ${@빈}, #locale.
     * 예) GET /ex02/objects?paramData=HelloParam
     */
    @GetMapping("/objects")
    public String objects(HttpSession session) {
        session.setAttribute("sessionData", "Hello Session");   // ${session.sessionData} 로 꺼낸다
        return "ex02/objects";
    }

    /** [교안 2.4] URL 링크 @{...}: 단순 URL / 쿼리 파라미터 / 경로 변수. */
    @GetMapping("/link")
    public String link(Model model) {
        model.addAttribute("param1", "data1");
        model.addAttribute("param2", "data2");
        return "ex02/link";
    }

    /** [교안 2.5] 리터럴·연산·속성 값 설정. */
    @GetMapping("/literal")
    public String literal(Model model) {
        model.addAttribute("data", "Spring!");
        model.addAttribute("nullData", null);   // Elvis(?:)·No-Operation(_) 확인용
        return "ex02/literal";
    }

    /** [교안 2.6] 반복(th:each) + 조건(th:if/unless/switch) + th:block. */
    @GetMapping("/each")
    public String each(Model model) {
        model.addAttribute("users", List.of(
                new User("userA", 10),
                new User("userB", 20),
                new User("userC", 30)
        ));
        return "ex02/each";
    }

    /** [교안 2.7] 주석과 자바스크립트 인라인(th:inline="javascript"). */
    @GetMapping("/javascript")
    public String javascript(Model model) {
        model.addAttribute("data", "Spring!");
        model.addAttribute("user", new User("userA", 10));
        model.addAttribute("users", List.of(new User("userA", 10), new User("userB", 20)));
        return "ex02/javascript";
    }

    /** [교안 2.8] 템플릿 조각(fragment): th:insert / th:replace / 파라미터 전달. */
    @GetMapping("/fragment")
    public String fragment() {
        return "ex02/fragment";
    }

    /** [교안 2.9] 템플릿 레이아웃: 공통 head 를 조각으로 재사용. */
    @GetMapping("/layout")
    public String layout() {
        return "ex02/layoutMain";
    }
}
