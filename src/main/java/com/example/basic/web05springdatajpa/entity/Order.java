package com.example.basic.web05springdatajpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * [교안 2.7] 연관관계 매핑 — 주문 N : 회원 1 (N:1).
 *
 * <p>FK(member_id)를 가진 이 쪽이 '연관관계의 주인'이다.
 * fetch 는 항상 LAZY 가 원칙(@ManyToOne 기본값은 EAGER 이므로 명시적으로 LAZY 지정).
 */
@Entity
@Table(name = "orders")        // 'order' 는 SQL 예약어라 테이블명을 orders 로 지정
@Getter @Setter
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemName;

    @ManyToOne(fetch = FetchType.LAZY)   // 지연 로딩: member 에 실제 접근하는 순간 SELECT
    @JoinColumn(name = "member_id")      // FK 컬럼
    private Member member;

    public Order(Member member, String itemName) {
        this.member = member;
        this.itemName = itemName;
    }
}
