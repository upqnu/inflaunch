package com.launcher.inflaunch.domain;

import com.launcher.inflaunch.enum_status.VideoStatus;
import com.launcher.inflaunch.enum_status.WatchStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
public class Video extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id")
    @ToString.Exclude
    private Course course;

    public void setCourse(Course course) {
        this.course = course;
    }

    @NotBlank
    private String title;

    /* 스트리밍할 동영상이 저장된 URL */
    @NotBlank
    private String source;

    /* 강의영상의 길이 - 초(second)로 표시한다. */
    @Column(nullable = false)
    @PositiveOrZero
    private Integer totalLength;

    private Integer lastViewingTime;

    @Enumerated(EnumType.STRING)
    private WatchStatus watchStatus;

    @Enumerated(EnumType.STRING)
    private VideoStatus videoStatus;

    public Video(String title, String source, int totalLength, Course course) {
        this.title = title;
        this.source = source;
        this.totalLength = totalLength;
        this.course = course;
        this.videoStatus = VideoStatus.ACTIVE;
        this.lastViewingTime = 0;
        this.watchStatus = WatchStatus.NOT_WATCHED;
    }
}
