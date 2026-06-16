package com.example.basic.web05springdatajpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * [교안 2] 엔티티 매핑.
 *
 * <p>테이블과 매핑되는 클래스를 '엔티티'라고 한다.
 * 변경 감지(dirty checking) 실습을 위해 @Setter 를 열어 두었지만,
 * 실무에서는 무분별한 setter 대신 의미 있는 메서드로 상태를 바꾸는 편이 안전하다.
 */
@Entity
@Getter @Setter
@NoArgsConstructor          // JPA 는 기본 생성자가 필수
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)   // DB 가 PK 자동 증가
    private Long id;

    @Column(nullable = false, length = 20)                // not null + 길이 제약
    private String name;

    private int age;

    private String email;

    @Enumerated(EnumType.STRING)   // enum 을 '문자열'로 저장 (ORDINAL 금지: 순서가 바뀌면 데이터 의미가 깨짐)
    private Role role;

    /**
     * [교안 7] 양방향 연관관계의 '주인이 아닌' 쪽.
     * 실제 FK(member_id)는 Order 가 가지므로, 여기서는 mappedBy 로 '읽기 전용 거울'임을 표시한다.
     * (이 컬렉션에만 add 하고 Order.member 를 안 넣으면 FK 가 저장되지 않으니 주의)
     */
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    public Member(String name, int age, String email, Role role) {   // 편의 생성자
        this.name = name;
        this.age = age;
        this.email = email;
        this.role = role;
    }
}
