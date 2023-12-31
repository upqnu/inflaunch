package com.launcher.inflaunch.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VideoCreateDto {

    @NotNull
    private Long courseId;

    @NotBlank
    private String title;

    @NotBlank
    private String source;

    // @Positive
    @PositiveOrZero
    private Integer totalLength;
}
