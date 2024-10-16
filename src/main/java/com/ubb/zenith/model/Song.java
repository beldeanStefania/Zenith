package com.ubb.zenith.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
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
    @NonNull
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @NotBlank(message = "Title cannot be empty")
    private String title;

    @NotBlank(message = "Artist cannot be empty")
    private String artist;

    private String genre;

    @ManyToOne
    @JoinColumn(name = "mood_id", nullable = false)
    @JsonBackReference
    private Mood mood;

}