package com.launcher.inflaunch.service;

import com.launcher.inflaunch.domain.*;
import com.launcher.inflaunch.dto.CourseCreateDto;
import com.launcher.inflaunch.dto.VideoCreateDto;
import com.launcher.inflaunch.enum_status.CourseStatus;
import com.launcher.inflaunch.enum_status.VideoStatus;
import com.launcher.inflaunch.repository.CourseRepository;
import com.launcher.inflaunch.repository.TypeRepository;
import com.launcher.inflaunch.repository.UserRepository;
import com.launcher.inflaunch.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final TypeRepository typeRepository;
    private final VideoRepository videoRepository;

    @Transactional
    public void proveMentorRole() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new IllegalArgumentException("User not found: " + username);
        }

        if (!hasMentorAuthority(user)) {
            throw new AccessDeniedException("You do not have permission to create lessons");
        }
    }

    @Transactional
    public void createCourse(CourseCreateDto courseCreateDto) {

        // user가 null 또는 강의 생성 권한이 없을 때 예외 발생
        User user = userRepository.findById(courseCreateDto.getUserId()).orElse(null);
        if (user == null || !hasMentorAuthority(user)) {
            throw new IllegalArgumentException("강의를 생성할 권한이 없습니다.");
        }

        // type이 null인 경우 예외 발생
        Type type = typeRepository.findBytype(courseCreateDto.getType());
        if (type == null) {
            throw new IllegalArgumentException("강의의 타입이 선택되지 않았습니다.");
        }

        if (courseCreateDto.getVideoList() == null) {
            courseCreateDto.setVideoList(new ArrayList<>()); // Initialize an empty list
        }

        // 필요한 정보 입력하면서 강의 생성
        Course newCourse = Course.builder()
                .user(user)
                .type(type)
                .title(courseCreateDto.getTitle())
                .description(courseCreateDto.getDescription())
                .price(courseCreateDto.getPrice())
                .courseStatus(CourseStatus.ACTIVE) // 원래 READY여야 하지만 테스트를 위해 ACTIVE로 변경
                .build();

        Course savedCourse = courseRepository.save(newCourse);

        // 강의영상 생성을 위한 정보 입력
        List<VideoCreateDto> videoListDto = courseCreateDto.getVideoList();
        List<Video> videoList = new ArrayList<>();

        for (VideoCreateDto videoCreateDto : videoListDto) {
            Video video = Video.builder()
                    .course(savedCourse)
                    .title(videoCreateDto.getTitle())
                    .source(videoCreateDto.getSource())
                    .totalLength(videoCreateDto.getTotalLength())
                    .videoStatus(VideoStatus.ACTIVE)
                    .build();
            videoList.add(video);
        }

        // 강의영상 저장
        List<Video> savedVideos = videoRepository.saveAll(videoList);
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
