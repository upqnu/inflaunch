package com.launcher.inflaunch.controller;

import com.launcher.inflaunch.domain.User;
import com.launcher.inflaunch.dto.ReportReviewCreateDto;
import com.launcher.inflaunch.repository.UserRepository;
import com.launcher.inflaunch.service.ReportReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
@RequestMapping("/courses/{courseId}/reviews")
public class ReportReviewController {

    private final ReportReviewService reportReviewService;
    private final UserRepository userRepository;

    /* 수강평 신고 페이지로 이동 */
    @GetMapping("/{reviewId}/report")
    public String showReportReview(@PathVariable Long reviewId, Model model) {
        Long userId = reportReviewService.getUserIdForReportReviewCreate();

        ReportReviewCreateDto reportReviewCreateDto = new ReportReviewCreateDto();
        reportReviewCreateDto.setReviewId(reviewId);
        reportReviewCreateDto.setUserId(userId);
        reportReviewCreateDto.setContent("");

        model.addAttribute("reportReviewCreateDto", reportReviewCreateDto);
        return "course/create-reportreview";
    }

    /* 수강평 신고 */
    @PostMapping("/{reviewId}/report")
    public String createReportReview(
            @PathVariable Long reviewId,
            @ModelAttribute ReportReviewCreateDto reportReviewCreateDto,
            RedirectAttributes redirectAttributes,
            Model model, Principal principal
    ) {
        try {
            String username = principal.getName();
            User user = userRepository.findByUsername(username);
            reportReviewCreateDto.setUserId(user.getId());

            reportReviewCreateDto.setReviewId(reviewId);

            reportReviewService.createReportReview(reportReviewCreateDto, user);
            model.addAttribute("reportReviewCreated", true);
            return "redirect:/courses/{courseId}/reviews";
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("error message", "수강평을 신고할 수 없습니다.");
            return "redirect:/courses/{courseId}/reviews";
        }
    }

}
