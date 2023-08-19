package com.launcher.inflaunch.dto;

import com.launcher.inflaunch.domain.Course;
import com.launcher.inflaunch.dto.CourseCreateDto;
import com.launcher.inflaunch.dto.CoursePatchDto;
import com.launcher.inflaunch.enum_status.CourseStatus;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {
//    public Course courseCreateDtoToCourse(CourseCreateDto courseCreateDto) {
//        return Course.builder()
//                .id(courseCreateDto.getUserId())
//                .type(courseCreateDto.getType()) // 확인 필요
//                .title(courseCreateDto.getTitle())
//                .description(courseCreateDto.getDescription())
//                .price(courseCreateDto.getPrice())
//                .videoList(courseCreateDto.getVideoList())
//                .courseStatus(CourseStatus.ACTIVE)
//                .build();
//    }

    public Course coursePatchDtoToCourse(CoursePatchDto coursePatchDto, Course existingCourse) {
        return Course.builder()
                .id(coursePatchDto.getId())
                .type(coursePatchDto.getType() != null ? coursePatchDto.getType() : existingCourse.getType())
                .title(coursePatchDto.getTitle() != null ? coursePatchDto.getTitle() : existingCourse.getTitle())
                .description(coursePatchDto.getDescription() != null ? coursePatchDto.getDescription() : existingCourse.getDescription())
                .price(coursePatchDto.getPrice() >= 0 ? coursePatchDto.getPrice() : existingCourse.getPrice())
                .videoList(existingCourse.getVideoList()) // Keep the existing videoList
                .courseStatus(existingCourse.getCourseStatus()) // Keep the existing courseStatus
                .build();
    }

    public Course courseDeleteDtoToCourse(Course existingCourse) {
        return Course.builder()
                .courseStatus(CourseStatus.DELETED)
                .build();
    }
}
