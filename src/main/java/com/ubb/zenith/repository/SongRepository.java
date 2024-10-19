package com.ubb.zenith.repository;

import com.ubb.zenith.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SongRepository extends JpaRepository<Song, Integer> {
    //pentru a verifica daca o melodie exista deja in baza de date
    Optional<Song> findByArtistAndTitle(String artist, String title);
}
