package com.example.basic.web10security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * [교안 3.4] 메모리 회원 저장소. (게시판에서는 MemberRepository 가 DB에서 찾는다)
 *
 * <p>테스트 계정 — 비밀번호는 모두 1234
 *   user   : ROLE_USER
 *   admin  : ROLE_ADMIN
 *   locked : ROLE_USER, 잠긴 계정
 */
@Component
public class Ex10MemberStore {

    private final Map<String, Ex10Member> store = new ConcurrentHashMap<>();

    public Ex10MemberStore(PasswordEncoder passwordEncoder) {
        // 평문 "1234"를 그대로 저장하지 않고 BCrypt 해시로 바꿔 저장한다
        save(new Ex10Member("user", passwordEncoder.encode("1234"), "일반회원", "ROLE_USER", false));
        save(new Ex10Member("admin", passwordEncoder.encode("1234"), "관리자", "ROLE_ADMIN", false));
        save(new Ex10Member("locked", passwordEncoder.encode("1234"), "잠긴회원", "ROLE_USER", true));
    }

    public void save(Ex10Member member) {
        store.put(member.getLoginId(), member);
    }

    public Optional<Ex10Member> findByLoginId(String loginId) {
        return Optional.ofNullable(store.get(loginId));
    }
}
