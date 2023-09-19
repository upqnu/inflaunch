package com.launcher.inflaunch.dto;

import com.launcher.inflaunch.domain.Type;
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
@NoArgsConstructor
@AllArgsConstructor
public class CoursePatchDto {
    private Long id;

    @NotNull
    private Type type;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @Min(0)
    private int price;

    private List<VideoCreateDto> videoList;

    public CoursePatchDto(List<VideoCreateDto> videoList) {
        this.videoList = videoList;
    }
}
