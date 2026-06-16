package com.example.basic.web05springdatajpa.entity;

/**
 * 회원 권한 enum. 엔티티에서 @Enumerated(EnumType.STRING) 으로 매핑한다.
 */
public enum Role {
    USER, ADMIN
}
