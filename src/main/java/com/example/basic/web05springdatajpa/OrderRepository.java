package com.example.basic.web05springdatajpa;

import com.example.basic.web05springdatajpa.entity.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * [교안 2.7] 주문 리포지토리 — N+1 문제와 그 해결책을 보여 준다.
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * 해결책 ① fetch join — 주문과 회원을 '한 번의 조인 쿼리'로 함께 조회한다.
     * (LAZY 라도 미리 다 가져오므로 이후 order.getMember() 에서 추가 쿼리가 안 나간다)
     */
    @Query("select o from Order o join fetch o.member")
    List<Order> findAllWithMember();

    /**
     * 해결책 ② @EntityGraph — JPQL 을 직접 쓰지 않고 어노테이션만으로 fetch join 효과를 낸다.
     */
    @EntityGraph(attributePaths = "member")
    @Query("select o from Order o")
    List<Order> findAllWithMemberEntityGraph();
}
