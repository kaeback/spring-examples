package com.example.basic.web05springdatajpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * [교안 2.4] 애플리케이션 시작 시 web05 Spring Data JPA 데모를 단계별로 실행한다.
 * 각 단계 사이에 구분선을 넣어 콘솔에서 SQL 로그를 구분해 보기 쉽게 했다.
 */
@Slf4j
@Component
@Order(2)   // web04 데모 다음에 실행
@RequiredArgsConstructor
public class SpringDataJpaRunner implements CommandLineRunner {

    private final SpringDataJpaDemo demo;

    @Override
    public void run(String... args) {
        log.info("========== [web05] Spring Data JPA 데모 ==========");

        section("2.4 시드 데이터");
        demo.seed();

        section("2.4 기본 CRUD 메서드");
        demo.demoCrud();

        section("3.2 쿼리 메소드");
        demo.demoQueryMethods();

        section("3.3 @Query");
        demo.demoQuery();

        section("4.2 페이징 (Pageable / Page)");
        demo.demoPaging();

        section("4.3 Slice (COUNT 쿼리 없음)");
        demo.demoSlice();

        section("5.2 N+1 문제 (LAZY + 목록 순회)");
        demo.demoNPlusOneProblem();

        section("5.2 N+1 해결 ① fetch join");
        demo.demoFetchJoin();

        section("5.2 N+1 해결 ② @EntityGraph");
        demo.demoEntityGraph();

        section("6.2 변경 감지 (Dirty Checking)");
        demo.demoDirtyChecking();

        section("7.2 DTO 프로젝션");
        demo.demoDto();

        log.info("========== [web05] 끝. 브라우저에서 /ex05/members 도 확인해 보세요 ==========");
    }

    private void section(String title) {
        log.info("----- {} -----", title);
    }
}
