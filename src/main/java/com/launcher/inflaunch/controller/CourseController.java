package com.launcher.inflaunch.controller;

import com.launcher.inflaunch.domain.Course;
import com.launcher.inflaunch.dto.CourseCreateDto;
import com.launcher.inflaunch.service.CourseService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    /* 강의 생성 정보를 입력하는 폼으로 이동 */
    @GetMapping("/new")
    public String showCourseCreateForm(Model model) {
        model.addAttribute("courseCreateDto", new CourseCreateDto());
        return "course/create-course";
    }

    /* 강의 생성  */
    @PostMapping("/new")
    public String createCourse(@ModelAttribute CourseCreateDto courseCreateDto) {
        courseService.createCourse(courseCreateDto);
        return "redirect:/courses";
    }

}
