package com.launcher.inflaunch.repository;

import com.launcher.inflaunch.domain.Category;
import com.launcher.inflaunch.domain.Type;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TypeRepositoryTest {

    PrintWriter pw = new PrintWriter(System.out);

    @Autowired
    private TypeRepository typeRepository;

    @Autowired
    private CategoryRepository categoryRepository;


    @Test
    void init() {

        Category category1 = categoryRepository.findByCategory("백엔드");
        Category category2 = categoryRepository.findByCategory("프론트엔드");
        Category category3 = categoryRepository.findByCategory("게임 개발");

        Type type1 = Type.builder()
                .category(category1)
                .type("JAVA")
                .sequence(1)
                .build();

        Type type2 = Type.builder()
                .category(category1)
                .type("MySQL")
                .sequence(2)
                .build();

        Type type3 = Type.builder()
                .category(category1)
                .type("MyBatis")
                .sequence(3)
                .build();

        Type type4 = Type.builder()
                .category(category1)
                .type("JPA")
                .sequence(4)
                .build();

        Type type5 = Type.builder()
                .category(category1)
                .type("SPRING")
                .sequence(5)
                .build();

        type1 = typeRepository.save(type1);
        type2 = typeRepository.save(type2);
        type3 = typeRepository.save(type3);
        type4 = typeRepository.save(type4);
        type5 = typeRepository.save(type5);

        typeRepository.findAll().forEach(pw::println);

        pw.close();
    }


}