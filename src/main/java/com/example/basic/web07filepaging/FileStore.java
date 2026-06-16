package com.example.basic.web07filepaging;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 업로드 파일 메타데이터를 보관하는 아주 단순한 메모리 저장소.
 * (실무라면 DB 테이블에 저장한다. 여기서는 학습용으로 메모리에 둔다.)
 */
@Component
public class FileStore {

    private final List<UploadFile> files = new CopyOnWriteArrayList<>();

    public void add(UploadFile file) {
        files.add(file);
    }

    public List<UploadFile> findAll() {
        return files;
    }

    /** 저장명(storeName)으로 원본 파일 정보를 찾는다 (다운로드 시 원본명 복원에 사용). */
    public UploadFile findByStoreName(String storeName) {
        return files.stream()
                .filter(f -> f.storeName().equals(storeName))
                .findFirst()
                .orElse(null);
    }
}
