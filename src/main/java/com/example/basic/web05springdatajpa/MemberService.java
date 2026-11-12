package com.example.basic.web05springdatajpa;

import com.example.basic.web05springdatajpa.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * [교안 4.2, 6.2] 서비스 계층 — 트랜잭션 경계와 변경 감지.
 *
 * <p>클래스에 @Transactional(readOnly = true) 를 걸어 두고(조회 최적화),
 * 저장·수정 메서드에만 일반 @Transactional 을 다시 붙여 덮어쓰는 패턴을 쓴다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)   // 기본은 읽기 전용(변경 감지용 스냅샷을 안 만들어 조회에 유리)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional   // 쓰기 작업은 readOnly 를 덮어쓴다
    public Long join(Member member) {
        return memberRepository.save(member).getId();
    }

    public Member findOne(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회원이 없습니다. id=" + id));
    }

    /**
     * [교안 6.2] 변경 감지(Dirty Checking) — save() 를 부르지 않아도 수정된다.
     * 트랜잭션 안에서 조회한 엔티티(영속 상태)의 값을 바꾸면, 커밋 시점에 UPDATE 가 자동 실행된다.
     */
    @Transactional
    public void changeName(Long id, String newName) {
        Member member = memberRepository.findById(id).orElseThrow();
        member.setName(newName);     // 값만 변경
        // memberRepository.save(member);  ← 필요 없다! (커밋 시 변경 감지로 UPDATE)
    }

    /** [교안 4.2] 페이징 — page 는 0부터, 10개씩, id 내림차순. */
    public Page<Member> getPage(String keyword, int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id"));
        return memberRepository.findByNameContaining(keyword, pageable);
    }
}
