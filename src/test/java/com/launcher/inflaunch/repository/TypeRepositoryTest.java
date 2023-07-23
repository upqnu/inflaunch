package com.launcher.inflaunch.repository;

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
    @Test
    void init() {
        Type type1 = Type.builder()
                .category("백엔드")
                .type("JAVA")
                .sequence(1)
                .build();

        Type type2 = Type.builder()
                .category("백엔드")
                .type("MySQL")
                .sequence(2)
                .build();

        Type type3 = Type.builder()
                .category("백엔드")
                .type("MyBatis")
                .sequence(3)
                .build();

        Type type4 = Type.builder()
                .category("백엔드")
                .type("JPA")
                .sequence(4)
                .build();

        Type type5 = Type.builder()
                .category("백엔드")
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