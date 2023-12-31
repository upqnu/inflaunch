package com.launcher.inflaunch.controller;

import com.launcher.inflaunch.domain.Review;
import com.launcher.inflaunch.domain.User;
import com.launcher.inflaunch.dto.ReviewCreateDto;
import com.launcher.inflaunch.dto.ReviewPatchDto;
import com.launcher.inflaunch.repository.UserRepository;
import com.launcher.inflaunch.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
@RequestMapping("/courses/{courseId}")
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;
    private final UserRepository userRepository;

    /* 수강평 작성 폼으로 이동 */
    @GetMapping("/newreview")
    public String showCreateReviewForm(@PathVariable Long courseId, Model model, RedirectAttributes redirectAttributes) {
        try {
            boolean hasReviewCreateAuthority = reviewService.hasReviewCreateAuthority(courseId);
//            log.info("ㅋㅋㅋ" + String.valueOf(hasReviewCreateAuthority));
            ReviewCreateDto reviewCreateDto = new ReviewCreateDto();
            reviewCreateDto.setCourseId(courseId);
            model.addAttribute("reviewCreateDto", reviewCreateDto);
            model.addAttribute("hasReviewCreateAuthority", hasReviewCreateAuthority);
            model.addAttribute("advantageEnum", com.launcher.inflaunch.enum_status.Advantage.values());
            model.addAttribute("disadvantageEnum", com.launcher.inflaunch.enum_status.Disadvantage.values());

            return "course/create-review";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "강의를 생성할 권한이 없습니다.");
            return "redirect:/courses/{courseId}";
        }
    }

    /* 수강평 작성 */
    @PostMapping("/newreview")
    @PreAuthorize("isAuthenticated()") // 인증된 사용자만 접근 가능하도록 설정
    public String createReview(
            @PathVariable Long courseId,
            @ModelAttribute ReviewCreateDto reviewCreateDto,
            BindingResult result, Model model, Principal principal) {

        String username = principal.getName();
        User user = userRepository.findByUsername(username);
        reviewCreateDto.setUserId(user.getId());

        if (result.hasErrors()) {
            // 유효성 검사 오류 처리
            return "course/create-review";
        }

        reviewCreateDto.setCourseId(courseId);

        reviewService.createReview(reviewCreateDto, user);
        model.addAttribute("reviewCreated", true);
        return "redirect:/courses/{courseId}";
    }

    /* 수강평 리스트*/
    // @GetMapping("")
    // public String showReviewPage(@PathVariable Long courseId, @RequestParam(required = false) Long reviewId, Model model, Principal principal) {
    //     List<Review> reviews = reviewService.getAllReviews(courseId);
    //     model.addAttribute("reviews", reviews);

    //     boolean hasReviews = !reviews.isEmpty();
    //     model.addAttribute("hasReviews", hasReviews);

    //     // Get the currently logged in user (if available)
    //     User currentUser = null;
    //     if (principal != null) {
    //         String currentUserName = principal.getName();
    //         currentUser = userRepository.findByUsername(currentUserName);
    //     }
    //     model.addAttribute("currentUser", currentUser);

    //     return "course/course-page";
    // }

    /* 수강평 수정 폼으로 이동 */
    @GetMapping("/reviews/{reviewId}/edit")
    public String showEditReviewForm(@PathVariable Long courseId, @PathVariable Long reviewId, Model model) {
        Review review = reviewService.getReview(reviewId);
        ReviewPatchDto reviewPatchDto = new ReviewPatchDto();

        reviewPatchDto.setRate(review.getRate());
        reviewPatchDto.setReviewWrite(review.getReviewWrite());
        reviewPatchDto.setAdvantage(review.getAdvantage());
        reviewPatchDto.setDisadvantage(review.getDisadvantage());

        model.addAttribute("courseId", courseId);
        model.addAttribute("review", review);
        model.addAttribute("reviewPatchDto", reviewPatchDto);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User currentUser = userRepository.findByUsername(currentPrincipalName);

        model.addAttribute("currentUser", currentUser);

        return "course/edit-review";
    }

    /* 수강평 수정 */
    @PatchMapping("/reviews/{reviewId}/edit")
    public String editReview(@PathVariable Long courseId, @PathVariable Long reviewId, @ModelAttribute ReviewPatchDto reviewPatchDto, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "course/course-page";
        }

        reviewService.updateReview(reviewId, reviewPatchDto);
        redirectAttributes.addAttribute("courseId", courseId);
        return "redirect:/courses/{courseId}";
    }

    /* 수강평 삭제 폼으로 이동*/
    @GetMapping("/reviews/{reviewId}/delete")
    public String showReviewCourse(@PathVariable Long reviewId, @PathVariable Long courseId, Model model) {
        model.addAttribute("reviewId", reviewId);
        model.addAttribute("courseId", courseId);

        return "course/delete-review";
    }

    /* 수강평 삭제*/
    @PostMapping("/reviews/{reviewId}/delete")
    public String deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return "redirect:/courses/{courseId}";
    }

}
