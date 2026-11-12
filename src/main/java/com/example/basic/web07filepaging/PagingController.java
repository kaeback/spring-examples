package com.example.basic.web07filepaging;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * [교안 2.4] 페이징 — 페이지 번호를 OFFSET 으로 바꾸는 '계산'을 직접 해 본다.
 *
 * <p>DB 없이 메모리 리스트로 페이징 공식을 체험하는 예제다. 실제 SQL 은 다음과 같다:
 *   SELECT * FROM items ORDER BY id LIMIT {size} OFFSET {offset};   (MySQL/PostgreSQL/H2)
 *
 * <p>핵심 공식:
 *   offset = (page - 1) * size                     // page 는 1부터, size 는 페이지당 개수
 *   전체 페이지 수 = ceil(전체 개수 / size)          // 올림
 *
 * <p>JPA 를 쓰면 이 계산을 Pageable 이 자동으로 해 준다 → web05 Spring Data JPA 참고.
 *
 * 브라우저로 확인: GET http://localhost:8080/ex07/paging?page=2&size=10
 */
@RestController
@RequestMapping("/ex07")
public class PagingController {

    // 1 ~ 95 까지 가짜 데이터 (전체 95개)
    private static final List<String> DATA = IntStream.rangeClosed(1, 95)
            .mapToObj(i -> "데이터-" + i)
            .toList();

    @GetMapping("/paging")
    public Map<String, Object> paging(@RequestParam(defaultValue = "1") int page,
                                      @RequestParam(defaultValue = "10") int size) {
        int total = DATA.size();                                  // 전체 개수 (DB라면 COUNT(*))
        int totalPages = (int) Math.ceil((double) total / size);  // 전체 페이지 수 = 올림(전체 / size)

        int offset = (page - 1) * size;                           // 시작 위치 (LIMIT의 OFFSET)
        int fromIndex = Math.min(offset, total);
        int toIndex = Math.min(offset + size, total);
        List<String> content = DATA.subList(fromIndex, toIndex);  // 현재 페이지에 보여 줄 조각

        // 응답을 보기 좋게 한 객체로 모아 JSON 으로 내려 준다.
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("page", page);
        result.put("size", size);
        result.put("offset", offset);
        result.put("totalElements", total);
        result.put("totalPages", totalPages);
        result.put("hasPrevious", page > 1);
        result.put("hasNext", page < totalPages);
        result.put("content", content);
        return result;
    }
}
