package com.example.basic.web05springdatajpa;

import com.example.basic.web05springdatajpa.entity.Member;
import com.example.basic.web05springdatajpa.entity.Order;
import com.example.basic.web05springdatajpa.entity.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * [교안 전체] Spring Data JPA 기능을 콘솔에서 단계별로 확인하는 데모.
 *
 * <p>각 메서드를 '따로' @Transactional 로 둔 이유: 메서드마다 새 트랜잭션 = 새 영속성 컨텍스트라,
 * N+1 같은 동작이 1차 캐시에 가려지지 않고 실제 SQL 로 드러난다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SpringDataJpaDemo {

    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final MemberService memberService;

    /** 회원 15명 + 주문 3건을 미리 저장한다. */
    @Transactional
    public void seed() {
        for (int i = 1; i <= 15; i++) {
            Role role = (i % 5 == 0) ? Role.ADMIN : Role.USER;     // 5,10,15 번은 ADMIN
            memberRepository.save(new Member("kim" + i, 20 + i, "kim" + i + "@test.com", role));
        }
        List<Member> all = memberRepository.findAll();
        // 주문 3건 — 서로 다른 회원에게 (N+1 데모용)
        orderRepository.save(new Order(all.get(0), "키보드"));
        orderRepository.save(new Order(all.get(1), "마우스"));
        orderRepository.save(new Order(all.get(2), "모니터"));
        log.info("seed 완료: 회원 {}명, 주문 3건", all.size());
    }

    /** [교안 4,5] 쿼리 메소드 & @Query. */
    @Transactional(readOnly = true)
    public void demoQueryMethods() {
        log.info("findByName('kim1')            = {}건", memberRepository.findByName("kim1").size());
        log.info("findByNameContaining('kim')   = {}건", memberRepository.findByNameContaining("kim").size());
        log.info("findByAgeGreaterThan(30)      = {}건", memberRepository.findByAgeGreaterThan(30).size());
        log.info("countByAge(21)                = {}건", memberRepository.countByAge(21));
        log.info("@Query search('kim1', 0)      = {}건", memberRepository.search("kim1", 0).size());
    }

    /** [교안 6] 페이징 — Page 객체가 전체 개수·페이지 수까지 알려 준다. */
    @Transactional(readOnly = true)
    public void demoPaging() {
        Page<Member> page = memberService.getPage("kim", 0);   // 0페이지, 10개씩
        log.info("총 개수      = {}", page.getTotalElements());
        log.info("총 페이지 수 = {}", page.getTotalPages());
        log.info("현재 페이지  = {} (0부터)", page.getNumber());
        log.info("다음 있음?   = {}", page.hasNext());
        log.info("이번 페이지 내용 {}건", page.getContent().size());
    }

    /**
     * [교안 7] N+1 '문제' — LAZY 연관을 목록 순회에서 접근하면 추가 쿼리가 건수만큼 나간다.
     * 콘솔에서 SELECT 가 1(주문) + 3(회원) = 4번 나가는 것을 확인하자.
     */
    @Transactional(readOnly = true)
    public void demoNPlusOneProblem() {
        List<Order> orders = orderRepository.findAll();        // SELECT 1번 (주문만)
        log.info("주문 {}건 조회. 이제 각 주문의 회원 이름에 접근합니다(→ LAZY 추가 SELECT N번)...", orders.size());
        for (Order o : orders) {
            log.info("   주문[{}] → 회원 {}", o.getItemName(), o.getMember().getName());  // 접근 순간 SELECT
        }
        log.info("위 콘솔에서 회원 SELECT 가 여러 번 나갔으면 그게 N+1 입니다.");
    }

    /** [교안 7] N+1 해결책 ① fetch join — 조인 한 번으로 주문+회원을 함께 가져온다(SELECT 1번). */
    @Transactional(readOnly = true)
    public void demoFetchJoinSolution() {
        List<Order> orders = orderRepository.findAllWithMember();   // 조인 SELECT 1번
        log.info("fetch join 으로 주문+회원을 한 번에 조회. 이제 회원 접근해도 추가 쿼리가 없습니다.");
        for (Order o : orders) {
            log.info("   주문[{}] → 회원 {}", o.getItemName(), o.getMember().getName());  // 추가 쿼리 없음
        }
    }

    /** [교안 9] 변경 감지 — save() 없이 이름이 바뀐다. */
    @Transactional
    public void demoDirtyChecking() {
        Long id = memberRepository.findByName("kim1").get(0).getId();
        memberService.changeName(id, "kim1-수정됨");   // 내부에서 setter 만 호출
        String changed = memberRepository.findById(id).orElseThrow().getName();
        log.info("변경 감지 결과: kim1 → {} (save 호출 없이 UPDATE 됨)", changed);
    }

    /** [교안 10] DTO 변환/프로젝션 — 엔티티를 직접 노출하지 않는다. */
    @Transactional(readOnly = true)
    public void demoDto() {
        List<MemberDto> dtos = memberRepository.findMemberDto();   // @Query DTO 프로젝션
        log.info("DTO 프로젝션 {}건. 첫 번째 = {}", dtos.size(), dtos.isEmpty() ? "없음" : dtos.get(0));
    }

    /** [교안 4 심화] 범위·목록 포함·복합 조건 쿼리 메소드. */
    @Transactional(readOnly = true)
    public void demoSearch() {
        log.info("findByAgeBetween(25,30)              = {}건", memberRepository.findByAgeBetween(25, 30).size());
        log.info("findByRoleIn([ADMIN])               = {}건", memberRepository.findByRoleIn(List.of(Role.ADMIN)).size());
        log.info("findByNameContainingAndAgeBetween   = {}건",
                memberRepository.findByNameContainingAndAgeBetween("kim", 28, 32).size());
    }

    /**
     * [교안 6 심화] Slice — '더보기'. 전체 개수 COUNT 쿼리 없이 다음 페이지 존재 여부만 판단한다.
     * USER 권한 회원을 5개씩 조회한다(콘솔에서 COUNT 쿼리가 '없는' 것을 확인).
     */
    @Transactional(readOnly = true)
    public void demoSlice() {
        Slice<Member> slice = memberRepository.findByRole(Role.USER, PageRequest.of(0, 5));
        log.info("Slice 내용 {}건, 다음 더보기 가능? = {} (Page 와 달리 전체 COUNT 쿼리를 안 날린다)",
                slice.getContent().size(), slice.hasNext());
    }
}
