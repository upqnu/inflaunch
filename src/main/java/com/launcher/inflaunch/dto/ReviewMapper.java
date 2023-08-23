package com.launcher.inflaunch.dto;

import com.launcher.inflaunch.domain.Review;
import com.launcher.inflaunch.dto.ReviewPatchDto;
import com.launcher.inflaunch.enum_status.ReviewStatus;
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
        return Review.builder()
                .id(reviewPatchDto.getId())
                .user(existingReview.getUser())
                .course(existingReview.getCourse())
                .rate(reviewPatchDto.getRate() >= 0 ? reviewPatchDto.getRate() : existingReview.getRate())
                .reviewWrite(reviewPatchDto.getReviewWrite() != null ? reviewPatchDto.getReviewWrite() : existingReview.getReviewWrite())
                .advantage(reviewPatchDto.getAdvantage() != null ? reviewPatchDto.getAdvantage() : existingReview.getAdvantage())
                .disadvantage(reviewPatchDto.getDisadvantage() != null ? reviewPatchDto.getDisadvantage() : existingReview.getDisadvantage())
                .reviewStatus(existingReview.getReviewStatus())
                .build();
    }

    public Review reviewDeleteDtoToReview(Review existingReview) {
        return Review.builder()
                .reviewStatus(ReviewStatus.DELETED)
                .build();
    }

}
