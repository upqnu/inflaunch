package com.launcher.inflaunch.repository;

import com.launcher.inflaunch.domain.ReportReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReportReviewRepository extends JpaRepository<ReportReview, Long> {

    Optional<ReportReview> findByReviewId(Long reviewId);
}
