package com.launcher.inflaunch.domain;

import com.launcher.inflaunch.enum_status.Advantage;
import com.launcher.inflaunch.enum_status.Disadvantage;
import com.launcher.inflaunch.enum_status.ReviewStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    @ManyToOne
    @JoinColumn(name = "course_id")
    @ToString.Exclude
    private Course course;

    private Float rate;

    @Size(min = 5)
    private String reviewWrite;

    @Enumerated(EnumType.STRING)
    private Advantage advantage;

    @Enumerated(EnumType.STRING)
    private Disadvantage disadvantage;

    @Enumerated(EnumType.STRING)
    private ReviewStatus reviewStatus;

    public void setReviewStatus(ReviewStatus reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    @OneToMany(mappedBy = "review",  cascade = CascadeType.ALL)
    @ToString.Exclude
    @Builder.Default
    private List<ReportReview> reportReviewList = new ArrayList<>();

    private Integer reportCount;

    public void incrementReportCount() {
        this.reportCount++;
    }

    public void updateReviewStatus(ReviewStatus newStatus) {
        this.reviewStatus = newStatus;
    }
}

