package com.launcher.inflaunch.repository;

import com.launcher.inflaunch.domain.ReportReview;
import com.launcher.inflaunch.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportReviewRepository extends JpaRepository<ReportReview, Long> {

    Optional<ReportReview> findByReviewId(Long reviewId);

    Optional<List<ReportReview>> findByUserIdAndReviewId(Long userId, Long reviewId);
}
