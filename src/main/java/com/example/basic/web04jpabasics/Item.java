package com.example.basic.web04jpabasics;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * [교안 JPA 기초] 영속성 컨텍스트 동작을 보기 위한 가장 단순한 엔티티.
 *
 * <p>이 장(web06)은 순수 JPA의 '개념'(생명주기·1차 캐시·변경 감지)을 EntityManager 로 직접 확인한다.
 * 실제 어노테이션 매핑·리포지토리는 다음 장(web07 Spring Data JPA)에서 자세히 다룬다.
 */
@Entity
@Getter @Setter
@NoArgsConstructor          // JPA 는 기본 생성자가 필수
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // PK 를 DB 가 자동 증가
    private Long id;

    private String name;
    private int price;

    public Item(String name, int price) {   // 편의 생성자
        this.name = name;
        this.price = price;
    }
}
