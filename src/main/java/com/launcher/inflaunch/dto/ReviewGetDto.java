package com.launcher.inflaunch.dto;

import com.launcher.inflaunch.domain.Review;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewGetDto {
    private List<Review> reviews;
    private Integer reviewCount;
    private Float averageRate;
}
