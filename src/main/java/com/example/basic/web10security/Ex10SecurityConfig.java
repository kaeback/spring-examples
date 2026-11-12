package com.example.basic.web10security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * [교안 2.2] 시큐리티 설정 — SecurityFilterChain 빈.
 *
 * <p>이 실습 프로젝트는 여러 장(ex01~ex10)이 한 애플리케이션에 모여 있으므로,
 * securityMatcher("/ex10/**") 로 이 필터 체인을 /ex10 아래 요청에만 적용한다.
 * 다른 장(/ex01, /ex06, /h2-console ...)은 어떤 시큐리티 체인에도 걸리지 않아 예전처럼 동작한다.
 * (실무 단일 애플리케이션에서는 securityMatcher 없이 모든 요청에 체인을 건다.)
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity                      // @PreAuthorize 사용 (교안 2.5)
@RequiredArgsConstructor
public class Ex10SecurityConfig {

    private final Ex10AuthFailureHandler failureHandler;   // 교안 2.7

    @Bean
    public SecurityFilterChain ex10FilterChain(HttpSecurity http) throws Exception {
        http
            // 이 체인이 담당할 요청: /ex10 과 그 아래 전부
            .securityMatcher("/ex10/**")
            // CSRF 보호는 기본값(켜짐) 그대로 둔다 → 교안 2.8
            // URL별 접근 권한: 위에서부터 비교해 처음 맞는 규칙 하나만 적용된다
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/ex10", "/ex10/login", "/ex10/encode", "/ex10/filters").permitAll()
                .requestMatchers("/ex10/admin/**").hasRole("ADMIN")   // ROLE_ADMIN 권한만
                .anyRequest().authenticated()                         // 나머지는 로그인만 하면
            )
            // 폼 로그인
            .formLogin(form -> form
                .loginPage("/ex10/login")             // 로그인 화면(GET) — 컨트롤러가 그린다
                .loginProcessingUrl("/ex10/login")    // 로그인 처리(POST) — 시큐리티 필터가 처리한다
                .defaultSuccessUrl("/ex10")           // 성공 시 이동 (원래 가려던 곳이 있으면 그곳)
                .failureHandler(failureHandler)       // 실패 시 원인별 메시지 (교안 2.7)
            )
            // 로그아웃 (CSRF가 켜져 있으므로 POST /ex10/logout 만 받는다)
            .logout(logout -> logout
                .logoutUrl("/ex10/logout")
                .logoutSuccessUrl("/ex10?logout")
            );
        return http.build();
    }

    /** 비밀번호 암호화기 — 회원 저장 시 encode, 로그인 시 matches 로 비교한다 (교안 2.3) */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
