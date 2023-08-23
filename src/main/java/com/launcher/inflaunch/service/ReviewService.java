package com.launcher.inflaunch.service;

import com.launcher.inflaunch.domain.*;
import com.launcher.inflaunch.dto.ReviewCreateDto;
import com.launcher.inflaunch.dto.ReviewMapper;
import com.launcher.inflaunch.dto.ReviewPatchDto;
import com.launcher.inflaunch.enum_status.ReviewStatus;
import com.launcher.inflaunch.exception.ReviewNotFoundException;
import com.launcher.inflaunch.repository.CourseRepository;
import com.launcher.inflaunch.repository.ReviewRepository;
import com.launcher.inflaunch.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final ReviewMapper reviewMapper;

    /* 수강평 생성 권한 있는지 확인 */
    @Transactional
    public void hasReviewCreateAuthority() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new IllegalArgumentException("User not found: " + username);
        }

        // user가 해당 강의를 결제하지 않았다면 수강평을 작성할 권한이 없음
//        if (!isPaidCourse(user)) {
//            throw new AccessDeniedException("수강평을 작성할 권한이 없습니다.");
//        }
    }

    /* 수강평 작성 */
    @Transactional
    public void createReview(ReviewCreateDto reviewCreateDto, User reviewUser) {

        // course가 null인 경우 예외 발생
        Course reviewCourse = courseRepository.findById(reviewCreateDto.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("수강평을 작성할 강의를 찾을 수 없습니다: " + reviewCreateDto.getCourseId()));

        // 필요한 정보 입력하면서 수강평 생성
        Review newReview = Review.builder()
                .user(reviewUser)
                .course(reviewCourse)
                .rate(reviewCreateDto.getRate())
                .reviewWrite(reviewCreateDto.getReviewWrite())
                .advantage(reviewCreateDto.getAdvantage())
                .disadvantage(reviewCreateDto.getDisadvantage())
                .reviewStatus(ReviewStatus.ACTIVE)
                .build();

        reviewRepository.save(newReview);
    }

    /* 특정 강의에 대해 작성된 모든 유효한 수강평 리스트*/
    public List<Review> getAllReviews(Long courseId) {
        return reviewRepository.findByCourseIdAndReviewStatus(courseId, ReviewStatus.ACTIVE);
    }

    /* 개별 수강평 */
    @Transactional
    public Review getReview(Long reviewId) {
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);

        if (optionalReview.isPresent()) {
            Review review = optionalReview.get();
            if (review.getReviewStatus() == ReviewStatus.ACTIVE) {
                return review;
            } else {
                throw new ReviewNotFoundException("강의가 존재하지 않습니다.");
            }
        } else {
            throw new ReviewNotFoundException("강의가 존재하지 않습니다.");
        }
    }

    /* 수강평 수정 */
    @Transactional
    public void updateReview(Long reviewId, ReviewPatchDto reviewPatchDto) {
        try {
            Review existingReview = reviewRepository.findById(reviewId)
                    .orElseThrow(() -> new ReviewNotFoundException(reviewId + " 번 id의 수강평이 존재하지 않습니다."));

            if (existingReview.getReviewStatus() != ReviewStatus.ACTIVE) {
                throw new IllegalArgumentException("이 수강평은 수정이 불가능한 상태입니다.");
            }

            Review updatedReview = reviewMapper.reviewPatchDtoToReview(reviewPatchDto, existingReview);

            reviewRepository.save(updatedReview);

            log.info(updatedReview + " Updated");
        } catch (ReviewNotFoundException e) {
            log.error(reviewId + " 번 id의 수강평이 존재하지 않습니다.", e);
        }
    }

    /* 로그인한 유저 여부 확인 */
    @Transactional
    public UserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null; // No user authenticated or anonymous user
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            return (UserDetails) principal;
        }

        return null;
    }

    /* 수강평 삭제 */
    public void deleteReview(Long reviewId) {
        Review existingReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException(reviewId + " 번 id의 수강평이 존재하지 않습니다."));

        if (existingReview.getReviewStatus() != ReviewStatus.ACTIVE) {
            throw new IllegalArgumentException("이 수강평은 삭제가 불가능한 상태입니다.");
        }

        Review deletedReview = reviewMapper.reviewDeleteDtoToReview(existingReview);
        reviewRepository.save(deletedReview);
    }
}
