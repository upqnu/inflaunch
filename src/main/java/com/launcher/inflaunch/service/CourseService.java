package com.launcher.inflaunch.service;

import com.launcher.inflaunch.domain.*;
import com.launcher.inflaunch.dto.CourseCreateDto;
import com.launcher.inflaunch.dto.VideoCreateDto;
import com.launcher.inflaunch.enum_status.CourseStatus;
import com.launcher.inflaunch.enum_status.VideoStatus;
import com.launcher.inflaunch.repository.CourseRepository;
import com.launcher.inflaunch.repository.TypeRepository;
import com.launcher.inflaunch.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final TypeRepository typeRepository;

    public void createCourse(CourseCreateDto courseCreateDto) {

        // user가 null 또는 강의 생성 권한이 없을 때 예외 발생
        User user = userRepository.findById(courseCreateDto.getUserId()).orElse(null);
        if (user == null || !hasMentorAuthority(user)) {
            throw new IllegalArgumentException("강의를 생성할 권한이 없습니다.");
        }

        // type이 null인 경우 예외 발생
        Type type = typeRepository.findBytype(courseCreateDto.getTypeName());
        if (type == null) {
            throw new IllegalArgumentException("강의의 타입이 선택되지 않았습니다.");
        }

        // Create lecture video list
        List<VideoCreateDto> videoListDto = courseCreateDto.getVideoList();
        List<Video> videoList = videoListDto.stream()
                .map(videoCreateDto -> Video.builder()
                        .title(videoCreateDto.getTitle())
                        .source(videoCreateDto.getSource())
                        .totalLength(videoCreateDto.getTotalLength())
                        .videoStatus(VideoStatus.ACTIVE)
                        .build())
                .collect(Collectors.toList());

        // 필요한 정보 입력하면서 강의 생성
        Course newCourse = Course.builder()
                .user(user)
                .type(type)
                .title(courseCreateDto.getTitle())
                .description(courseCreateDto.getDescription())
                .price(courseCreateDto.getPrice())
                .videoList(videoList)
                .courseStatus(CourseStatus.READY)
                .build();

//        // 업로드할 강의영상에 대한 정보
//        List<VideoCreateDto> videoList = courseCreateDto.getVideoList();
//        for (VideoCreateDto videoCreateDto : videoList) {
//            String videoTitle = videoCreateDto.getTitle();
//            String videoSource = videoCreateDto.getSource();
//            int videoTotalLength = videoCreateDto.getTotalLength();
//
//            // 필요한 정보 입력하면서 강의영상 생성(업로드)
//            Video video = Video.builder()
//                    .course(newCourse) // 강의 영상이 담길 강의를 지정
//                    .title(videoTitle)
//                    .source(videoSource)
//                    .totalLength(videoTotalLength)
//                    .videoStatus(VideoStatus.ACTIVE)
//                    .build();
//
//            // Add the video to the course's video list
//            newCourse.addVideo(video);
//        }

        courseRepository.save(newCourse);
    }

    private boolean hasMentorAuthority(User user) {
        for (Authority authority : user.getAuthorities()) {
            if ("ROLE_MENTOR".equals(authority.getName())) {
                return true;
            }
        }
        return false;
    }

}
