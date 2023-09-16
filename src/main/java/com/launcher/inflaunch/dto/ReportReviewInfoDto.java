package com.launcher.inflaunch.dto;

import com.launcher.inflaunch.enum_status.ReviewStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportReviewInfoDto {
    private Long reportReviewId;
    private String reportReviewContent;
    private Long reviewReportingUserId;
    private String reviewReportingUsername;
    private Long reportedReviewId;
    private String reportedReviewWrite;
    private Long reviewUserId;
    private String reviewUsername;
    private Long reportedReviewCourseId;
    private String reportedReviewCourseTitle;
    private Integer reportCount;
    private ReviewStatus reviewStatus;

    public ReportReviewInfoDto(Long reportReviewId, String reportReviewContent, Long reviewReportingUserId,
                               String reviewReportingUsername, Long reportedReviewId, String reportedReviewWrite,
                               Long reviewUserId, String reviewUsername, Long reportedReviewCourseId,
                               String reportedReviewCourseTitle, Integer reportCount, ReviewStatus reviewStatus) {
        this.reportReviewId = reportReviewId;
        this.reportReviewContent = reportReviewContent;
        this.reviewReportingUserId = reviewReportingUserId;
        this.reviewReportingUsername = reviewReportingUsername;
        this.reportedReviewId = reportedReviewId;
        this.reportedReviewWrite = reportedReviewWrite;
        this.reviewUserId = reviewUserId;
        this.reviewUsername = reviewUsername;
        this.reportedReviewCourseId = reportedReviewCourseId;
        this.reportedReviewCourseTitle = reportedReviewCourseTitle;
        this.reportCount = reportCount;
        this.reviewStatus = reviewStatus;
    }
}
