package com.ubb.zenith.dto;

import com.ubb.zenith.model.Song;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PlaylistDTO {
    private String name;
    private Integer id_song;
}
