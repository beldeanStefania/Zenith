package com.ubb.zenith.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MoodDTO {

    private Integer happiness_score;
    private Integer sadness_score;
    private Integer love_score;
    private Integer energy_score;
}
