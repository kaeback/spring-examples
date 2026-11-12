package com.example.basic.web01requestresponse;

import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * [교안 2.5] 다양한 요청 파라미터 바인딩 — 폼의 select / radio / checkbox 처리.
 *
 * <p>실무 폼은 단순 String·int 뿐 아니라 다음을 함께 받는다.
 *  - 라디오            : 하나만 선택 → 단일 값(String)
 *  - 체크박스 / 다중 select : 여러 개 선택 → List&lt;String&gt; (또는 String[])
 *  - 전체 파라미터 한 번에   : Map(키당 첫 값) / MultiValueMap(키당 값 리스트)
 *
 * <p>모든 결과는 @RestController 라 JSON 으로 응답한다 → 배열로 들어오는 값을 눈으로 확인할 수 있다.
 *
 * <p>정적 폼으로 직접 제출해 보기: http://localhost:8080/ex01/form.html
 */
@RestController
@RequestMapping("/ex01")
public class FormBindingController {

    /**
     * 폼 전송 처리 — 체크박스/다중 select 는 List 로 받는다.
     *
     * <p>주의(흔한 함정): 체크박스를 '하나도' 선택하지 않으면 그 파라미터는 아예 전송되지 않는다.
     * 그래서 required=false 로 두고, null 이면 빈 리스트로 바꿔 NPE 를 피한다.
     * (List&lt;String&gt; 대신 String[] hobbies 로도 똑같이 받을 수 있다.)
     */
    @PostMapping("/bind")
    public Map<String, Object> bind(
            @RequestParam String name,                              // 텍스트 (단일, 필수)
            @RequestParam(required = false) String gender,          // 라디오 (단일, 미선택 시 null)
            @RequestParam(required = false) List<String> hobbies,   // 체크박스 (다중)
            @RequestParam(required = false) List<String> cities) {  // 다중 select
        Map<String, Object> echo = new LinkedHashMap<>();
        echo.put("name", name);
        echo.put("gender", gender);
        echo.put("hobbies", hobbies == null ? List.of() : hobbies);   // 미선택 → 빈 리스트
        echo.put("cities", cities == null ? List.of() : cities);
        return echo;
    }

    /**
     * 같은 폼을 '객체'로 받기 — @ModelAttribute.
     * 폼 필드명이 객체 필드명과 같으면 다중 값도 List 필드(hobbies, cities)에 자동 바인딩된다.
     */
    @PostMapping("/signup")
    public SignupForm signup(@ModelAttribute SignupForm form) {
        return form;
    }

    /**
     * 전체 파라미터를 Map 으로 — 단, Map&lt;String,String&gt; 은 키마다 '첫 값 하나'만 담는다.
     * 예) GET /ex01/all-params?name=kim&age=20  → {"name":"kim","age":"20"}
     */
    @GetMapping("/all-params")
    public Map<String, String> allParams(@RequestParam Map<String, String> params) {
        return params;
    }

    /**
     * 같은 이름으로 여러 값이 올 때는 MultiValueMap — 키마다 값 '리스트'를 담는다.
     * 예) GET /ex01/multi?tag=java&tag=spring&tag=jpa  → {"tag":["java","spring","jpa"]}
     *
     * <p>주의: MultiValueMap 을 '그대로 반환'하면 스프링이 폼 변환기(FormHttpMessageConverter)를 골라
     * "tag=java&tag=spring..." 형태(form-urlencoded)로 응답한다. JSON 으로 보이려면
     * 아래처럼 평범한 Map 으로 감싸서 반환한다. (파라미터로 '받는' 것은 MultiValueMap 그대로다.)
     */
    @GetMapping("/multi")
    public Map<String, List<String>> multi(@RequestParam MultiValueMap<String, String> params) {
        return new LinkedHashMap<>(params);
    }
}
