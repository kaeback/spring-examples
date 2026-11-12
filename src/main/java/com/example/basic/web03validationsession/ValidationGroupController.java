package com.example.basic.web03validationsession;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * [교안 검증 4.2] 검증 그룹 비교 — 같은 입력을 세 가지 방식으로 검증해 '어떤 필드가 걸렸는지'를 글자로 돌려준다.
 *
 * <p>화면 없이 curl 로 확인한다. 예)
 *   curl -d "name=" localhost:8080/ex03/groups/save
 *   curl -d "name=" localhost:8080/ex03/groups/update
 *   curl -d "name=" localhost:8080/ex03/groups/valid
 */
@RestController
@RequestMapping("/ex03/groups")
public class ValidationGroupController {

    /** 등록 규칙 — SaveCheck 그룹 제약만 검사한다. (id 는 검사하지 않음) */
    @PostMapping("/save")
    public String save(@Validated(SaveCheck.class) @ModelAttribute MemberForm form,
                       BindingResult bindingResult) {
        return result(bindingResult);
    }

    /** 수정 규칙 — UpdateCheck 그룹 제약만 검사한다. (id 도 필수) */
    @PostMapping("/update")
    public String update(@Validated(UpdateCheck.class) @ModelAttribute MemberForm form,
                         BindingResult bindingResult) {
        return result(bindingResult);
    }

    /**
     * 그룹 없이 @Valid — Default 그룹만 검사한다.
     * MemberForm 의 제약은 모두 groups 를 지정해 두었으므로 Default 에 속한 제약이 없다 → 아무것도 검사되지 않는다(함정).
     */
    @PostMapping("/valid")
    public String valid(@Valid @ModelAttribute MemberForm form,
                        BindingResult bindingResult) {
        return result(bindingResult);
    }

    /** 걸린 필드와 제약 이름을 "필드(제약)" 형태로 모아 정렬해 돌려준다. */
    private String result(BindingResult bindingResult) {
        List<String> errors = bindingResult.getFieldErrors().stream()
                .map((FieldError e) -> e.getField() + "(" + e.getCode() + ")")
                .sorted()
                .toList();
        return "오류 " + errors.size() + "개 " + errors;
    }
}
