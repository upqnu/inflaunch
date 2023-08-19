package com.launcher.inflaunch.dto;

import com.launcher.inflaunch.enum_status.Advantage;
import com.launcher.inflaunch.enum_status.Disadvantage;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCreateDto {
    @NotNull
    private Long userId;

    @NotNull
    private Long courseId;

    private Float rate = 0.0f;

    @Min(5)
    private String reviewWrite;

    private Advantage advantage;

    private Disadvantage disadvantage;
}
