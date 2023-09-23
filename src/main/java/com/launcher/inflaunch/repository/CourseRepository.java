package com.launcher.inflaunch.repository;

import com.launcher.inflaunch.domain.Course;
import com.launcher.inflaunch.domain.Type;
import com.launcher.inflaunch.enum_status.CourseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    Course findByTitle(String course);

    Page<Course> findByCourseStatus(CourseStatus courseStatus, Pageable pageable);

//    Page<Course> findAll(Pageable pageable);
}
