package com.example.demo3.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<Category> categorylist() {
        log.info("카테고리 목록 서비스 시작");
        List<Category> categoryList = categoryRepository.findAll();
        log.info("카테고리 목록 조회 성공 - 총 {} 개 ", categoryList.size());
        return categoryList;
    }
}
