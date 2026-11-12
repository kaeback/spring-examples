package com.example.basic.test02unitintegration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;  // Boot 4 패키지
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * [테스트 2장 2.6] @SpringBootTest + MockMvc — 목 없이 진짜 TodoStore 로 "생성 → 조회 → 삭제" 흐름을 검증한다.
 *
 * <p>Boot 4 의 @SpringBootTest 는 MockMvc 를 자동으로 만들지 않으므로 @AutoConfigureMockMvc 를 붙인다.
 */
@SpringBootTest
@AutoConfigureMockMvc        // MockMvc 빈을 등록
class TodoFlowIntegrationTest {

    @Autowired MockMvc mockMvc;

    @Test
    void 생성_조회_삭제() throws Exception {
        // 생성 → 응답의 Location 헤더로 새 자원의 주소를 얻는다
        String location = mockMvc.perform(post("/ex08/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"통합 테스트\"}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");

        // 그 주소로 조회
        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("통합 테스트"));

        // 삭제 → 204, 다시 조회하면 404
        mockMvc.perform(delete(location))
                .andExpect(status().isNoContent());
        mockMvc.perform(get(location))
                .andExpect(status().isNotFound());
    }
}
