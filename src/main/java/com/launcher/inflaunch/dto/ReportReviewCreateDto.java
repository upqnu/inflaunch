package com.launcher.inflaunch.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportReviewCreateDto {

    @NotNull
    private Long userId;

    @NotNull
    private Long reviewId;

    @NotNull
    @Size(min = 20)
    private String content;
}
