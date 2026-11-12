package com.example.basic.web01requestresponse;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * [교안 4.2, 5.2] Forward 와 Redirect — 그리고 PRG 패턴
 *
 * <p>Forward : 서버 내부에서 다른 자원으로 넘김. 요청 1번, URL 그대로.
 *             스프링에서 컨트롤러가 반환하는 '뷰 이름'은 기본적으로 forward 다(web02).
 * <p>Redirect: 브라우저에게 'Location 으로 다시 요청해'(302). 요청 2번, URL 바뀜.
 *
 * <p>PRG(Post-Redirect-Get): POST 로 상태를 바꾼 뒤 결과 화면을 redirect 로 보여 준다.
 * 그러면 새로고침(F5) 해도 안전한 GET 만 반복되어 '중복 등록/중복 결제'를 막는다.
 */
@Controller
@RequestMapping("/ex01")
public class ForwardRedirectController {

    /**
     * Forward — 서버 안에서 다른 경로로 '같은 요청'을 넘긴다.
     * 요청 속성(request attribute)이 그대로 유지되고, 브라우저 주소는 /ex01/forward 그대로다.
     * 예) GET /ex01/forward → 200 "도착 경로=/ex01/target, 요청 속성 via=forward 로 넘긴 값"
     */
    @GetMapping("/forward")
    public String forward(HttpServletRequest request) {
        request.setAttribute("via", "forward 로 넘긴 값");
        return "forward:/ex01/target";
    }

    /**
     * Redirect — 브라우저에게 302 + Location 을 보내 '새 요청'을 하게 한다.
     * 새 요청이므로 여기서 넣은 요청 속성은 사라진다.
     * 예) GET /ex01/redirect → 302 Location: /ex01/target → 200 "..., 요청 속성 via=null"
     */
    @GetMapping("/redirect")
    public String redirect(HttpServletRequest request) {
        request.setAttribute("via", "redirect 로 넘긴 값");
        return "redirect:/ex01/target";
    }

    /** forward·redirect 의 도착 지점 — 요청 속성이 남아 있는지 글자로 보여 준다. */
    @GetMapping("/target")
    @ResponseBody
    public String target(HttpServletRequest request) {
        return "도착 경로=" + request.getRequestURI() + ", 요청 속성 via=" + request.getAttribute("via");
    }

    /**
     * 주문 처리(POST) 후 결과 페이지로 redirect — PRG 패턴.
     * RedirectAttributes 로 redirect 후에도 데이터를 전달한다.
     *
     * 직접 호출은 POST 라서 주소창으론 안 되고, 아래 간단한 폼 페이지에서 버튼으로 호출한다:
     *   GET /ex01/order  → 주문 폼
     */
    @PostMapping("/order")
    public String order(@RequestParam String item, RedirectAttributes ra) {
        // ... 실제로는 여기서 주문을 저장한다 ...
        Long orderId = 100L;                       // 저장 결과로 받은 PK 라고 가정

        ra.addAttribute("orderId", orderId);       // ① URL 경로/쿼리 변수로 전달 (URL에 노출됨)
        ra.addFlashAttribute("item", item);        // ② 1회성 flash 데이터 (URL에 안 보이고, 한 번 읽으면 사라짐)
        return "redirect:/ex01/order/{orderId}";   // {orderId}는 위 addAttribute 값으로 치환
    }

    /**
     * redirect 도착 지점(GET) — 뷰 템플릿으로 결과 화면을 렌더링한다.
     * flash 로 넘어온 item 은 redirect 대상의 Model 에 자동으로 들어오므로 템플릿에서 ${item} 으로 쓴다.
     * 새로고침해도 이 GET 만 반복되므로 주문이 중복 처리되지 않는다.
     */
    @GetMapping("/order/{orderId}")
    public String orderResult(@PathVariable Long orderId, Model model) {
        model.addAttribute("orderId", orderId);   // 경로 변수는 직접 모델에 담는다
        return "ex01/orderResult";                // templates/ex01/orderResult.html
    }

    /** 위 PRG 를 브라우저에서 눌러 볼 수 있는 주문 폼(타임리프). */
    @GetMapping("/order")
    public String orderForm() {
        return "ex01/orderForm";                  // templates/ex01/orderForm.html
    }
}
