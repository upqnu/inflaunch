package com.launcher.inflaunch.repository;

import com.launcher.inflaunch.domain.Course;
import com.launcher.inflaunch.domain.Review;
import com.launcher.inflaunch.enum_status.Advantage;
import com.launcher.inflaunch.enum_status.Disadvantage;
import com.launcher.inflaunch.enum_status.ReviewStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReviewRepositoryTest {

    PrintWriter pw = new PrintWriter(System.out);

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    void init() {
        Course course1 = courseRepository.findByTitle("JAVA 기초");
        Course course2 = courseRepository.findByTitle("MySQL 기초");
        Course course3 = courseRepository.findByTitle("MyBatis 기초");
        Course course4 = courseRepository.findByTitle("JPA 기초");
        Course course5 = courseRepository.findByTitle("SPRING 기초");

        Review review1 = Review.builder()
                .course(course1)
                .rate(3.5F)
                .reviewWrite("강의가 조금 좋아요")
                .advantage(Advantage.EASY_TO_UNDERSTAND)
                .disadvantage(Disadvantage.LACK_OF_INTERACTION)
                .reviewStatus(ReviewStatus.ACTIVE)
                .build();

        Review review2 = Review.builder()
                .course(course2)
                .rate(3.0F)
                .reviewWrite("DB가 무엇인지 드디어 깨달았다")
                .advantage(Advantage.NOTHING_GOOD)
                .disadvantage(Disadvantage.TOO_FAST_PACE)
                .reviewStatus(ReviewStatus.ACTIVE)
                .build();

        Review review3 = Review.builder()
                .course(course3)
                .rate(4.5F)
                .reviewWrite("아직은 MyBatis를 알아야 할 때")
                .advantage(Advantage.USER_FRIENDLY)
                .disadvantage(Disadvantage.POORLY_ORGANIZED)
                .reviewStatus(ReviewStatus.ACTIVE)
                .build();

        Review review4 = Review.builder()
                .course(course4)
                .rate(4.0F)
                .reviewWrite("이제 JPA는 내 단점이 아닌 장점")
                .advantage(Advantage.WELL_ORGANIZED)
                .disadvantage(Disadvantage.DIFFICULT_TO_UNDERSTAND)
                .reviewStatus(ReviewStatus.ACTIVE)
                .build();

        Review review5 = Review.builder()
                .course(course5)
                .rate(5.0F)
                .reviewWrite("Spring으로 취업문이 넓어졌다")
                .advantage(Advantage.TRENDY)
                .disadvantage(Disadvantage.NOTHING_BAD)
                .reviewStatus(ReviewStatus.ACTIVE)
                .build();

        reviewRepository.save(review1);
        reviewRepository.save(review2);
        reviewRepository.save(review3);
        reviewRepository.save(review4);
        reviewRepository.save(review5);

        reviewRepository.findAll().forEach(pw::println);

        pw.close();
    }

//    @Test
//    void delete() {
//        reviewRepository.deleteAll();
//    }
}