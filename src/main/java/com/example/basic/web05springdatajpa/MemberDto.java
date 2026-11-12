package com.example.basic.web05springdatajpa;

import com.example.basic.web05springdatajpa.entity.Member;

/**
 * [교안 7.2] 엔티티와 DTO 분리.
 *
 * <p>화면·API 로는 엔티티(Member)를 그대로 내보내지 않고, 필요한 값만 담은 DTO 로 변환한다.
 * (이유: 비밀번호 등 민감 정보 노출, 양방향 연관관계 순환참조, LAZY 직렬화 오류, API 스펙이 엔티티에 묶임)
 *
 * <p>record 로 정의하면 getter·생성자·equals/hashCode 가 자동 생성된다(자바 16+).
 * 또한 @Query 의 DTO 프로젝션(select new ...MemberDto(...))에서도 이 생성자를 사용한다.
 */
public record MemberDto(Long id, String name, int age) {

    /** 엔티티 → DTO 변환 편의 생성자. */
    public MemberDto(Member member) {
        this(member.getId(), member.getName(), member.getAge());
    }
}
