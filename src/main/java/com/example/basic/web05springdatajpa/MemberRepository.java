package com.example.basic.web05springdatajpa;

import com.example.basic.web05springdatajpa.entity.Member;
import com.example.basic.web05springdatajpa.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * [교안 3~6] Spring Data JPA 리포지토리.
 *
 * <p>JpaRepository<엔티티, PK타입> 을 상속한 '인터페이스만' 선언하면
 * save/findById/findAll/count/delete/existsById 같은 기본 CRUD 가 자동 제공된다(구현체 자동 생성).
 */
public interface MemberRepository extends JpaRepository<Member, Long> {

    // ── [교안 4] 쿼리 메소드: 메서드 '이름'만 규칙대로 지으면 SQL 이 자동 생성된다 ──

    List<Member> findByName(String name);                       // WHERE name = ?
    List<Member> findByNameContaining(String keyword);          // WHERE name LIKE %?%
    List<Member> findByAgeGreaterThan(int age);                 // WHERE age > ?
    Optional<Member> findByEmail(String email);                 // 단건 + null 안전(Optional)
    long countByAge(int age);                                   // 개수

    // ── [교안 4 심화] 범위(Between)·목록 포함(In)·복합 조건 ──

    List<Member> findByAgeBetween(int from, int to);            // WHERE age BETWEEN ? AND ?
    List<Member> findByRoleIn(List<Role> roles);               // WHERE role IN (?, ?)
    List<Member> findByNameContainingAndAgeBetween(String keyword, int from, int to);  // 복합 조건

    // ── [교안 5] @Query: 복잡한 조회는 JPQL 을 직접 작성 ──

    @Query("select m from Member m where m.name = :name and m.age > :age")
    List<Member> search(@Param("name") String name, @Param("age") int age);

    /** DTO 프로젝션 — 조회 시점에 바로 DTO 로 받는다(엔티티 전체를 안 가져옴). */
    @Query("select new com.example.basic.web05springdatajpa.MemberDto(m.id, m.name, m.age) from Member m")
    List<MemberDto> findMemberDto();

    // ── [교안 6] 페이징: Pageable 을 받으면 페이징·정렬 SQL + COUNT 쿼리가 자동 생성된다 ──

    Page<Member> findByNameContaining(String keyword, Pageable pageable);

    /**
     * [교안 6 심화] Slice — '더보기/무한 스크롤'에 쓴다.
     * Page 와 달리 전체 개수 COUNT 쿼리를 날리지 않고, size+1 개를 조회해 '다음 페이지 존재 여부'만 판단한다.
     */
    Slice<Member> findByRole(Role role, Pageable pageable);
}
