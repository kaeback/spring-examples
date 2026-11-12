package com.example.basic.test02unitintegration;

import com.example.basic.test01junit.Member;
import com.example.basic.test01junit.MemberService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * [테스트 2장 2.1] 순수 단위 테스트 — 가짜 저장소를 생성자로 넣는다. 스프링 컨테이너가 필요 없다.
 */
class MemberServiceFakeTest {

    @Test
    void 회원을_저장한다() {
        // given : 가짜 Repository를 생성자로 주입 (스프링 컨테이너 X)
        MemberService service = new MemberService(new FakeMemberRepository());

        // when
        Member saved = service.join(new Member(1L, "kim"));

        // then
        assertThat(saved.getName()).isEqualTo("kim");
        assertThat(service.findMembers()).hasSize(1);
    }
}
