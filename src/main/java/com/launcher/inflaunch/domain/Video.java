package com.launcher.inflaunch.domain;

import com.launcher.inflaunch.enum_status.VideoStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

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
    @ToString.Exclude
    private Course course;

    @NotBlank
    private String title;

    /* 스트리밍할 동영상이 저장된 URL */
    @NotBlank
    private String source;

    /* 강의영상의 길이 - 초(second)로 표시한다. */
    @NotNull
    @Positive
    private int totalLength;

    @Enumerated(EnumType.STRING)
    private VideoStatus videoStatus;
}
