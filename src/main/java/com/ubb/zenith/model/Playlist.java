package com.ubb.zenith.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Playlist {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @OneToMany(mappedBy = "playlist",cascade = ALL)
    @JsonBackReference
    private List<Song> songs;

    @OneToMany
    @JsonBackReference
    private List<UserPlaylist> userPlaylist;

    @NotBlank(message = "Playlist name cannot be empty")
    @Size(min = 3, max = 20, message = "Playlist name must be between 3 and 20 characters")
    private String name;

}