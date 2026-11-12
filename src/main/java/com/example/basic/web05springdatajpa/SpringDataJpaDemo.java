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
 * [교안 2.4~7.2] Spring Data JPA 기능을 콘솔에서 단계별로 확인하는 데모.
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

    /** [교안 2.4] 회원 15명 + 주문 3건을 미리 저장한다. */
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

    /** [교안 2.4] JpaRepository 가 기본으로 주는 메서드. */
    @Transactional(readOnly = true)
    public void demoCrud() {
        log.info("count()        = {}", memberRepository.count());
        log.info("findById(1)    = {}", memberRepository.findById(1L).map(Member::getName).orElse("없음"));
        log.info("findById(99)   = {}", memberRepository.findById(99L).map(Member::getName).orElse("없음"));
        log.info("existsById(15) = {}", memberRepository.existsById(15L));
    }

    /** [교안 3.2] 쿼리 메소드 — 이름 규칙만으로 WHERE 절이 만들어진다. */
    @Transactional(readOnly = true)
    public void demoQueryMethods() {
        log.info("findByName('kim1')                     = {}건", memberRepository.findByName("kim1").size());
        log.info("findByNameContaining('kim1')           = {}건", memberRepository.findByNameContaining("kim1").size());
        log.info("findByAgeGreaterThan(30)               = {}건", memberRepository.findByAgeGreaterThan(30).size());
        log.info("findByEmail('kim3@test.com')           = {}",
                memberRepository.findByEmail("kim3@test.com").map(Member::getName).orElse("없음"));
        log.info("countByAge(21)                         = {}", memberRepository.countByAge(21));
        log.info("findByAgeBetween(25, 30)               = {}건", memberRepository.findByAgeBetween(25, 30).size());
        log.info("findByRoleIn([ADMIN])                  = {}건", memberRepository.findByRoleIn(List.of(Role.ADMIN)).size());
        log.info("findByNameContainingAndAgeBetween(...) = {}건",
                memberRepository.findByNameContainingAndAgeBetween("kim", 28, 32).size());
    }

    /** [교안 3.3] @Query — JPQL 을 직접 쓴다. */
    @Transactional(readOnly = true)
    public void demoQuery() {
        List<Member> result = memberRepository.search("kim12", 30);
        log.info("@Query search('kim12', 30) = {}건", result.size());
    }

    /** [교안 4.2] 페이징 — Page 객체가 전체 개수·페이지 수까지 알려 준다. */
    @Transactional(readOnly = true)
    public void demoPaging() {
        Page<Member> page = memberService.getPage("kim", 0);   // 첫 페이지(0부터), 10개씩
        log.info("총 개수 = {}, 총 페이지 수 = {}, 현재 페이지 = {} (0부터)",
                page.getTotalElements(), page.getTotalPages(), page.getNumber());
        log.info("이번 페이지 {}건: {}", page.getContent().size(),
                page.getContent().stream().map(Member::getName).toList());
        log.info("이전 있음? {}, 다음 있음? {}", page.hasPrevious(), page.hasNext());
    }

    /**
     * [교안 4.3] Slice — '더보기'. 전체 개수 COUNT 쿼리 없이 다음 페이지 존재 여부만 판단한다.
     * USER 권한 회원을 5개씩 조회한다(콘솔에서 COUNT 쿼리가 '없는' 것을 확인).
     */
    @Transactional(readOnly = true)
    public void demoSlice() {
        Slice<Member> slice = memberRepository.findByRole(Role.USER, PageRequest.of(0, 5));
        log.info("Slice 내용 {}건, 다음 더보기 가능? = {}", slice.getContent().size(), slice.hasNext());
    }

    /**
     * [교안 5.2] N+1 '문제' — LAZY 연관을 목록 순회에서 접근하면 추가 쿼리가 건수만큼 나간다.
     * 콘솔에서 SELECT 가 1(주문) + 3(회원) = 4번 나가는 것을 확인하자.
     */
    @Transactional(readOnly = true)
    public void demoNPlusOneProblem() {
        List<Order> orders = orderRepository.findAll();        // SELECT 1번 (주문만)
        log.info("주문 {}건 조회. 이제 각 주문의 회원 이름에 접근합니다", orders.size());
        for (Order o : orders) {
            log.info("주문[{}] → 회원 {}", o.getItemName(), o.getMember().getName());  // 접근 순간 SELECT
        }
    }

    /** [교안 5.2] N+1 해결책 ① fetch join — 조인 한 번으로 주문+회원을 함께 가져온다(SELECT 1번). */
    @Transactional(readOnly = true)
    public void demoFetchJoin() {
        List<Order> orders = orderRepository.findAllWithMember();   // 조인 SELECT 1번
        for (Order o : orders) {
            log.info("주문[{}] → 회원 {}", o.getItemName(), o.getMember().getName());  // 추가 쿼리 없음
        }
    }

    /** [교안 5.2] N+1 해결책 ② @EntityGraph — 어노테이션만으로 같은 효과(left join). */
    @Transactional(readOnly = true)
    public void demoEntityGraph() {
        List<Order> orders = orderRepository.findAllWithMemberEntityGraph();
        for (Order o : orders) {
            log.info("주문[{}] → 회원 {}", o.getItemName(), o.getMember().getName());  // 추가 쿼리 없음
        }
    }

    /**
     * [교안 6.2] 변경 감지 — save() 없이 이름이 바뀐다.
     * 이 메서드에는 @Transactional 을 붙이지 않았다. changeName() 이 자기 트랜잭션에서 커밋된 뒤
     * 다시 조회해야 UPDATE 가 실제로 DB 에 들어갔는지 확인할 수 있기 때문이다.
     */
    public void demoDirtyChecking() {
        Long id = memberRepository.findByName("kim1").get(0).getId();
        log.info("changeName 호출");
        memberService.changeName(id, "kim1-수정됨");   // 내부에서 setter 만 호출
        log.info("changeName 반환 = 커밋 완료");
        String changed = memberRepository.findById(id).orElseThrow().getName();   // 새 트랜잭션에서 DB 조회
        log.info("다시 조회한 이름 = {}", changed);
    }

    /** [교안 7.2] DTO 프로젝션 — 조회 시점에 바로 DTO 로 받는다. */
    @Transactional(readOnly = true)
    public void demoDto() {
        List<MemberDto> dtos = memberRepository.findMemberDto();   // @Query DTO 프로젝션
        log.info("DTO 프로젝션 {}건. 첫 번째 = {}", dtos.size(), dtos.isEmpty() ? "없음" : dtos.get(0));
    }
}
