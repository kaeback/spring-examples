package com.example.basic.test02unitintegration;

import com.example.basic.test01junit.Member;
import com.example.basic.test01junit.MemberRepository;
import com.example.basic.test01junit.MemberService;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * [테스트 2장 2.2] Mockito 목 — mock() 으로 만들고, when() 으로 응답을 정하고, verify() 로 호출을 확인한다.
 */
class MemberServiceMockTest {

    @Test
    void 아이디로_조회() {
        // ① 목 생성 : 구현 없이 껍데기만 있는 가짜 객체
        MemberRepository repo = mock(MemberRepository.class);

        // ② 스터빙 : "findById(1L)을 부르면 이 회원을 돌려줘"라고 응답을 미리 정의
        when(repo.findById(1L)).thenReturn(Optional.of(new Member(1L, "kim")));

        MemberService service = new MemberService(repo);   // 생성자 주입

        // ③ 실행 & 검증
        assertThat(service.find(1L).getName()).isEqualTo("kim");

        // ④ 호출 검증 : service가 repo.findById(1L)을 정확히 1번 불렀는가
        verify(repo, times(1)).findById(1L);
    }

    @Test
    void 중복이면_저장하지_않는다() {
        MemberRepository repo = mock(MemberRepository.class);
        when(repo.findById(1L)).thenReturn(Optional.of(new Member(1L, "kim")));   // 이미 있는 회원
        MemberService service = new MemberService(repo);

        assertThatThrownBy(() -> service.join(new Member(1L, "lee")))
                .isInstanceOf(IllegalStateException.class);

        verify(repo, never()).save(any());   // save 는 한 번도 불리지 않아야 한다
    }

    @Test
    void 스터빙하지_않은_메서드는_기본값() {
        MemberRepository repo = mock(MemberRepository.class);

        assertThat(repo.findById(99L)).isEmpty();   // Optional → Optional.empty()
        assertThat(repo.findAll()).isEmpty();       // List → 빈 리스트
        assertThat(repo.save(new Member(1L, "kim"))).isNull();   // 그 밖의 객체 → null
    }
}
