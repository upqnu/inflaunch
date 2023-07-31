package com.launcher.inflaunch.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor // 강의 생성 시, 입력 폼을 불러올 때 비어 있는 CourseCreateDto를 모델을 추가하기 위해 필요
@AllArgsConstructor
public class CourseCreateDto {
    @NotNull
    private Long userId;

    @NotNull
    private String typeName;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @Min(0)
    private int price;

    private List<VideoCreateDto> videoList;
}
