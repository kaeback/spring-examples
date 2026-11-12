package com.example.basic.web10security;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * [교안 2.2~3.5, 4.2~4.3] 시큐리티 실습 컨트롤러.
 *
 * <p>실습 순서:
 *  1) GET /ex10/filters  → /ex10 체인에 들어 있는 시큐리티 필터 목록
 *  2) GET /ex10/me       → 로그인 전이면 /ex10/login 으로 리다이렉트
 *  3) user/1234 로 로그인 → /ex10/me 로 돌아와 로그인 사용자 정보 확인
 *  4) GET /ex10/admin/home, /ex10/report → user 는 403, admin 은 통과
 *  5) 틀린 비밀번호, locked/1234 로 로그인 → 원인별 실패 메시지
 */
@Controller
@RequestMapping("/ex10")
@RequiredArgsConstructor
public class Ex10Controller {

    private final PasswordEncoder passwordEncoder;
    private final List<SecurityFilterChain> filterChains;

    /** 홈 — sec: 네임스페이스로 로그인 상태를 보여 준다 (교안 4.3) */
    @GetMapping
    public String home() {
        return "ex10/home";
    }

    /** 로그인 화면(GET). 로그인 처리(POST /ex10/login)는 시큐리티 필터가 하므로 메서드가 없다. */
    @GetMapping("/login")
    public String loginForm() {
        return "ex10/login";
    }

    /** [교안 2.2] 이 앱에 등록된 시큐리티 필터 체인과, 체인 안의 필터를 순서대로 보여 준다 */
    @GetMapping("/filters")
    @ResponseBody
    public String filters() {
        StringBuilder sb = new StringBuilder();
        for (SecurityFilterChain chain : filterChains) {
            if (chain instanceof DefaultSecurityFilterChain dc) {
                sb.append("적용 대상: ").append(dc.getRequestMatcher()).append("\n");
            }
            int no = 1;
            for (Filter filter : chain.getFilters()) {
                sb.append(no++).append(". ").append(filter.getClass().getSimpleName()).append("\n");
            }
        }
        return sb.toString();
    }

    /** [교안 3.4] 같은 비밀번호를 두 번 암호화해 보고, matches 로 비교한다 */
    @GetMapping("/encode")
    @ResponseBody
    public String encode(@RequestParam(defaultValue = "1234") String raw) {
        String hash1 = passwordEncoder.encode(raw);
        String hash2 = passwordEncoder.encode(raw);
        return "encode 1번째 : " + hash1 + "\n"
                + "encode 2번째 : " + hash2 + "\n"
                + "같은 문자열인가 : " + hash1.equals(hash2) + "\n"
                + "matches(raw, hash1) : " + passwordEncoder.matches(raw, hash1) + "\n"
                + "matches(\"0000\", hash1) : " + passwordEncoder.matches("0000", hash1);
    }

    /** [교안 3.5] 로그인한 사용자 정보 꺼내기 — 두 방법 비교 */
    @GetMapping("/me")
    @ResponseBody
    public String me(@AuthenticationPrincipal Ex10UserDetails user) {
        // ① 파라미터로 주입받기 (권장)
        Ex10Member member = user.getMember();
        // ② SecurityContextHolder 에서 직접 꺼내기
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return "@AuthenticationPrincipal → 아이디=" + user.getUsername()
                + ", 이름=" + member.getName() + ", 권한=" + user.getAuthorities() + "\n"
                + "SecurityContextHolder   → name=" + auth.getName()
                + ", 인증 객체=" + auth.getClass().getSimpleName();
    }

    /** [교안 4.2] URL 규칙 hasRole("ADMIN") 으로 보호 */
    @GetMapping("/admin/home")
    @ResponseBody
    public String adminHome() {
        return "관리자 페이지 (URL 규칙 hasRole('ADMIN') 통과)";
    }

    /** [교안 4.2] 메서드에 @PreAuthorize 로 보호 */
    @GetMapping("/report")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public String report() {
        return "관리자 보고서 (@PreAuthorize 통과)";
    }
}
