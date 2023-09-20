package com.launcher.inflaunch.service;

import com.launcher.inflaunch.config.PrincipalDetails;
import com.launcher.inflaunch.domain.Authority;
import com.launcher.inflaunch.domain.ReportReview;
import com.launcher.inflaunch.domain.Review;
import com.launcher.inflaunch.domain.User;
import com.launcher.inflaunch.dto.ReportReviewCreateDto;
import com.launcher.inflaunch.enum_status.ReviewStatus;
import com.launcher.inflaunch.repository.ReportReviewRepository;
import com.launcher.inflaunch.repository.ReviewRepository;
import com.launcher.inflaunch.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class ReportReviewService {

    private final ReviewRepository reviewRepository;
    private final ReportReviewRepository reportReviewRepository;
    private final UserRepository userRepository;

    /* 수강평을 신고할 수 있는 권한을 가진 유저인지 판별 */
    @Transactional
    public Long getUserIdForReportReviewCreate(Long reviewId) {
//        // 모든 로그인한 유저는 어떠한 수강평에 대해서도 신고가 가능하다
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
//        User user = userRepository.findByUsername(username);
//
//        if (user == null) {
//            throw new IllegalArgumentException("유저를 찾을 수 없습니다 : " + username);
//        }
//
//        return user.getId();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            throw new AccessDeniedException("로그인이 필요합니다.");
        }

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Long userId = principalDetails.getUser().getId();

        Optional<List<ReportReview>> reportReviews = reportReviewRepository.findByUserIdAndReviewId(userId, reviewId);
        if (reportReviews.isPresent() && !reportReviews.get().isEmpty()) {
            throw new AccessDeniedException("이미 해당 수강평을 신고하셨습니다.");
        }
        else return userId;
    }

    /* 수강평 신고 작성 */
    @Transactional
    public void createReportReview(ReportReviewCreateDto reportReviewCreateDto, User reviewReportingUser) {
        // review가 null인 경우 예외 발생
        Review fakeReview = reviewRepository.findById(reportReviewCreateDto.getReviewId())
                .orElseThrow(() -> new IllegalArgumentException(reportReviewCreateDto.getReviewId() + "수강평이 존재하지 않습니다."));

        // content가 null인 경우 예외 발생
        if (reportReviewCreateDto.getContent() == null) {
            throw new IllegalArgumentException("수강평 신고 내용이 없습니다.");
        }

        // 필요한 정보 입력하면서 수강평 신고 생성
        ReportReview newReportReview = ReportReview.builder()
                .user(reviewReportingUser)
                .review(fakeReview)
                .content(reportReviewCreateDto.getContent())
                .build();

        reportReviewRepository.save(newReportReview);

        // Increment reportCount and update reviewStatus if necessary
        fakeReview.incrementReportCount();

        if (fakeReview.getReportCount() >= 5) {
            fakeReview.updateReviewStatus(ReviewStatus.INACTIVE);
        }
    }

    /* 유저가 admin 권한을 가지고 있는지 여부 */
    @Transactional
    public boolean hasAdminAuthority(User user) {
        for (Authority authority : user.getAuthorities()) {
            if ("ROLE_ADMIN".equals(authority.getName())) {
                return true;
            }
        }
        return false;
    }
}
