package com.ubb.zenith.dto;

import com.ubb.zenith.model.Mood;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SongDTO {
    //dto pentru Song
    private String title;

    private String artist;

    private String genre;

    private Integer moodId;
}
