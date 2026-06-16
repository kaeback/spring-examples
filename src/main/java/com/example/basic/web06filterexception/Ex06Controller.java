package com.example.basic.web06filterexception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 필터·인터셉터 동작을 눈으로 확인하기 위한 엔드포인트 모음.
 *
 * <p>실습 순서:
 *  1) GET /ex06/open            → 공개. 누구나 접근 OK.
 *  2) GET /ex06/secure/data     → 인터셉터 보호. 로그인 전이면 /ex06/login 으로 리다이렉트.
 *  3) GET /ex06/filter-secure/data → 필터 보호. 로그인 전이면 /ex06/login 으로 리다이렉트.
 *  4) GET /ex06/login           → 세션에 로그인 표시 → 다시 2), 3) 접근하면 통과.
 *  5) GET /ex06/logout          → 로그아웃.
 *
 * <p>요청할 때마다 콘솔에 LogFilter / LogInterceptor 로그가 찍히는지 확인하자.
 */
@RestController
@RequestMapping("/ex06")
public class Ex06Controller {

    @GetMapping("/open")
    public String open() {
        return "공개 자원입니다. (인증 불필요)";
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request) {
        HttpSession session = request.getSession();      // 없으면 생성
        session.setAttribute("ex06Login", true);         // 로그인 표시
        return "로그인 완료. 이제 /ex06/secure/data 와 /ex06/filter-secure/data 에 접근할 수 있습니다.";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "로그아웃 완료.";
    }

    @GetMapping("/secure/data")
    public String secureData() {
        return "인터셉터로 보호되는 자원입니다. (로그인 했군요!)";
    }

    @GetMapping("/filter-secure/data")
    public String filterSecureData() {
        return "필터로 보호되는 자원입니다. (로그인 했군요!)";
    }
}
