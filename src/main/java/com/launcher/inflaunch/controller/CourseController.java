package com.launcher.inflaunch.controller;

import com.launcher.inflaunch.domain.Course;
import com.launcher.inflaunch.domain.Type;
import com.launcher.inflaunch.domain.User;
import com.launcher.inflaunch.dto.CourseCreateDto;
import com.launcher.inflaunch.repository.TypeRepository;
import com.launcher.inflaunch.repository.UserRepository;
import com.launcher.inflaunch.service.CourseService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    public String showCourseCreateForm(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {

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

}
