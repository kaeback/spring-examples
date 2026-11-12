package com.example.basic.test01junit;

import java.util.List;

/**
 * [테스트 1장 2.5] 생성자로 저장소를 받는 서비스.
 *
 * <p>스프링 빈으로 등록하지 않는다(@Service 없음). 생성자 주입 구조라서
 * 테스트가 원하는 저장소(메모리·가짜·목)를 new 로 넣을 수 있다.
 */
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member join(Member member) {
        memberRepository.findById(member.getId()).ifPresent(m -> {
            throw new IllegalStateException("이미 존재하는 회원입니다: " + member.getId());
        });
        return memberRepository.save(member);
    }

    public Member find(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회원이 없습니다: " + id));
    }

    public List<Member> findMembers() {
        return memberRepository.findAll();
    }
}
