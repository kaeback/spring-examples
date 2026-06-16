package com.example.basic.web03validationsession;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.util.List;

/**
 * [교안 8] Bean Validation 실습용 폼.
 *
 * <p>요청 데이터의 유효성을 어노테이션으로 '선언적으로' 검증한다.
 * 컨트롤러에서 @Valid(또는 @Validated)를 붙여야 실제 검증이 실행된다.
 *
 * <p><b>검증 그룹 주의점</b>: 컨트롤러에서 @Validated(SaveCheck.class) 처럼 그룹을 지정하면,
 * 그 그룹(SaveCheck)에 속한 제약만 검증한다. 그래서 그룹을 쓸 때는 각 제약에도
 * groups 를 명시해야 한다. groups 를 안 적은 제약은 'Default' 그룹에만 속해
 * SaveCheck 검증에서 빠져 버린다(= 검증이 안 되는 함정).
 *
 * <p>여기서는 일반 항목을 등록·수정 양쪽 그룹에 모두 넣고, id 만 수정(UpdateCheck) 시 필수로 두었다.
 */
@Data
public class MemberForm {

    @NotNull(groups = UpdateCheck.class)   // 수정 시에만 필수 (등록 시엔 검사 안 함)
    private Long id;

    @NotBlank(groups = {SaveCheck.class, UpdateCheck.class}, message = "이름은 필수입니다.")
    @Size(min = 2, max = 20, groups = {SaveCheck.class, UpdateCheck.class},
          message = "이름은 {min}~{max}자로 입력하세요.")   // {min},{max} = 속성 이름 플레이스홀더
    private String name;

    @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
    @Range(min = 1, max = 120, groups = {SaveCheck.class, UpdateCheck.class},
           message = "나이는 {min}~{max} 사이여야 합니다.")   // 숫자 범위(Hibernate Validator)
    private Integer age;

    @Email(groups = {SaveCheck.class, UpdateCheck.class},
           message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @Pattern(regexp = "\\d{3}-\\d{4}-\\d{4}", groups = {SaveCheck.class, UpdateCheck.class},
             message = "전화번호는 010-0000-0000 형식이어야 합니다.")
    private String phone;

    // ── 다중 값·불린 검증 (체크박스) ──

    /**
     * 관심분야 — 체크박스로 여러 개 선택. '최소 1개 선택'은 @NotEmpty 로 강제한다.
     * 주의: @Size(min=1) 은 값이 null 이면 통과시키므로(빈 컬렉션만 검사) '필수 선택'에는 부적합하다.
     *       null·빈 값을 모두 막는 @NotEmpty 를 쓴다.
     */
    @NotEmpty(groups = {SaveCheck.class, UpdateCheck.class},
              message = "관심분야를 최소 1개 선택하세요.")
    private List<String> interests;

    /**
     * 약관 동의 — 단일 체크박스. @AssertTrue 는 값이 true 여야 통과한다.
     * 체크하지 않으면 false(기본값) → 검증 실패.
     */
    @AssertTrue(groups = {SaveCheck.class, UpdateCheck.class},
                message = "약관에 동의해야 가입할 수 있습니다.")
    private boolean agree;
}
