package com.launcher.inflaunch.dto;

import com.launcher.inflaunch.domain.Course;
import com.launcher.inflaunch.domain.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {
//    public Review reviewCreateDtoToReview(ReviewCreateDto reviewCreateDto) {
//        return Review.builder()
//                .id(reviewCreateDto.getUserId())
//                .id(reviewCreateDto.getCourseId())
//                .rate(reviewCreateDto.getRate())
//                .reviewWrite(reviewCreateDto.getReviewWrite())
//                .advantage(reviewCreateDto.getAdvantage())
//                .disadvantage(reviewCreateDto.getDisadvantage())
//                .reviewStatus(ReviewStatus.ACTIVE)
//                .build();
//    }

    public Review reviewPatchDtoToReview(ReviewPatchDto reviewPatchDto, Review existingReview) {
        Course updatedCourse = existingReview.getCourse();

        Review updatedReview = Review.builder()
                .id(existingReview.getId())
                .user(existingReview.getUser())
                .course(updatedCourse)
                .rate(reviewPatchDto.getRate() >= 0 ? reviewPatchDto.getRate() : existingReview.getRate())
                .reviewWrite(reviewPatchDto.getReviewWrite() != null ? reviewPatchDto.getReviewWrite() : existingReview.getReviewWrite())
                .advantage(reviewPatchDto.getAdvantage() != null ? reviewPatchDto.getAdvantage() : existingReview.getAdvantage())
                .disadvantage(reviewPatchDto.getDisadvantage() != null ? reviewPatchDto.getDisadvantage() : existingReview.getDisadvantage())
                .reportCount(existingReview.getReportCount())
                .reviewStatus(existingReview.getReviewStatus())
                .build();

        // Set the regDate from existingCourse
        updatedReview.setRegDate(existingReview.getRegDate());

        return updatedReview;
    }

//    public Review reviewDeleteDtoToReview(Review existingReview) {
//        return Review.builder()
//                .reviewStatus(ReviewStatus.DELETED)
//                .build();
//    }

}
