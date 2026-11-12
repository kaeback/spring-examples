package com.example.basic.web04jpabasics;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * [교안 5.2] 연관관계의 주인이 '아닌' 쪽 — 팀 1 : 선수 N.
 *
 * <p>FK(team_id)는 player 테이블에 있으므로, 이 쪽의 players 는 mappedBy 로 표시한 '읽기 전용 거울'이다.
 * 여기에만 add 해도 FK 는 저장되지 않는다.
 */
@Entity
@Getter
@NoArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "team")          // Player.team 이 연관관계의 주인
    private List<Player> players = new ArrayList<>();

    public Team(String name) {
        this.name = name;
    }
}
