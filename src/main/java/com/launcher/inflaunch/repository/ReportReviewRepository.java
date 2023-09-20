package com.launcher.inflaunch.repository;

import com.launcher.inflaunch.domain.ReportReview;
import com.launcher.inflaunch.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReportReviewRepository extends JpaRepository<ReportReview, Long> {

    Optional<ReportReview> findByReviewId(Long reviewId);

    Optional<List<ReportReview>> findByUserIdAndReviewId(Long userId, Long reviewId);
}
