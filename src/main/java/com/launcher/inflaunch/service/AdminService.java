package com.launcher.inflaunch.service;

import com.launcher.inflaunch.domain.ReportReview;
import com.launcher.inflaunch.domain.Review;
import com.launcher.inflaunch.dto.ReportReviewInfoDto;
import com.launcher.inflaunch.enum_status.ReviewStatus;
import com.launcher.inflaunch.exception.ReviewNotFoundException;
import com.launcher.inflaunch.repository.ReportReviewRepository;
import com.launcher.inflaunch.repository.ReviewRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.hibernate.validator.internal.util.logging.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@Service
@Slf4j
public class AdminService {

    private final ReportReviewRepository reportReviewRepository;
    private final ReviewRepository reviewRepository;

    /* 신고된 수강평 조회 */
    @Transactional
    public Page<ReportReviewInfoDto> getReportedReviews(Pageable pageable) {

//        List<ReportReview> reportedReviews = reportReviewRepository.findAll();
        Page<ReportReview> reportedReviewsPage = reportReviewRepository.findAll(pageable);
        List<ReportReviewInfoDto> reportReviewInfoDtos = new ArrayList<>();

        if (reportedReviewsPage.hasContent()) {
            for (ReportReview reportReview : reportedReviewsPage.getContent()) {
                Review reportedReview = reportReview.getReview();

                /* 신고되지 않은 수강평에 대한 정보는 노출할 필요가 없다. 하나라도 신고가 접수되면 admin이 확인할 수 있도록 한다. */
                if (reportedReview.getReportCount() >= 0) {

                    // reportReview에 대한 정보
                    Long reportReviewId = reportReview.getId();
                    String reportReviewContent = reportReview.getContent();
                    Long reviewReportingUserId = reportReview.getUser().getId();
                    String reviewReportingUsername = reportReview.getUser().getUsername();

                    // 신고된 review 정보
                    Long reportedReviewId = reportedReview.getId();
                    String reportedReviewWrite = reportedReview.getReviewWrite();
                    Long reviewUserId = reportReview.getUser().getId();
                    String reviewUsername = reportReview.getUser().getUsername();
                    Long reportedReviewCourseId = reportedReview.getCourse().getId();
                    String reportedReviewCourseTitle = reportedReview.getCourse().getTitle();
                    Integer reportCount = reportedReview.getReportCount();
                    ReviewStatus reviewStatus = reportedReview.getReviewStatus();

                    // ReportReviewInfoDto 객체에 정보 추가
                    ReportReviewInfoDto reportReviewInfoDto = new ReportReviewInfoDto(
                            reportReviewId, reportReviewContent, reviewReportingUserId, reviewReportingUsername,
                            reportedReviewId, reportedReviewWrite, reviewUserId, reviewUsername,
                            reportedReviewCourseId, reportedReviewCourseTitle, reportCount, reviewStatus
                    );
                    reportReviewInfoDtos.add(reportReviewInfoDto);
                }
            }
        }

        return reportedReviewsPage.getTotalElements() == 0? new PageImpl<>(Collections.emptyList(), pageable, reportedReviewsPage.getTotalElements()) : new PageImpl<>(reportReviewInfoDtos, pageable, reportedReviewsPage.getTotalElements());
    }

    /* 신고된 수강평에 대한 처리 */
    @Transactional
    public void updateReviewStatus(Long reviewId, ReviewStatus reviewStatus) {
        Review readyToBeUpdatedReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException(reviewId + "번 ID의 수강평이 존재하지 않습니다."));

        log.info("2. Status 수정 시작");

        readyToBeUpdatedReview.setRegDate(readyToBeUpdatedReview.getRegDate());
        readyToBeUpdatedReview.setReviewStatus(reviewStatus);

        reviewRepository.save(readyToBeUpdatedReview);
        log.info("3. ID {}번 수강평은 {} 처리하였습니다.", reviewId, reviewStatus);
    }
}
