package com.example.basic.web04jpabasics;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * [교안 2.7] 연관관계의 주인 — 선수 N : 팀 1.
 *
 * <p>FK(team_id)를 가진 이 쪽이 주인이다. team 필드에 넣은 값만 FK 컬럼에 저장된다.
 */
@Entity
@Getter @Setter
@NoArgsConstructor
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)     // 기본값 EAGER 대신 LAZY
    @JoinColumn(name = "team_id")          // FK 컬럼
    private Team team;

    public Player(String name) {
        this.name = name;
    }
}
