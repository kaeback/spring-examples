package com.example.basic.web03validationsession;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * [교안 쿠키·세션 2.2~3.4] 쿠키와 세션 — 상태를 한 화면(Thymeleaf)에서 확인한다.
 *
 * <p>HTTP 는 무상태(stateless)라 요청 간 상태를 기억하지 못한다.
 *  - 쿠키 : 서버가 Set-Cookie 로 내려보내면 브라우저가 저장했다가 요청마다 보냄. 클라이언트 저장 → 보안 취약.
 *  - 세션 : 중요한 값은 서버에 보관하고, 브라우저엔 추정 불가능한 세션 ID 만 쿠키로 전달. 로그인 유지에 사용.
 *
 * <p>흐름: 쿠키 저장/세션 로그인/로그아웃 같은 '상태 변경'은 처리 후 상태 화면으로 redirect(PRG)하고,
 * 상태 화면(/ex03/state)에서 현재 쿠키·세션 값을 보여 준다.
 *
 * <p>참고: 교안은 로그인을 POST 로 처리하지만, 여기서는 '주소창으로 바로 눌러 보기' 편하도록 GET 으로 했다.
 * 실제 로그인은 POST + Spring Security 가 정석이다.
 *
 * 브라우저로 확인: GET http://localhost:8080/ex03/state
 */
@Controller
@RequestMapping("/ex03")
public class CookieSessionController {

    /**
     * 상태 화면 — 현재 쿠키(memberId)와 세션(loginMember) 값을 읽어 모델에 담고 렌더링한다.
     *  - @CookieValue     : 요청에 담겨 온 쿠키를 꺼낸다(없으면 null).
     *  - @SessionAttribute: 이미 만들어진 세션의 속성을 꺼낸다(새로 만들지 않음, 없으면 null).
     */
    @GetMapping("/state")
    public String state(
            @CookieValue(name = "memberId", required = false) Long memberId,
            @SessionAttribute(name = "loginMember", required = false) String loginMember,
            Model model) {
        model.addAttribute("memberId", memberId);
        model.addAttribute("loginMember", loginMember);
        return "ex03/state";
    }

    /** 쿠키 생성 — 응답에 Set-Cookie 로 실어 보낸 뒤 상태 화면으로 이동. */
    @GetMapping("/cookie/set")
    public String setCookie(@RequestParam Long memberId, HttpServletResponse response) {
        Cookie cookie = new Cookie("memberId", String.valueOf(memberId));
        cookie.setMaxAge(60 * 30);   // 유효 시간 30분(초). 0이면 즉시 삭제
        cookie.setHttpOnly(true);    // JS(document.cookie) 접근 차단 → XSS 로 인한 쿠키 탈취 완화
        cookie.setPath("/");         // 쿠키가 전송될 경로
        response.addCookie(cookie);
        return "redirect:/ex03/state";
    }

    /** 쿠키 삭제 — 같은 이름의 쿠키를 MaxAge 0 으로 덮어써서 만료시킨다. */
    @GetMapping("/cookie/clear")
    public String clearCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("memberId", "");
        cookie.setMaxAge(0);         // 즉시 삭제
        cookie.setPath("/");
        response.addCookie(cookie);
        return "redirect:/ex03/state";
    }

    /** 세션 로그인 — 세션에 값 저장 후 상태 화면으로 이동. getSession()은 없으면 새로 만든다. */
    @GetMapping("/session/login")
    public String login(@RequestParam String name, HttpServletRequest request) {
        request.getSession().setAttribute("loginMember", name);
        return "redirect:/ex03/state";
    }

    /** 로그아웃 — 세션 무효화. getSession(false)는 세션이 없으면 null(새로 만들지 않음). */
    @GetMapping("/session/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();    // null 체크 후 무효화 (NPE 방지)
        }
        return "redirect:/ex03/state";
    }

    /**
     * [교안 쿠키·세션 3.4] 세션 정보 — 서버가 세션을 어떻게 관리하는지 들여다본다.
     * getSession(false)로 '조회만' 하므로, 세션이 없으면 새로 만들지 않고 "세션 없음"을 보여 준다.
     */
    @GetMapping("/session/info")
    public String sessionInfo(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            model.addAttribute("sessionId", session.getId());                              // JSESSIONID 쿠키 값과 같다
            model.addAttribute("maxInactiveInterval", session.getMaxInactiveInterval());   // 타임아웃(초). 기본 1800 = 30분
            model.addAttribute("creationTime", toTime(session.getCreationTime()));         // 세션이 만들어진 시각
            model.addAttribute("lastAccessedTime", toTime(session.getLastAccessedTime())); // 직전 요청 시각
            model.addAttribute("loginMember", session.getAttribute("loginMember"));
        }
        return "ex03/session-info";
    }

    /** 밀리초(epoch) → 날짜·시각 */
    private LocalDateTime toTime(long epochMillis) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMillis), ZoneId.systemDefault());
    }
}
