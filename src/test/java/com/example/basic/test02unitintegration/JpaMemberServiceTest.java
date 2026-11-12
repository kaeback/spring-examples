package com.example.basic.test02unitintegration;

import com.example.basic.web05springdatajpa.MemberRepository;
import com.example.basic.web05springdatajpa.MemberService;
import com.example.basic.web05springdatajpa.entity.Member;
import com.example.basic.web05springdatajpa.entity.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * [테스트 2장 2.5] @Mock / @InjectMocks — web05 의 JPA 서비스를 DB 없이 단위 테스트한다.
 *
 * <p>MemberRepository 는 JpaRepository 를 상속한 인터페이스지만, 목으로 바꾸면 DB 도 스프링도 필요 없다.
 */
@ExtendWith(MockitoExtension.class)   // JUnit(Jupiter)에 Mockito 연동
class JpaMemberServiceTest {

    @Mock                              // 목 객체 자동 생성
    MemberRepository memberRepository;

    @InjectMocks                       // 위 목을 생성자로 주입한 대상 생성
    MemberService memberService;

    @Test
    void 아이디로_조회() {
        when(memberRepository.findById(1L))
                .thenReturn(Optional.of(new Member("kim", 20, "kim@test.com", Role.USER)));

        assertThat(memberService.findOne(1L).getName()).isEqualTo("kim");
    }

    @Test
    void 없는_회원이면_예외() {
        when(memberRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.findOne(99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("회원이 없습니다. id=99");
    }

    @Test
    void 이름_변경은_save를_부르지_않는다() {
        Member member = new Member("kim", 20, "kim@test.com", Role.USER);
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        memberService.changeName(1L, "lee");

        assertThat(member.getName()).isEqualTo("lee");   // 객체 값이 바뀌었다
        verify(memberRepository, never()).save(any());  // UPDATE 는 변경 감지가 할 일 (단위 테스트에서는 확인 불가)
    }
}
