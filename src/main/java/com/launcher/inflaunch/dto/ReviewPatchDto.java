package com.launcher.inflaunch.dto;

import com.launcher.inflaunch.enum_status.Advantage;
import com.launcher.inflaunch.enum_status.Disadvantage;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewPatchDto {
    private Long id;

    private Float rate;

    @Min(5)
    private String reviewWrite;

    private Advantage advantage;

    private Disadvantage disadvantage;
}
