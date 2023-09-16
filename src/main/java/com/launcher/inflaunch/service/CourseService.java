package com.launcher.inflaunch.service;

import com.launcher.inflaunch.domain.*;
import com.launcher.inflaunch.dto.CourseCreateDto;
import com.launcher.inflaunch.dto.CourseMapper;
import com.launcher.inflaunch.dto.CoursePatchDto;
import com.launcher.inflaunch.dto.VideoCreateDto;
import com.launcher.inflaunch.enum_status.CourseStatus;
import com.launcher.inflaunch.exception.CourseNotFoundException;
import com.launcher.inflaunch.repository.CourseRepository;
import com.launcher.inflaunch.repository.TypeRepository;
import com.launcher.inflaunch.repository.UserRepository;
import com.launcher.inflaunch.repository.VideoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final TypeRepository typeRepository;
    private final VideoRepository videoRepository;
    private final CourseMapper courseMapper;


    /* 강의를 생성하려는 유저가 mentor인지 여부 판별 */
    @Transactional
    public void proveMentorRole() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다: " + username);
        }

        if (!hasMentorAuthority(user)) {
            throw new AccessDeniedException("강의를 생성할 권한이 없습니다.");
        }
    }

    /* 강의 생성 */
    @Transactional
    public void createCourse(CourseCreateDto courseCreateDto, User authorizedUser) {

        // user가 null 또는 강의 생성 권한이 없을 때(Mentor가 아니라면) 예외 발생
        User checkedUser = userRepository.findById(authorizedUser.getId()).orElse(null);
        if (checkedUser == null || !hasMentorAuthority(checkedUser)) {
            throw new IllegalArgumentException("강의를 생성할 권한이 없습니다.");
        }

        // type이 null인 경우 예외 발생
        Type inputtedType = typeRepository.findBytype(courseCreateDto.getType());
        if (inputtedType == null) {
            throw new IllegalArgumentException("강의의 타입이 선택되지 않았습니다.");
        }

        // title이 이미 존재하는 경우 예외 발생
        Course existingCourse = courseRepository.findByTitle(courseCreateDto.getTitle());
        if (existingCourse != null) {
            throw new IllegalArgumentException("이미 존재하는 제목입니다. 다른 제목으로 수정하세요.");
        }

        // mapper를 통한 새로운 course 생성
        Course newCourse = courseMapper.courseCreateDtoToCourse(courseCreateDto, checkedUser, inputtedType);

        List<VideoCreateDto> videoListDto = courseCreateDto.getVideoList();
        List<Video> videoList = new ArrayList<>();
        if (videoListDto != null && !videoListDto.isEmpty()) {
            for (VideoCreateDto videoCreateDto : videoListDto) {
                // 비디오 정보의 필드 값들이 모두 비어있는지 확인
                if (!videoCreateDto.getTitle().isBlank() && !videoCreateDto.getSource().isBlank() && videoCreateDto.getTotalLength() != null) {
                    Video video = new Video(videoCreateDto.getTitle(), videoCreateDto.getSource(), videoCreateDto.getTotalLength(), newCourse);
                    videoList.add(video);
                }
            }
        }

        courseRepository.save(newCourse);
    }

    /* 유저가 mentor 권한을 가지고 있는지 여부 */
    public boolean hasMentorAuthority(User user) {
        for (Authority authority : user.getAuthorities()) {
            if ("ROLE_MENTOR".equals(authority.getName())) {
                return true;
            }
        }
        return false;
    }

    /* 모든 강의 리스트 */
    public List<Course> getAllCourses() {
        return courseRepository.findByCourseStatus(CourseStatus.ACTIVE);
    }

    /* 개별 강의 페이지 */
    @Transactional
    public Course getCourse(Long courseId) {
        Optional<Course> optionalCourse = courseRepository.findById(courseId);

        if (optionalCourse.isPresent()) {
            Course course = optionalCourse.get();
            if (course.getCourseStatus() == CourseStatus.ACTIVE) {
                return course;
            } else {
                throw new CourseNotFoundException("강의가 존재하지 않습니다.");
            }
        } else {
            throw new CourseNotFoundException("강의가 존재하지 않습니다.");
        }
    }

    /* 강의 수정 */
    @Transactional
    public void updateCourse(Long courseId, CoursePatchDto coursePatchDto) {
        Course existingCourse = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId + " 번 id의 강의가 존재하지 않습니다."));

        if (existingCourse.getCourseStatus() != CourseStatus.ACTIVE) {
            throw new IllegalArgumentException("이 강의는 수정이 불가능한 상태입니다.");
        }

        User currentUser = getCurrentUser();
//        if (!hasAdminAuthority(currentUser) && !existingCourse.getUser().equals(currentUser)) {
//            throw new IllegalArgumentException("이 강의를 수정할 권한이 없습니다.");
//        }
//        if (!hasAdminAuthority(currentUser) &&
//                !(existingCourse.getUser() != null && existingCourse.getUser().getId().equals(currentUser.getId()))) {
//            throw new IllegalArgumentException("이 강의를 수정할 권한이 없습니다.");
//        }
        if (currentUser == null) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        if (!hasAdminAuthority(currentUser) && (existingCourse.getUser() == null || !existingCourse.getUser().getId().equals(currentUser.getId()))) {
            throw new IllegalArgumentException("이 강의를 수정할 권한이 없습니다.");
        }

        Course updatedCourse = courseMapper.coursePatchDtoToCourse(coursePatchDto, existingCourse, currentUser);

        courseRepository.save(updatedCourse);
    }

    /* 유저가 admin 권한을 가지고 있는지 여부 */
    @Transactional
    public boolean hasAdminAuthority(User user) {
        for (Authority authority : user.getAuthorities()) {
            if ((authority.getName()).equals("ROLE_ADMIN")) {
                return true;
            }
        }
        return false;
    }

    /* 로그인한 유저 여부 확인 */
    @Transactional
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null; // No user authenticated or anonymous user
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            return userRepository.findByUsername(userDetails.getUsername());
        }
        return null;
    }

    /* 강의 삭제 */
    @Transactional
    public void deleteCourse(Long courseId) {

        Course existingCourse = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId + " 번 id의 강의가 존재하지 않습니다."));

        if (existingCourse.getCourseStatus() != CourseStatus.ACTIVE) {
            throw new IllegalArgumentException("이 강의는 삭제가 불가능한 상태입니다.");
        }

        existingCourse.setCourseStatus(CourseStatus.DELETED);
        courseRepository.save(existingCourse);
    }
}
