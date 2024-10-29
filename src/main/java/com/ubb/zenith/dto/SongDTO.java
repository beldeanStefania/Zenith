package com.ubb.zenith.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SongDTO {
    private String title;
    private String artist;
    private String genre;
    private Integer moodId;
}