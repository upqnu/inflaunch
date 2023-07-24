package com.launcher.inflaunch.repository;

import com.launcher.inflaunch.domain.Course;
import com.launcher.inflaunch.domain.Type;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {

}
