package com.example.basic.web08rest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * [교안 2.6] 비교용 화면 컨트롤러(@Controller) — 같은 할 일 목록을 서버에서 HTML 로 완성해 보낸다(SSR).
 *
 * <p>@RestController 인 TodoApiController 는 데이터(JSON)만 보내고, 화면은 브라우저의
 * 자바스크립트(static/ex08/todos.html)가 그린다.
 */
@Controller
@RequiredArgsConstructor
public class Ex08ViewController {

    private final TodoStore todoStore;

    /** 반환값 "ex08/ssr" 은 응답 본문이 아니라 뷰 이름 → templates/ex08/ssr.html 을 렌더링 */
    @GetMapping("/ex08/ssr")
    public String ssr(Model model) {
        model.addAttribute("todos", todoStore.findAll().stream()
                .map(TodoResponse::from)
                .toList());
        return "ex08/ssr";
    }
}
