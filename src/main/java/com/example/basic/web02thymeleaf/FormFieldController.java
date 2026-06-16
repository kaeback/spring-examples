package com.example.basic.web02thymeleaf;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * [교안 심화] th:field 로 폼 입력(라디오·체크박스·select) 그리기.
 *
 * <p>web01 의 정적 폼은 선택 상태를 직접 표시해야 했지만, th:field 는 모델 객체의 값과 비교해
 * 일치하는 입력을 '자동으로' checked/selected 처리한다. (수정 폼에서 기존 값 복원에 특히 유용)
 *
 * 브라우저로 확인: GET http://localhost:8080/ex02/form
 */
@Controller
@RequestMapping("/ex02")
public class FormFieldController {

    // 선택지(옵션) 데이터 — @ModelAttribute 메서드는 이 컨트롤러의 모든 뷰에 자동으로 모델에 담긴다.
    @ModelAttribute("gradeOptions")
    public List<String> gradeOptions() { return List.of("브론즈", "실버", "골드"); }

    @ModelAttribute("langOptions")
    public List<String> langOptions() { return List.of("Java", "Kotlin", "Python", "JavaScript"); }

    @ModelAttribute("cityOptions")
    public List<String> cityOptions() { return List.of("서울", "부산", "대구", "광주"); }

    @GetMapping("/form")
    public String form(Model model) {
        // 미리 값이 채워진 객체 → th:field 가 이 값들을 화면에서 자동 선택해 준다.
        SurveyForm form = new SurveyForm();
        form.setName("홍길동");
        form.setGrade("실버");
        form.setLangs(List.of("Java", "Python"));
        form.setCity("부산");
        form.setAgree(true);
        model.addAttribute("surveyForm", form);
        return "ex02/form";
    }

    @PostMapping("/form")
    public String submit(@ModelAttribute SurveyForm surveyForm, Model model) {
        model.addAttribute("result", surveyForm);   // 제출 결과를 같은 화면 아래에 표시
        return "ex02/form";
    }
}
