package com.ubb.zenith.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Song {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @NotBlank(message = "Title cannot be empty")
    private String title;

    @NotBlank(message = "Artist cannot be empty")
    private String artist;

    private String genre;

    @Lob // Anotare pentru a specifica că acesta este un obiect mare
    private byte[] audioData; // Fișier audio stocat ca byte array

    @ManyToOne
    @JoinColumn(name = "playlist_id")
    @JsonManagedReference
    private Playlist playlist;

    @ManyToOne
    @JoinColumn(name = "mood_id")
    @JsonBackReference
    private Mood mood;
}