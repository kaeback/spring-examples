package com.example.basic.test02unitintegration;

import com.example.basic.web05springdatajpa.MemberService;
import com.example.basic.web05springdatajpa.entity.Member;
import com.example.basic.web05springdatajpa.entity.Role;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * [테스트 2장 3.4] @SpringBootTest — 애플리케이션 전체를 띄워 진짜 빈(서비스 + JPA + H2)으로 검증한다.
 */
@SpringBootTest          // 스프링 컨테이너를 띄운다
@Transactional           // 테스트가 끝나면 롤백 → 다른 테스트에 데이터가 남지 않는다
class MemberServiceIntegrationTest {

    @Autowired           // 컨테이너가 만든 진짜 빈을 주입
    MemberService memberService;

    @Autowired
    EntityManager em;

    @Test
    void 회원가입_통합() {
        Long id = memberService.join(new Member("tester", 30, "tester@test.com", Role.USER));

        Member found = memberService.findOne(id);

        assertThat(found.getName()).isEqualTo("tester");   // 값으로 비교한다
    }

    @Test
    void 변경_감지_통합() {
        Long id = memberService.join(new Member("before", 30, "before@test.com", Role.USER));

        memberService.changeName(id, "after");             // save() 없이 이름만 바꾼다
        em.flush();   // 지금까지의 변경을 DB 에 SQL 로 반영 (변경 감지 → UPDATE)
        em.clear();   // 영속성 컨텍스트를 비워, 다음 조회가 DB 에서 새로 읽게 한다

        assertThat(memberService.findOne(id).getName()).isEqualTo("after");
    }
}
