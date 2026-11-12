package com.example.basic.test02unitintegration;

import com.example.basic.web08rest.Todo;
import com.example.basic.web08rest.TodoApiController;
import com.example.basic.web08rest.TodoStore;
import com.example.basic.web10security.Ex10AuthFailureHandler;
import com.example.basic.web10security.Ex10SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;           // Boot 4 패키지
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * [테스트 2장 2.5] @WebMvcTest + jsonPath — REST API 의 상태 코드·헤더·JSON 본문을 검증한다.
 *
 * <p>@RestControllerAdvice(TodoApiAdvice)도 웹 계층이라 함께 등록된다 → 오류 JSON 까지 확인할 수 있다.
 */
@WebMvcTest(TodoApiController.class)
@Import({Ex10SecurityConfig.class, Ex10AuthFailureHandler.class})   // 없으면 부트 기본 보안이 걸려 401·403
class TodoApiControllerTest {

    @Autowired MockMvc mockMvc;

    @MockitoBean TodoStore todoStore;

    @Test
    void 목록은_JSON_배열() throws Exception {
        when(todoStore.findAll()).thenReturn(List.of(new Todo(1L, "장보기"), new Todo(2L, "운동")));

        mockMvc.perform(get("/ex08/api/todos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))          // $ = 응답 JSON 전체 (배열)
                .andExpect(jsonPath("$[0].title").value("장보기"))
                .andExpect(jsonPath("$[0].createdAt").doesNotExist());   // DTO 에 없는 값은 나가지 않는다
    }

    @Test
    void 없는_할_일은_404_JSON() throws Exception {
        when(todoStore.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/ex08/api/todos/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("NOT_FOUND"));
    }

    @Test
    void 생성하면_201과_Location() throws Exception {
        when(todoStore.save("장보기")).thenReturn(new Todo(3L, "장보기"));

        mockMvc.perform(post("/ex08/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"장보기\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/ex08/api/todos/3"))
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.done").value(false));
    }

    @Test
    void 빈_제목은_400이고_저장하지_않는다() throws Exception {
        mockMvc.perform(post("/ex08/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION"))
                .andExpect(jsonPath("$.message").value("할 일을 입력하세요."));

        verify(todoStore, never()).save(anyString());
    }
}
