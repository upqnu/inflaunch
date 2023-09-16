package com.launcher.inflaunch.dto;

import com.launcher.inflaunch.domain.Course;
import com.launcher.inflaunch.domain.Type;
import com.launcher.inflaunch.domain.User;
import com.launcher.inflaunch.domain.Video;
import com.launcher.inflaunch.enum_status.CourseStatus;
import com.launcher.inflaunch.enum_status.VideoStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CourseMapper {

    public Course courseCreateDtoToCourse(CourseCreateDto courseCreateDto, User authorizedUser, Type inputtedType) {

        List<Video> videoList = Optional.ofNullable(courseCreateDto.getVideoList())
                .orElse(Collections.emptyList())
                .stream()
                .map(this::videoCreateDtoToVideo)
                .collect(Collectors.toList());

        Course course = Course.builder()
                .user(authorizedUser)
                .type(inputtedType)
                .title(courseCreateDto.getTitle())
                .description(courseCreateDto.getDescription())
                .price(courseCreateDto.getPrice())
                .videoList(videoList)
                .courseStatus(CourseStatus.ACTIVE)
                .build();

        course.getVideoList().forEach(video -> {
            video.setCourse(course);
        });

        return course;
    }

    public Video videoCreateDtoToVideo(VideoCreateDto videoCreateDto) {
        return Video.builder()
                .title(videoCreateDto.getTitle())
                .source(videoCreateDto.getSource())
                .totalLength(videoCreateDto.getTotalLength())
                .lastViewingTime(0)  // lastViewingTime 필드에 대한 값을 0으로 설정
                .videoStatus(VideoStatus.ACTIVE)
                .build();
    }

    public Course coursePatchDtoToCourse(CoursePatchDto coursePatchDto, Course existingCourse, User currentUser) {
        Course updatedCourse = Course.builder()
                .id(existingCourse.getId())
                .user(currentUser)
                .type(coursePatchDto.getType() != null ? coursePatchDto.getType() : existingCourse.getType())
                .title(coursePatchDto.getTitle() != null ? coursePatchDto.getTitle() : existingCourse.getTitle())
                .description(coursePatchDto.getDescription() != null ? coursePatchDto.getDescription() : existingCourse.getDescription())
                .price(coursePatchDto.getPrice() >= 0 ? coursePatchDto.getPrice() : existingCourse.getPrice())
                // .videoList(existingCourse.getVideoList()) // Keep the existing videoList
                .videoList(coursePatchDto.getVideoList() != null && !coursePatchDto.getVideoList().isEmpty()  ? mapVideoCreateDtoListToVideoList(coursePatchDto.getVideoList(), existingCourse) : existingCourse.getVideoList())
                .courseStatus(existingCourse.getCourseStatus()) // Keep the existing courseStatus
                .build();

        // Set the regDate from existingCourse
        updatedCourse.setRegDate(existingCourse.getRegDate());

        return updatedCourse;
    }

    public List<Video> mapVideoCreateDtoListToVideoList(List<VideoCreateDto> videoCreateDtoList, Course existingCourse) {
        List<Video> videoList = new ArrayList<>();
        for (VideoCreateDto videoCreateDto : videoCreateDtoList) {
            Video video = Video.builder()
                    .course(existingCourse)
                    .title(videoCreateDto.getTitle())
                    .source(videoCreateDto.getSource())
                    .totalLength(videoCreateDto.getTotalLength())
                    .videoStatus(VideoStatus.ACTIVE)
                    .lastViewingTime(0)  // lastViewingTime 자동계산 로직을 설정하기 전이므로 0으로 세팅
                    .build();
            videoList.add(video);
        }
        return videoList;
    }

//    public Course courseDeleteDtoToCourse(Course existingCourse) {
//        return Course.builder()
//                .courseStatus(CourseStatus.DELETED)
//                .build();
//    }
}
