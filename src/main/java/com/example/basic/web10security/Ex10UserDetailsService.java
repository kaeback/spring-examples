package com.example.basic.web10security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * [교안 3.4] 로그인할 때 시큐리티가 호출하는 사용자 조회 서비스.
 * 아이디로 회원을 찾아 UserDetails 로 돌려준다. 비밀번호 비교는 시큐리티가 한다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class Ex10UserDetailsService implements UserDetailsService {

    private final Ex10MemberStore memberStore;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("[UserDetailsService] loadUserByUsername({})", username);
        Ex10Member member = memberStore.findByLoginId(username)
                // 없으면 null 을 돌려주지 말고 예외를 던진다
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자: " + username));
        return new Ex10UserDetails(member);
    }
}
