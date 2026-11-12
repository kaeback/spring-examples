package com.example.basic.web10security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * [교안 3.4] 시큐리티가 이해하는 사용자 정보(UserDetails).
 * 우리 회원 객체(Ex10Member)를 감싸서 UserDetails 규격에 맞춘다.
 */
@Getter
public class Ex10UserDetails implements UserDetails {

    private final Ex10Member member;

    public Ex10UserDetails(Ex10Member member) {
        this.member = member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(member.getRole()));   // 예: [ROLE_USER]
    }

    @Override
    public String getPassword() {
        return member.getPassword();      // 암호화된 비밀번호 — 시큐리티가 PasswordEncoder 로 비교한다
    }

    @Override
    public String getUsername() {
        return member.getLoginId();       // 인증 주체를 구분하는 값 = 로그인 아이디
    }

    // isAccountNonExpired·isCredentialsNonExpired·isEnabled 는 기본값(true)을 그대로 쓴다.
    // 잠금 여부만 회원 정보로 결정한다 → false 면 LockedException (교안 3.6)
    @Override
    public boolean isAccountNonLocked() {
        return !member.isLocked();
    }
}
