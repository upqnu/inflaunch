package com.launcher.inflaunch.repository;

import com.launcher.inflaunch.domain.Cart;
import com.launcher.inflaunch.domain.Course;
import com.launcher.inflaunch.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CartRepositoryTest {
    PrintWriter pw = new PrintWriter(System.out);

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;


    @Test
    void init() {

        User user1 = userRepository.findByUsername("member1");
        User user2 = userRepository.findByUsername("member2");

        Course course1 = courseRepository.findByTitle("JAVA 기초");
        Course course2 = courseRepository.findByTitle("MySQL 기초");
        Course course3 = courseRepository.findByTitle("MyBatis 기초");
        Course course4 = courseRepository.findByTitle("JPA 기초");
        Course course5 = courseRepository.findByTitle("SPRING 기초");
        Cart cart1 = Cart.builder()
                .user(user1)
                .course(course1)
                .build();

        Cart cart2 = Cart.builder()
                .user(user1)
                .course(course2)
                .build();

        Cart cart3 = Cart.builder()
                .user(user1)
                .course(course4)
                .build();

        Cart cart4 = Cart.builder()
                .user(user2)
                .course(course1)
                .build();

        Cart cart5 = Cart.builder()
                .user(user2)
                .course(course5)
                .build();

        cart1 = cartRepository.save(cart1);
        cart2 = cartRepository.save(cart2);
        cart3 = cartRepository.save(cart3);
        cart4 = cartRepository.save(cart4);
        cart5 = cartRepository.save(cart5);

        cartRepository.findAll().forEach(pw::println);

        pw.close();
    }
}