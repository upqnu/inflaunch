package com.launcher.inflaunch.repository;

import com.launcher.inflaunch.domain.Course;
import com.launcher.inflaunch.domain.Type;
import com.launcher.inflaunch.enum_status.CourseStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Course findByTitle(String course);

    List<Course> findByCourseStatus(CourseStatus courseStatus);
}
