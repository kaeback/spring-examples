package com.example.basic.web04jpabasics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * [교안 1.4] 애플리케이션이 시작되면 web04 JPA 기초 데모를 자동 실행한다.
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

        log.info("----- 2.3 생명주기와 쓰기 지연 -----");
        Long id = demo.lifecycle();

        log.info("----- 2.4 1차 캐시와 동일성 -----");
        demo.firstLevelCache(id);

        log.info("----- 2.5 변경 감지 -----");
        demo.dirtyChecking(id);
        log.info("dirtyChecking() 반환 = 커밋 완료");

        log.info("----- 3.3 JPQL 과 자동 flush -----");
        demo.jpql(id);

        log.info("----- 4.2 준영속과 merge -----");
        demo.detachAndMerge(id);
        log.info("detachAndMerge() 반환 = 커밋 완료");

        log.info("----- 5.2 연관관계의 주인 -----");
        demo.owner();

        log.info("========== [web04] 끝 ==========");
    }
}
