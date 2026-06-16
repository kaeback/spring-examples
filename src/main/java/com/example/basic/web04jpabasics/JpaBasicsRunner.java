package com.example.basic.web04jpabasics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 애플리케이션이 시작되면 web04 JPA 기초 데모를 자동 실행한다.
 * 콘솔의 SQL 로그(Hibernate: ...)와 함께 동작을 관찰하자.
 */
@Slf4j
@Component
@Order(1)   // web05 데모(Order 2)보다 먼저 실행
@RequiredArgsConstructor
public class JpaBasicsRunner implements CommandLineRunner {

    private final JpaBasicsDemo demo;

    @Override
    public void run(String... args) {
        // System.out 대신 로거를 쓴다(콘솔 인코딩 차이로 한글이 깨지는 것을 방지)
        log.info("========== [web04] JPA 기초 — 영속성 컨텍스트 데모 ==========");
        demo.run();
        log.info("----- 준영속(detach)과 merge -----");
        demo.demoDetachMerge();
        log.info("========== [web04] 끝 (위 콘솔에서 INSERT/UPDATE/SELECT SQL 을 확인) ==========");
    }
}
