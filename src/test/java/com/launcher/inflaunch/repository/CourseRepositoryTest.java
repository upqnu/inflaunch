package com.launcher.inflaunch.repository;

import com.launcher.inflaunch.domain.Course;
import com.launcher.inflaunch.domain.Type;
import com.launcher.inflaunch.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CourseRepositoryTest {

    PrintWriter pw = new PrintWriter(System.out);

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TypeRepository typeRepository;

    @Test
    void init() {

        User user1 = userRepository.findByUsername("mentor1");

        Type type1 = typeRepository.findBytype("JAVA");
        Type type2 = typeRepository.findBytype("MySQL");
        Type type3 = typeRepository.findBytype("MyBatis");
        Type type4 = typeRepository.findBytype("JPA");
        Type type5 = typeRepository.findBytype("SPRING");

        Course course1 = Course.builder()
                .user(user1)
                .type(type1)
                .title("JAVA 기초")
                .description("JAVA에 대한 기초 문법을 알려드려요")
                .price(35000)
                .build();

        Course course2 = Course.builder()
                .user(user1)
                .type(type2)
                .title("MySQL 기초")
                .description("MySQL에 대한 기초 문법을 알려드려요")
                .price(55000)
                .build();

        Course course3 = Course.builder()
                .user(user1)
                .type(type3)
                .title("MyBatis 기초")
                .description("MyBatis에 대한 기초 문법을 알려드려요")
                .price(75000)
                .build();

        Course course4 = Course.builder()
                .user(user1)
                .type(type4)
                .title("JPA 기초")
                .description("JPA에 대한 기초 문법을 알려드려요")
                .price(105000)
                .build();

        Course course5 = Course.builder()
                .user(user1)
                .type(type5)
                .title("SPRING 기초")
                .description("SPRING에 대한 기초 문법을 알려드려요")
                .price(135000)
                .build();

        course1 = courseRepository.save(course1);
        course2 = courseRepository.save(course2);
        course3 = courseRepository.save(course3);
        course4 = courseRepository.save(course4);
        course5 = courseRepository.save(course5);

        courseRepository.findAll().forEach(pw::println);

        pw.close();
    }

}