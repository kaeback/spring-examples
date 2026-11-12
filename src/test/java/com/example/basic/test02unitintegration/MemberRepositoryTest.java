package com.example.basic.test02unitintegration;

import com.example.basic.web05springdatajpa.MemberRepository;
import com.example.basic.web05springdatajpa.entity.Member;
import com.example.basic.web05springdatajpa.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;   // Boot 4 패키지
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * [테스트 2장 2.4] @DataJpaTest — JPA 관련 빈(엔티티·리포지토리)만 띄워 쿼리 메서드를 검증한다.
 *
 * <p>내장 DB(H2)로 바꿔 실행하고, 테스트마다 트랜잭션을 롤백한다.
 * 앱 시작 시 데이터를 넣는 CommandLineRunner(web04·web05 데모)는 실행되지 않으므로 DB 는 비어 있다.
 */
@DataJpaTest
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        memberRepository.save(new Member("kim", 25, "kim@test.com", Role.USER));
        memberRepository.save(new Member("kimchi", 30, "kimchi@test.com", Role.USER));
        memberRepository.save(new Member("lee", 35, "lee@test.com", Role.ADMIN));
    }

    @Test
    void 이름에_포함된_글자로_검색() {
        assertThat(memberRepository.findByNameContaining("kim"))
                .extracting(Member::getName)
                .containsExactlyInAnyOrder("kim", "kimchi");
    }

    @Test
    void 나이_범위와_이름으로_검색() {
        assertThat(memberRepository.findByNameContainingAndAgeBetween("kim", 28, 40))
                .extracting(Member::getName)
                .containsExactly("kimchi");
    }

    @Test
    void 페이징() {
        Page<Member> page = memberRepository.findByNameContaining("",
                PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "id")));   // 첫 페이지, 2개씩, 최신순

        assertThat(page.getTotalElements()).isEqualTo(3);   // 롤백 덕분에 매번 3명으로 시작
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getContent()).extracting(Member::getName)
                .containsExactly("lee", "kimchi");
    }
}
