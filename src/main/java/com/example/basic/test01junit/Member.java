package com.example.basic.test01junit;

/**
 * [테스트 1장 2.3] 테스트 연습용 회원 — JPA 엔티티가 아닌 평범한 객체.
 * (web05 의 JPA Member 와는 다른 클래스다)
 */
public class Member {

    private final Long id;
    private final String name;
    private boolean active = true;   // 가입 직후에는 활성 상태

    public Member(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public boolean isActive() { return active; }
    public void deactivate() { this.active = false; }
}
