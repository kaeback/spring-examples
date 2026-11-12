package com.example.basic.web05springdatajpa;

import com.example.basic.web05springdatajpa.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * [교안 4.2] 페이징 목록 화면 — Page 를 타임리프로 그리고, 페이지 번호를 '블록' 단위로 보여 준다.
 *
 * <p>회원이 15명뿐이라 효과가 보이도록 한 페이지 2명, 한 블록 5페이지로 작게 잡았다.
 * 브라우저로 확인: GET /ex05/members/list , /ex05/members/list?page=5
 */
@Controller
@RequestMapping("/ex05")
@RequiredArgsConstructor
public class MemberListController {

    private static final int BLOCK_SIZE = 5;   // 한 블록에 보여 줄 페이지 번호 개수

    private final MemberRepository memberRepository;

    @GetMapping("/members/list")
    public String list(@RequestParam(defaultValue = "") String keyword,
                       @PageableDefault(size = 2, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                       Model model) {
        Page<Member> members = memberRepository.findByNameContaining(keyword, pageable);
        model.addAttribute("members", members);
        model.addAttribute("keyword", keyword);

        // 페이지 블록 계산 (page 는 0부터)
        int current = members.getNumber();                                // 현재 페이지
        int start = (current / BLOCK_SIZE) * BLOCK_SIZE;                  // 블록 시작 (0, 5, 10 …)
        int end = Math.min(start + BLOCK_SIZE, members.getTotalPages());  // 블록 끝(포함 안 함)
        model.addAttribute("startPage", start);
        model.addAttribute("endPage", end);
        return "ex05/list";
    }
}
