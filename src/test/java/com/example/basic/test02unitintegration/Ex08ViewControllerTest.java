package com.example.basic.test02unitintegration;

import com.example.basic.web08rest.Ex08ViewController;
import com.example.basic.web08rest.Todo;
import com.example.basic.web08rest.TodoStore;
import com.example.basic.web10security.Ex10AuthFailureHandler;
import com.example.basic.web10security.Ex10SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;           // Boot 4 패키지
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * [테스트 2장 3.3] @WebMvcTest — 화면 컨트롤러의 상태 코드·뷰 이름·모델·렌더링 결과를 검증한다.
 */
@WebMvcTest(Ex08ViewController.class)   // 웹 계층만 로딩 (가벼움)
@Import({Ex10SecurityConfig.class, Ex10AuthFailureHandler.class})   // 없으면 부트 기본 보안이 걸려 401·403
class Ex08ViewControllerTest {

    @Autowired MockMvc mockMvc;         // 가짜 HTTP 요청 도구

    @MockitoBean TodoStore todoStore;   // 의존 빈은 목으로 대체

    @Test
    void SSR_화면() throws Exception {
        when(todoStore.findAll()).thenReturn(List.of(new Todo(1L, "테스트 공부")));

        mockMvc.perform(get("/ex08/ssr"))                     // GET /ex08/ssr 요청 흉내
                .andExpect(status().isOk())                   // 200 OK 인가
                .andExpect(view().name("ex08/ssr"))           // 뷰 이름
                .andExpect(model().attribute("todos", hasSize(1)))
                .andExpect(content().string(containsString("테스트 공부")));   // 타임리프가 실제로 렌더링한 HTML
    }
}
