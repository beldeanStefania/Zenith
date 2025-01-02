package com.ubb.zenith.dto;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PlaylistRequest {
    private String username;
    private int happinessScore;
    private int sadnessScore;
    private int loveScore;
    private int energyScore;
    private String playlistName;
}
