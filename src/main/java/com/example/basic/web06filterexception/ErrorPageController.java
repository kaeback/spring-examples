package com.example.basic.web06filterexception;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

/**
 * [교안 4.2] 예외와 오류 페이지.
 *
 * <p>주소마다 다른 방식으로 오류를 낸다. 브라우저로 열어 상태 코드와 오류 화면을 비교하자.
 *  - /ex06/error-page/ex   → 그냥 던진다                    → 500 (5xx 화면이 없으므로 Whitelabel)
 *  - /ex06/error-page/404  → sendError(404)                 → 404 → templates/error/4xx.html
 *  - /ex06/error-page/bad  → ResponseStatusException(400)   → 400 → templates/error/4xx.html
 */
@Controller
@RequestMapping("/ex06/error-page")
public class ErrorPageController {

    /** ① 그냥 던진다 → 아무도 처리하지 않으므로 500 */
    @GetMapping("/ex")
    public String ex() {
        throw new RuntimeException("연습용 예외");
    }

    /** ② 상태 코드를 직접 보낸다 → 404 */
    @GetMapping("/404")
    public void notFound(HttpServletResponse response) throws IOException {
        response.sendError(404, "없는 페이지");
    }

    /** ③ 상태 코드가 담긴 예외를 던진다 → ResponseStatusExceptionResolver 가 400 으로 바꾼다 */
    @GetMapping("/bad")
    public String bad() {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 요청");
    }
}
