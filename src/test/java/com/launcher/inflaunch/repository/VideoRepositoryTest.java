package com.launcher.inflaunch.repository;

import com.launcher.inflaunch.domain.Course;
import com.launcher.inflaunch.domain.Video;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.PrintWriter;

@SpringBootTest
class VideoRepositoryTest {

    PrintWriter pw = new PrintWriter(System.out);

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Test
    void init() {

        Course course1 = courseRepository.findByTitle("JAVA 기초");
        Course course2 = courseRepository.findByTitle("MySQL 기초");
        Course course3 = courseRepository.findByTitle("MyBatis 기초");
        Course course4 = courseRepository.findByTitle("JPA 기초");
        Course course5 = courseRepository.findByTitle("SPRING 기초");

        Video video1 = Video.builder()
                .course(course1)
                .title("JAVA 기초문법 강의영상")
                .source("https://www.youtube.com/watch?v=NQq0dOoEPUM")
                .totalLength(36000)
                .build();

        Video video2 = Video.builder()
                .course(course2)
                .title("MySQL 기초문법 강의영상")
                .source("https://www.youtube.com/watch?v=dgpBXNa9vJc")
                .totalLength(10800)
                .build();

        Video video3 = Video.builder()
                .course(course3)
                .title("MyBatis 기초문법 강의영상")
                .source("https://www.youtube.com/watch?v=4YOk7oLGTKI")
                .totalLength(3600)
                .build();

        Video video4 = Video.builder()
                .course(course4)
                .title("JPA 기초문법 강의영상")
                .source("https://www.youtube.com/watch?v=yfElp7_jtog")
                .totalLength(1800)
                .build();

        Video video5 = Video.builder()
                .course(course5)
                .title("SPRING 기초문법 강의영상")
                .source("https://www.youtube.com/watch?v=AalcVuKwBUM")
                .totalLength(2700)
                .build();

        video1 = videoRepository.save(video1);
        video2 = videoRepository.save(video2);
        video3 = videoRepository.save(video3);
        video4 = videoRepository.save(video4);
        video5 = videoRepository.save(video5);

        videoRepository.findAll().forEach(pw::println);

        pw.close();
    }

}