package com.launcher.inflaunch.controller;

import com.launcher.inflaunch.config.PrincipalDetails;
import com.launcher.inflaunch.domain.Course;
import com.launcher.inflaunch.domain.Review;
import com.launcher.inflaunch.domain.Type;
import com.launcher.inflaunch.domain.User;
import com.launcher.inflaunch.dto.CourseCreateDto;
import com.launcher.inflaunch.dto.CoursePatchDto;
import com.launcher.inflaunch.exception.CourseNotFoundException;
import com.launcher.inflaunch.repository.TypeRepository;
import com.launcher.inflaunch.repository.UserRepository;
import com.launcher.inflaunch.service.CourseService;
import com.launcher.inflaunch.service.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
@RequestMapping("/courses")
@Slf4j
public class CourseController {

    private final CourseService courseService;
    private final TypeRepository typeRepository;
    private final UserRepository userRepository;
    private final ReviewService reviewService;

    /* 강의 생성 정보를 입력하는 폼으로 이동 */
    @GetMapping("/new")
//    @PreAuthorize("hasAuthority('ROLE_MENTOR')")
    public String showCourseCreateForm(Model model, RedirectAttributes redirectAttributes) {

        try {
            boolean hasMentorAuthority = courseService.proveMentorRole();
            model.addAttribute("hasMentorAuthority", hasMentorAuthority);

            List<Type> types = typeRepository.findAll();
            model.addAttribute("types", types);

            model.addAttribute("courseCreateDto", new CourseCreateDto());

//            for (Type type : types) {
//                log.info("Type: {}" + type.getType());
//            }

            return "course/create-course";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "강의를 생성할 권한이 없습니다.");
            return "redirect:/courses";
        }

    }

    /* 강의 생성 */
    @PostMapping("/new")
    public String createCourse(@ModelAttribute CourseCreateDto courseCreateDto, BindingResult result, Model model, Principal principal) {

        String username = principal.getName();
        User user = userRepository.findByUsername(username);

        if (result.hasErrors()) {
            model.addAttribute("org.springframework.validation.BindingResult.courseCreateDto", result);
            return "course/create-course";
        }

        try {
            courseService.createCourse(courseCreateDto, user);
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "course/create-course";
        }

        model.addAttribute("courseCreated", true);
        return "redirect:/courses";
    }

    /* 전체 강의 리스트 페이지 */
    @GetMapping
    public String showCourseList(Model model, Principal principal) {

        List<Course> courses = courseService.getAllCourses();
        model.addAttribute("courses", courses);

        boolean hasMentorAuthority = false;

        try {
            hasMentorAuthority = courseService.proveMentorRole();
        } catch (AccessDeniedException e) {
            hasMentorAuthority = false;
        }

        model.addAttribute("hasMentorAuthority", hasMentorAuthority);

        return "course/courses";
    }

    /* 개별 강의 페이지 */
    @GetMapping("/{id}")
    public String showCourse(@PathVariable Long id, Model model, Principal principal) {

        try {
            Course course = courseService.getCourse(id);
            List<Review> reviews = reviewService.getAllReviews(id);
            model.addAttribute("course", course);
            model.addAttribute("reviews", reviews);
            model.addAttribute("courseId", id);

            boolean hasReviews = !reviews.isEmpty();
            model.addAttribute("hasReviews", hasReviews);

            User currentUser = Optional.ofNullable(principal)
                    .map(Principal::getName)
                    .map(userRepository::findByUsername)
                    .orElse(null);
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("hasMentorAuthority", courseService.hasMentorAuthority(currentUser));
            model.addAttribute("hasAdminAuthority", courseService.hasAdminAuthority(currentUser));

            return "course/course-page";
        } catch (CourseNotFoundException ex) {
            model.addAttribute("errorMessage", "강의가 존재하지 않습니다.");
            return "course/courses";
        }
    }

    /* 강의 수정 폼으로 이동 */
    @GetMapping("/{id}/edit")
    public String showEditCourseForm(@PathVariable Long id, Model model, Principal principal) {
        Course course = courseService.getCourse(id);

        // 사용자 인증(admin 또는 course를 생성한 유저가 아니면 예외 발생)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof PrincipalDetails) {
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            User user = principalDetails.getUser();
             Long userId = user.getId();

            if (!course.getUser().getId().equals(userId)
                    && !authentication.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"))
                    && !authentication.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_MENTOR"))) {
                throw new IllegalArgumentException("수정 권한이 없습니다.");
            }
        }

        // course가 null인지 확인
        if (course == null) {
            throw new IllegalArgumentException("해당 강의를 찾을 수 없습니다.");
        }

        List<Type> allTypes = typeRepository.findAll();

        model.addAttribute("course", course);
        model.addAttribute("allTypes", allTypes);
        model.addAttribute("type", course.getType());
        model.addAttribute("videoList", course.getVideoList());

        return "course/edit-course";
    }

    /* 강의 수정 */
    @PatchMapping("/{id}/edit")
    public String editCourse(@PathVariable Long id, @ModelAttribute CoursePatchDto coursePatchDto, BindingResult bindingResult, Model model, Principal principal) {
        if (bindingResult.hasErrors()) {
            for (FieldError error : bindingResult.getFieldErrors()) {
                log.info("Field: {} - Error: {}", error.getField(), error.getDefaultMessage());
            }
            // 필요한 경우 에러 처리 방법을 추가
            return "course/course-page";
        }

        // coursePatchDto  객체를 출력하여 해당 객체의 내용을 확인
        log.info("CoursePatchDto: {}", coursePatchDto);

//        // 1. 사용자 인증
//        Course course = courseService.getCourse(id);
//        if (!course.getUser().getEmail().equals(principal.getName()) && !principal.getName().equals("admin")) {
//            throw new IllegalArgumentException("수정 권한이 없습니다.");
//        }

        // Print the videoList
        if (coursePatchDto.getVideoList() == null || coursePatchDto.getVideoList().isEmpty()) {
            log.error("강의 영상이 존재하지 않습니다.");
            model.addAttribute("errorMessage", "강의 영상이 존재하지 않습니다.");
            return "course/course-page";
        }

        try {
            courseService.updateCourse(id, coursePatchDto);
        } catch (Exception e) {
            log.error("강의 정보 수정에 실패했습니다.", e);
            model.addAttribute("errorMessage", "강의 정보 수정에 실패했습니다.");
            return "course/course-page";
        }

        return "redirect:/courses/{id}";
    }

    /* 강의 삭제 폼으로 이동 */
    @GetMapping("/{id}/delete")
    public String showDeleteCourse(@PathVariable Long id, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {

        model.addAttribute("courseId", id);
        return "course/delete-course";
    }

    /* 강의 삭제 */
    @PostMapping("/{id}/delete")
    public String deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return "redirect:/courses";
    }
}
