package com.example.basic.web10security;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * [교안 2.3] 실습용 회원. (게시판에서는 JPA 엔티티 Member 가 이 역할을 한다)
 */
@Getter
@AllArgsConstructor
public class Ex10Member {
    private String loginId;    // 로그인 아이디
    private String password;   // BCrypt로 암호화된 비밀번호
    private String name;       // 표시 이름
    private String role;       // 권한 — "ROLE_" 접두사까지 포함해 저장 (예: ROLE_USER)
    private boolean locked;    // 잠긴 계정인가 (교안 2.7의 LockedException 실습용)
}
