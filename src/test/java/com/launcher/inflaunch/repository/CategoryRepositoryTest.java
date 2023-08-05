package com.launcher.inflaunch.repository;

import com.launcher.inflaunch.domain.Category;
import com.launcher.inflaunch.domain.Type;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoryRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    void init() {
        Category category1 = Category.builder()
                .category("백엔드")
                .build();

        Category category2 = Category.builder()
                .category("프론트엔드")
                .build();

        Category category3 = Category.builder()
                .category("게임 개발")
                .build();

        Category category4 = Category.builder()
                .category("AI")
                .build();

        category1 = categoryRepository.save(category1);
        category2 = categoryRepository.save(category2);
        category3 = categoryRepository.save(category3);
        category4 = categoryRepository.save(category4);
    }

}