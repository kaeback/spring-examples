package com.example.basic.web10security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * [교안 3.6] 로그인 실패 처리 — 실패 원인(예외 종류)에 따라 다른 메시지를 로그인 화면에 보여 준다.
 */
@Slf4j
@Component
public class Ex10AuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        log.info("[로그인 실패] {}", exception.getClass().getSimpleName());

        String message;
        if (exception instanceof BadCredentialsException) {        // 비밀번호 틀림 (없는 아이디도 여기로 온다)
            message = "아이디 또는 비밀번호가 맞지 않습니다.";
        } else if (exception instanceof LockedException) {          // isAccountNonLocked() == false
            message = "잠긴 계정입니다. 관리자에게 문의하세요.";
        } else {
            message = "로그인에 실패했습니다.";
        }
        // 핸들러는 싱글톤이므로 필드(setDefaultFailureUrl)를 바꾸지 말고 요청마다 직접 리다이렉트한다
        String encoded = URLEncoder.encode(message, StandardCharsets.UTF_8);
        getRedirectStrategy().sendRedirect(request, response, "/ex10/login?error=" + encoded);
    }
}
