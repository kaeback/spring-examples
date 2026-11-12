package com.example.basic.web05springdatajpa;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * [교안 4.2, 7.2] REST API — 엔티티가 아니라 DTO 로 응답하고, 페이징을 요청 파라미터로 받는다.
 *
 * <p>시작 시 SpringDataJpaRunner 가 회원을 미리 저장해 두므로 바로 조회된다.
 *
 * <p>브라우저로 확인:
 *  - GET /ex05/members                              → 전체(DTO 리스트)
 *  - GET /ex05/members/page?keyword=kim&page=0&size=5&sort=id,desc  → 페이징
 *  - GET /ex05/members/search?ageFrom=25&ageTo=30&keyword=kim       → 복합 조건 검색
 */
@RestController
@RequestMapping("/ex05")
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberRepository memberRepository;

    @GetMapping("/members")
    public List<MemberDto> list() {
        // 엔티티 → DTO 변환 후 응답 (엔티티 직접 노출 금지)
        return memberRepository.findAll().stream()
                .map(MemberDto::new)
                .toList();
    }

    /**
     * 스프링 MVC 가 ?page=0&size=10&sort=id,desc 를 Pageable 로 자동 바인딩한다.
     * @PageableDefault 로 파라미터가 없을 때의 기본값을 지정.
     */
    @GetMapping("/members/page")
    public PagedModel<MemberDto> page(
            @RequestParam(defaultValue = "") String keyword,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        // Page 의 map 으로 내용물(엔티티)을 DTO 로 변환하면 페이징 메타데이터는 그대로 유지된다.
        Page<MemberDto> page = memberRepository.findByNameContaining(keyword, pageable).map(MemberDto::new);
        // Page 를 그대로 JSON 으로 내보내면 '구조가 바뀔 수 있다'는 경고가 출력된다 → 모양이 고정된 PagedModel 로 감싼다
        return new PagedModel<>(page);
    }

    /**
     * 복합 조건 검색 — 이름 부분일치 + 나이 범위. (선택 파라미터는 기본값으로 처리)
     * 예) GET /ex05/members/search?keyword=kim&ageFrom=28&ageTo=32
     */
    @GetMapping("/members/search")
    public List<MemberDto> search(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int ageFrom,
            @RequestParam(defaultValue = "200") int ageTo) {
        return memberRepository.findByNameContainingAndAgeBetween(keyword, ageFrom, ageTo).stream()
                .map(MemberDto::new)
                .toList();
    }
}
