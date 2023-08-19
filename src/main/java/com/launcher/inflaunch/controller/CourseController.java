package com.launcher.inflaunch.controller;

import com.launcher.inflaunch.domain.Course;
import com.launcher.inflaunch.domain.Type;
import com.launcher.inflaunch.domain.User;
import com.launcher.inflaunch.dto.CourseCreateDto;
import com.launcher.inflaunch.dto.CoursePatchDto;
import com.launcher.inflaunch.dto.VideoCreateDto;
import com.launcher.inflaunch.exception.CourseNotFoundException;
import com.launcher.inflaunch.repository.TypeRepository;
import com.launcher.inflaunch.repository.UserRepository;
import com.launcher.inflaunch.service.CourseService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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

@RequiredArgsConstructor
@Controller
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;
    private final TypeRepository typeRepository;
    private final UserRepository userRepository;

    /* 강의 생성 정보를 입력하는 폼으로 이동 */
    @GetMapping("/new")
    @PreAuthorize("hasAuthority('ROLE_MENTOR')")
    public String showCourseCreateForm(Model model, RedirectAttributes redirectAttributes) {

        try {
            courseService.proveMentorRole();

            List<Type> types = typeRepository.findAll();
            model.addAttribute("types", types);

            model.addAttribute("courseCreateDto", new CourseCreateDto());

            for (Type type : types) {
                System.out.println("Type: " + type.getType());
            }

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
        courseCreateDto.setUserId(user.getId());

        if (result.hasErrors()) {
            // 유효성 검사 오류 처리
            return "course/create-course";
        }

        courseService.createCourse(courseCreateDto);
        model.addAttribute("courseCreated", true);
        return "redirect:/courses";
    }

    /* 전체 강의 리스트 페이지 */
    @GetMapping
    public String showCourseList(Model model) {
        List<Course> courses = courseService.getAllCourses();
        model.addAttribute("courses", courses);
        return "course/courses"; // Without the file extension (.html)
    }

    /* 개별 강의 페이지 */
    @GetMapping("/{id}")
    public String showCourse(@PathVariable Long id, Model model) {

        try {
            Course course = courseService.getCourse(id);
            model.addAttribute("course", course);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            boolean hasAdminAuthority = authentication.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
            model.addAttribute("hasAdminAuthority", hasAdminAuthority);

            String currentUser = authentication.getName();
            model.addAttribute("currentUser", currentUser);

            return "course/course-page";
        } catch (CourseNotFoundException ex) {
            model.addAttribute("errorMessage", "강의가 존재하지 않습니다.");
            return "course/courses";
        }
    }

    /* 강의 수정 폼으로 이동 */
    @GetMapping("/{id}/edit")
    public String showEditCourseForm(@PathVariable Long id, Model model) {
        Course course = courseService.getCourse(id);
        List<Type> allTypes = typeRepository.findAll();

        model.addAttribute("course", course);
        model.addAttribute("allTypes", allTypes);
        model.addAttribute("type", course.getType());

        return "course/edit-course";
    }

    /* 강의 수정 */
    @PatchMapping("/{id}/edit")
    public String editCourse(@PathVariable Long id, @ModelAttribute CoursePatchDto coursePatchDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            for (FieldError error : bindingResult.getFieldErrors()) {
                System.out.println("Field: " + error.getField() + " - Error: " + error.getDefaultMessage());
            }
            // You can also add more specific error handling here if needed
            return "course/course-page"; // Redirect to an error page or handle the errors as per your application's design
        }

        System.out.println("CoursePatchDto: " + coursePatchDto);

        // Print the videoList
        if (coursePatchDto.getVideoList() != null) {
            for (VideoCreateDto video : coursePatchDto.getVideoList()) {
                System.out.println("Video Title: " + video.getTitle());
                System.out.println("Video Source: " + video.getSource());
                System.out.println("Video Total Length: " + video.getTotalLength());
            }
        } else {
            System.out.println("Video List is null");
        }

        courseService.updateCourse(id, coursePatchDto);
        return "course/course-page";
    }

    /* 강의 삭제 폼으로 이동 */
    @GetMapping("/{id}/delete")
    public String showDeleteCourse(@PathVariable Long id, Model model) {
        model.addAttribute("courseId", id);
        return "course/delete-course";
    }

    /* 강의 삭제 */
    @PostMapping("/{id}/delete")
    public String deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return "redirect:/course/courses";
    }
}
