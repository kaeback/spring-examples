package com.example.basic.web06filterexception;

import com.example.basic.web06filterexception.filter.LogFilter;
import com.example.basic.web06filterexception.filter.LoginCheckFilter;
import com.example.basic.web06filterexception.interceptor.LogInterceptor;
import com.example.basic.web06filterexception.interceptor.LoginCheckInterceptor;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * [교안 2.2~3.3] 필터·인터셉터 등록 설정.
 *
 * <p>이 실습 프로젝트는 여러 장(ex01~ex07)이 한 애플리케이션에 모여 있으므로,
 * 필터·인터셉터를 "/*" 전체가 아니라 "/ex06/**" 로 좁게 걸어 다른 장에 영향을 주지 않게 했다.
 * (실무 단일 애플리케이션에서는 교안처럼 "/*", "/**" 전체에 거는 경우가 많다.)
 *
 * <p>경로 분리:
 *   - /ex06/secure/**        → 인터셉터(LoginCheckInterceptor)로 보호  (권장 방식)
 *   - /ex06/filter-secure/** → 필터(LoginCheckFilter)로 보호           (필터 방식 비교용)
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    // ─────────────── 필터 등록 ───────────────

    /** 모든 /ex06 요청에 로그 필터 적용. */
    @Bean
    public FilterRegistrationBean<Filter> logFilter() {
        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new LogFilter());
        bean.setOrder(1);                       // 순서. 낮을수록 먼저 실행
        bean.addUrlPatterns("/ex06/*");         // 적용 URL 패턴
        return bean;
    }

    /** /ex06/filter-secure 이하만 로그인 체크(필터 방식). */
    @Bean
    public FilterRegistrationBean<Filter> loginCheckFilter() {
        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new LoginCheckFilter());
        bean.setOrder(2);
        bean.addUrlPatterns("/ex06/filter-secure/*");
        return bean;
    }

    // ─────────────── 인터셉터 등록 ───────────────

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 로그 인터셉터: /ex06 전체. 정적 리소스·에러는 제외.
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/ex06/**")
                .excludePathPatterns("/error");   // 오류 화면용 내부 요청(/error)은 뺀다 → 교안 4.1

        // 로그인 체크 인터셉터: /ex06/secure 이하만. 로그인/공개 경로는 제외.
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/ex06/secure/**");
    }
}
