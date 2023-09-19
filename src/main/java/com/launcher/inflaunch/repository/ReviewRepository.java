package com.launcher.inflaunch.repository;

import com.launcher.inflaunch.domain.Review;
import com.launcher.inflaunch.enum_status.ReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findById(Long id);

    List<Review> findByReviewStatus(ReviewStatus reviewStatus);

    List<Review> findByCourseIdAndReviewStatus(Long courseId, ReviewStatus reviewStatus);
}
