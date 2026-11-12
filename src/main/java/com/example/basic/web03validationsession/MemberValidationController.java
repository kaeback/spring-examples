package com.example.basic.web03validationsession;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * [교안 검증 2.2] Bean Validation — 화면(폼) 컨트롤러 흐름.
 *
 * <p>핵심 패턴: @Validated(또는 @Valid) + BindingResult
 *  1) 검증 대상 파라미터 '바로 뒤'에 BindingResult 를 둔다.
 *  2) bindingResult.hasErrors() 면 입력값을 유지한 채 폼으로 되돌린다.
 *  3) 통과하면 저장 후 PRG(redirect)로 완료 화면으로 이동.
 *
 * <p>@Valid vs @Validated:
 *  - 보통은 @Valid 로 충분하다.
 *  - 여기서는 '검증 그룹'(등록/수정 규칙 분리)을 쓰려고 @Validated(SaveCheck.class)를 사용했다.
 *    @Valid 에는 그룹을 지정하는 속성이 없다.
 *
 * <p>타임리프 폼에서 #fields / th:errors / th:errorclass 로 오류를 표시한다(addForm.html).
 *
 * 브라우저로 확인: GET http://localhost:8080/ex03/members/add
 */
@Slf4j
@Controller
@RequestMapping("/ex03/members")
public class MemberValidationController {

    /** 관심분야 체크박스 선택지 — 이 컨트롤러의 모든 뷰에 자동으로 모델에 담긴다. */
    @ModelAttribute("interestOptions")
    public java.util.List<String> interestOptions() {
        return java.util.List.of("백엔드", "프론트엔드", "데이터", "인프라");
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        // 빈 폼 객체를 모델에 담아야 타임리프 th:object / th:field 가 동작한다.
        model.addAttribute("memberForm", new MemberForm());
        return "ex03/addForm";
    }

    @PostMapping("/add")
    public String add(@Validated(SaveCheck.class) @ModelAttribute MemberForm form,
                      BindingResult bindingResult,           // 반드시 검증 대상 바로 뒤!
                      RedirectAttributes ra) {

        // (특정 필드가 아닌) 복합 검증은 여기서 직접 글로벌 오류로 추가할 수도 있다.
        // 예) 이름과 이메일이 똑같으면 안 된다 같은 규칙 → bindingResult.reject(...)
        // (둘 다 비어 있을 때는 이미 필드 오류가 나므로 값이 있을 때만 비교한다)
        if (StringUtils.hasText(form.getName()) && form.getName().equals(form.getEmail())) {
            bindingResult.reject("nameEqualsEmail", "이름과 이메일은 같을 수 없습니다.");
        }

        if (bindingResult.hasErrors()) {
            log.info("검증 오류 = {}", bindingResult);
            return "ex03/addForm";        // 오류 시 폼으로 복귀 (입력값·오류 메시지 유지)
        }

        // ... 실제로는 여기서 저장 ...
        ra.addFlashAttribute("savedName", form.getName());
        return "redirect:/ex03/members/add/complete";    // PRG
    }

    @GetMapping("/add/complete")
    public String complete() {
        return "ex03/complete";
    }
}
