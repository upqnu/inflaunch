package com.launcher.inflaunch.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor // 강의 생성 시, 입력 폼을 불러올 때 비어 있는 CourseCreateDto를 모델을 추가하기 위해 필요
@AllArgsConstructor
@Builder
public class CourseCreateDto {
    @NotNull
    private Long userId;

    @NotNull(message = "강의의 타입을 선택하세요.")
    private String type;

    @NotBlank(message = "강의의 제목을 입력하세요.")
    private String title;

    @NotBlank(message = "강의 소개 내용을 입력하세요.")
    private String description;

    @Min(0)
    private int price;

    @Builder.Default
    private List<VideoCreateDto> videoList = new ArrayList<>();
}
