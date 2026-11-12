package com.example.basic.web07filepaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

/**
 * [교안 2.1~2.3] 파일 업로드 / 다운로드.
 *
 * <p>업로드: multipart/form-data 로 전송된 파일을 MultipartFile 로 받아, 고유 저장명으로 디스크에 저장.
 * <p>다운로드: 저장된 파일을 Resource 로 읽어 응답 바디에 싣고, Content-Disposition 헤더로 첨부 처리.
 *
 * <p>저장 경로는 application.yml 의 file.dir (OS 임시 폴더 하위)이다.
 * 브라우저로 확인: GET http://localhost:8080/ex07/upload
 */
@Slf4j
@Controller
@RequestMapping("/ex07")
@RequiredArgsConstructor
public class FileController {

    private final FileStore fileStore;

    @Value("${file.dir}")
    private String fileDir;

    /** 업로드 폼 + 업로드된 파일 목록을 함께 보여 준다. */
    @GetMapping("/upload")
    public String uploadForm(Model model) {
        model.addAttribute("files", fileStore.findAll());
        return "ex07/uploadForm";
    }

    /** [교안 2.1] 단일 파일 업로드. */
    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) throws IOException {
        if (!file.isEmpty()) {            // 파일 미선택 방어
            storeOne(file);
        }
        return "redirect:/ex07/upload";   // PRG
    }

    /**
     * [교안 2.2] 다중 파일 업로드 — 여러 파일은 같은 이름(files)으로 와 List<MultipartFile> 로 받는다.
     * 폼에서는 input 에 multiple 속성을 준다.
     */
    @PostMapping("/upload-multi")
    public String uploadMulti(@RequestParam List<MultipartFile> files) throws IOException {
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                storeOne(file);
            }
        }
        return "redirect:/ex07/upload";
    }

    /** [교안 2.1] 파일 하나를 고유 저장명으로 디스크에 저장하고 메타데이터를 보관한다(단일·다중 공통). */
    private void storeOne(MultipartFile file) throws IOException {
        Files.createDirectories(Paths.get(fileDir));   // 저장 폴더가 없으면 생성

        String originalName = file.getOriginalFilename();          // 사용자가 올린 원본 이름
        String ext = "";
        if (originalName != null && originalName.contains(".")) {
            ext = originalName.substring(originalName.lastIndexOf("."));
        }
        String storeName = UUID.randomUUID() + ext;                // 서버 저장용 고유 이름

        file.transferTo(new File(fileDir + storeName));            // 실제 디스크에 저장
        fileStore.add(new UploadFile(originalName, storeName));    // 두 이름을 함께 보관

        log.info("업로드 완료 original={}, store={}, size={}bytes, type={}",
                originalName, storeName, file.getSize(), file.getContentType());
    }

    /**
     * [교안 2.3] 다운로드 — 저장명(storeName)으로 파일을 찾아, 원본명(originalName)으로 내려보낸다.
     * 예) GET /ex07/download/{storeName}
     */
    @GetMapping("/download/{storeName}")
    public ResponseEntity<Resource> download(@PathVariable String storeName) throws MalformedURLException {
        UploadFile uploadFile = fileStore.findByStoreName(storeName);   // 목록에 있는 저장명만 허용
        if (uploadFile == null) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new UrlResource("file:" + fileDir + storeName);  // 디스크 파일 → Resource

        // attachment = 다운로드 / inline = 브라우저에서 바로 열기
        // filename*=UTF-8''... 형식(RFC 5987)으로 만들어 한글 파일명이 깨지지 않게 한다
        ContentDisposition contentDisposition = ContentDisposition.attachment()
                .filename(uploadFile.originalName(), StandardCharsets.UTF_8)
                .build();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)   // "그냥 바이트 덩어리" → 저장 대화상자
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
                .body(resource);
    }
}
