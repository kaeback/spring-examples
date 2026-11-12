package com.example.basic.web06filterexception;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * [교안 2.5] @ExceptionHandler — 예외를 직접 잡아 내가 고른 화면을 보여 준다.
 *
 *  - /ex06/handler/state → 이 컨트롤러 안의 @ExceptionHandler 가 처리      → 409 + ex06/message 화면
 *  - /ex06/handler/todo  → 컨트롤러 안에 핸들러가 없음 → ViewExceptionAdvice → 501 + ex06/message 화면
 */
@Controller
@RequestMapping("/ex06/handler")
public class ExceptionHandlerController {

    @GetMapping("/state")
    public String state() {
        throw new IllegalStateException("이미 처리된 요청입니다.");
    }

    @GetMapping("/todo")
    public String todo() {
        throw new UnsupportedOperationException("인쇄");
    }

    /** 이 컨트롤러 안에서 IllegalStateException 이 나면 아래 메서드가 받는다. */
    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)               // 응답 코드 409
    public String handleState(IllegalStateException e, Model model) {
        model.addAttribute("message", e.getMessage());
        return "ex06/message";                         // 내가 고른 뷰
    }
}
