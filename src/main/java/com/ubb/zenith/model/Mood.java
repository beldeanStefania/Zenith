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

    @NotBlank(message = "happy_score cannot be empty")
    @Size(min = 1, max = 10, message = "happy_score must be between 1 and 10 characters")
    private Integer happiness_score;

    @NotBlank(message = "sadness_score cannot be empty")
    @Size(min = 1, max = 10, message = "sadness_score must be between 1 and 10 characters")
    private Integer sadness_score;

    @NotBlank(message = "anger_score cannot be empty")
    @Size(min = 1, max = 10, message = "anger_score must be between 1 and 10 characters")
    private Integer love_score;

    @NotBlank(message = "energy_score cannot be empty")
    @Size(min = 1, max = 10, message = "energy_score must be between 1 and 10 characters")
    private Integer energy_score;

    @OneToMany(mappedBy = "mood", cascade = ALL)
    @JsonManagedReference
    private List<Song> songs;
}
