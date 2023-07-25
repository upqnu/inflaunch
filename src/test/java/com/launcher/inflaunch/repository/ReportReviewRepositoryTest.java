package com.launcher.inflaunch.repository;

import com.launcher.inflaunch.domain.ReportReview;
import com.launcher.inflaunch.domain.Review;
import com.launcher.inflaunch.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.PrintWriter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReportReviewRepositoryTest {

    PrintWriter pw = new PrintWriter(System.out);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReportReviewRepository reportReviewRepository;

    @Test
    void init() {
        User user1 = userRepository.findByUsername("member1");
        User user2 = userRepository.findByUsername("member2");

        Optional<Review> review1 = reviewRepository.findById(1L);
        Optional<Review> review2 = reviewRepository.findById(2L);
        Optional<Review> review3 = reviewRepository.findById(3L);
        Optional<Review> review4 = reviewRepository.findById(4L);
        Optional<Review> review5 = reviewRepository.findById(5L);

        ReportReview reportReview1 = ReportReview.builder()
                .user(user1)
                .review(review1.get())
                .content("수강평이 뭔가 이상합니다. 조금 이상합니다. 많이 이상합니다.이상하다구요.")
                .build();

        ReportReview reportReview2 = ReportReview.builder()
                .user(user1)
                .review(review2.get())
                .content("리뷰가 뭔가 이상하다. 조금 이상해. 사실 많이 이상해. 주작 냄새가...")
                .build();

        ReportReview reportReview3 = ReportReview.builder()
                .user(user2)
                .review(review3.get())
                .content("이 리뷰... 강사 친구가 대신 써 준 것 같다. 물증은 없고 심증만 있으니...")
                .build();

        ReportReview reportReview4 = ReportReview.builder()
                .user(user1)
                .review(review4.get())
                .content("나쁜 수업은 아니지만 이 수강평, 평점은 너무 장점만을 과장한 거 같다. 확인 부탁해요")
                .build();

        ReportReview reportReview5 = ReportReview.builder()
                .user(user2)
                .review(review5.get())
                .content("걍 수강평을 쓴 말투가 맘에 들지 않아. 가뜩이나 지구온난화로 더워 미칠 지경인데")
                .build();

        reportReview1 = reportReviewRepository.save(reportReview1);
        reportReview2 = reportReviewRepository.save(reportReview2);
        reportReview3 = reportReviewRepository.save(reportReview3);
        reportReview4 = reportReviewRepository.save(reportReview4);
        reportReview5 = reportReviewRepository.save(reportReview5);

        reportReviewRepository.findAll().forEach(pw::println);

        pw.close();
    }

//    @Test
//    void delete() {
//        reportReviewRepository.deleteAll();
//    }

}