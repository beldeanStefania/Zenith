package com.ubb.zenith.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserPlaylistDTO {
    private String username;
    private String playlistName;
    private LocalDate date;
}
