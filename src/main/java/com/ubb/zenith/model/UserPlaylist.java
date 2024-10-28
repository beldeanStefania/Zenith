package com.ubb.zenith.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDate;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPlaylist {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "user_id")
    private User user;

    //    @OneToMany(mappedBy = "userPlaylist", cascade = ALL)
//    @JsonManagedReference
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;

    private LocalDate date;
}
