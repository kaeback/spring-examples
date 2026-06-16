package com.example.basic.web02thymeleaf;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * [교안 전체] Thymeleaf 문법 실습 컨트롤러.
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

    /** [교안 2,4] 텍스트 출력: th:text / [[...]] / utext / 스프링 빈 호출. */
    @GetMapping("/text")
    public String text(Model model) {
        model.addAttribute("data", "Hello <b>Spring</b>");  // <b> 태그가 섞인 문자열 → escape/utext 차이 확인
        return "ex02/text";
    }

    /** [교안 3,5] 변수 표현식(객체/리스트/맵), th:with, 날짜 유틸리티. */
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

    /** [교안 9,10] 반복(th:each) + 조건(th:if/unless/switch). */
    @GetMapping("/each")
    public String each(Model model) {
        model.addAttribute("users", List.of(
                new User("userA", 10),
                new User("userB", 20),
                new User("userC", 30)
        ));
        return "ex02/each";
    }

    /** [교안 13] 템플릿 조각(fragment): th:insert / th:replace / 파라미터 전달. */
    @GetMapping("/fragment")
    public String fragment() {
        return "ex02/fragment";
    }

    /** [교안 14] 템플릿 레이아웃: 공통 head 를 조각으로 재사용. */
    @GetMapping("/layout")
    public String layout() {
        return "ex02/layoutMain";
    }
}
