package com.ubb.zenith.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Mood {

    @Id
    @NotNull
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @NotNull(message = "happiness_score cannot be null")
    @Min(value = 1, message = "happiness_score must be at least 1")
    @Max(value = 10, message = "happiness_score must be at most 10")
    private Integer happiness_score;

    @NotNull(message = "sadness_score cannot be null")
    @Min(value = 1, message = "sadness_score must be at least 1")
    @Max(value = 10, message = "sadness_score must be at most 10")
    private Integer sadness_score;

    @NotNull(message = "love_score cannot be null")
    @Min(value = 1, message = "love_score must be at least 1")
    @Max(value = 10, message = "love_score must be at most 10")
    private Integer love_score;

    @NotNull(message = "energy_score cannot be null")
    @Min(value = 1, message = "energy_score must be at least 1")
    @Max(value = 10, message = "energy_score must be at most 10")
    private Integer energy_score;

    @OneToMany(mappedBy = "mood", cascade = ALL)
    @JsonManagedReference
    private List<Song> songs;
}